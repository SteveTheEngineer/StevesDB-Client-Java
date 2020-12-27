package me.ste.stevesdbclient;

import me.ste.stevesdbclient.database.Database;
import me.ste.stevesdbclient.network.NetworkManager;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.network.packet.in.*;
import me.ste.stevesdbclient.network.packet.out.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class StevesDBClient {
    static {
        NetworkManager.getInstance().registerPacketsOut(
                AddColumnPacket.class,
                AddEntryPacket.class,
                CreateDatabasePacket.class,
                CreateTablePacket.class,
                DeleteDatabasePacket.class,
                DeleteTablePacket.class,
                EncryptionEnabledPacket.class,
                EncryptionPacket.class,
                HelloPacket.class,
                ListColumnsPacket.class,
                ListDatabasesPacket.class,
                ListEntriesPacket.class,
                ListTablesPacket.class,
                LoginPacket.class,
                LogoutPacket.class,
                ModifyEntryPacket.class,
                RemoveColumnPacket.class,
                RemoveEntryPacket.class,
                RenameColumnPacket.class,
                RenameDatabasePacket.class,
                RenameTablePacket.class
        );

        NetworkManager.getInstance().registerPacketsIn(
                AddColumnResponsePacket.class,
                AddEntryResponsePacket.class,
                CreateDatabaseResponsePacket.class,
                CreateTableResponsePacket.class,
                DeleteDatabaseResponsePacket.class,
                DeleteTableResponsePacket.class,
                EncryptionResponsePacket.class,
                HelloResponsePacket.class,
                ListColumnsResponsePacket.class,
                ListDatabasesResponsePacket.class,
                ListEntriesResponsePacket.class,
                ListTablesResponsePacket.class,
                LoginResponsePacket.class,
                LogoutResponsePacket.class,
                ModifyEntryResponsePacket.class,
                RemoveColumnResponsePacket.class,
                RemoveEntryResponsePacket.class,
                RenameColumnResponsePacket.class,
                RenameDatabaseResponsePacket.class,
                RenameTableResponsePacket.class
        );
    }

    /**
     * The protocol version this client was made to work with
     */
    public static final int PROTOCOL_VERSION = 3;
    /**
     * The version of the client itself
     */
    public static final String CLIENT_VERSION = StevesDBClient.class.getPackage().getImplementationVersion();

    private AsynchronousSocketChannel socket;
    private final Map<Class<? extends PacketIn>, Collection<CompletableFuture<? extends PacketIn>>> waiting = new HashMap<>();

    private byte[] encryptionSecret = null;
    private int serverProtocol;
    private String serverVersion;

    private String username;

    public StevesDBClient() {
    }

    /**
     * Establish a connection with a StevesDB server
     * @param address the server address
     * @param encryption whether to enable packet encryption
     * @return true, if the connection was established
     */
    public CompletableFuture<Boolean> connect(SocketAddress address, boolean encryption) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                this.socket = AsynchronousSocketChannel.open();
                this.socket.connect(address).get();
                this.read();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            this.sendPacket(new HelloPacket(StevesDBClient.PROTOCOL_VERSION, StevesDBClient.CLIENT_VERSION));
            try {
                HelloResponsePacket helloResponse = this.waitForResponse(HelloResponsePacket.class).get();

                this.serverProtocol = helloResponse.getProtocolVersion();
                this.serverVersion = helloResponse.getServerVersion();
                if(this.serverProtocol == StevesDBClient.PROTOCOL_VERSION) {
                    if(encryption) {
                        this.sendPacket(new EncryptionPacket());
                        EncryptionResponsePacket encryptionResponse = this.waitForResponse(EncryptionResponsePacket.class).get();
                        Cipher cipher = Cipher.getInstance("RSA");
                        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                        keyGenerator.init(128);
                        SecretKey secretKey = keyGenerator.generateKey();
                        cipher.init(Cipher.ENCRYPT_MODE, encryptionResponse.getPublicKey());
                        cipher.update(secretKey.getEncoded());
                        this.sendPacket(new EncryptionEnabledPacket(cipher.doFinal()));
                        this.encryptionSecret = secretKey.getEncoded();
                    }
                    return true;
                } else {
                    throw new RuntimeException("Incompatible protocol versions");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Get the current user username
     * @return the current user username
     */
    @Nullable
    public String getUsername() {
        return this.username;
    }

    /**
     * Get whether the connection is authenticated
     * @return true, if the connection is authenticated
     */
    public boolean isLoggedIn() {
        return this.username != null;
    }

    /**
     * Authenticate the connection
     * @param username user username
     * @param password user password
     * @return true, if the login credentials are correct and the connection is authenticated
     */
    public CompletableFuture<Boolean> login(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            this.sendPacket(new LoginPacket(username, password));
            try {
                if(this.waitForResponse(LoginResponsePacket.class).get().isSuccess()) {
                    this.username = username;
                    return true;
                } else {
                    return false;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Deauthenticate the connection
     * @return true, if the connection was successfully deauthenticated
     */
    public CompletableFuture<Boolean> logout() {
        return CompletableFuture.supplyAsync(() -> {
            this.sendPacket(new LogoutPacket());
            try {
                if(this.waitForResponse(LogoutResponsePacket.class).get().isSuccess()) {
                    this.username = null;
                    return true;
                } else {
                    return false;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Get all databases that exist on the server
     * @return the databases
     */
    public CompletableFuture<Database[]> getDatabases() {
        return CompletableFuture.supplyAsync(() -> {
            this.sendPacket(new ListDatabasesPacket());
            try {
                Collection<String> databaseNames = this.waitForResponse(ListDatabasesResponsePacket.class).get().getDatabases();
                Database[] databases = new Database[databaseNames.size()];
                int i = 0;
                for(String name : databaseNames) {
                    databases[i++] = this.getDatabase(name);
                }
                return databases;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Database[0];
            }
        });
    }

    /**
     * Get a database by it's name
     * @param name the database name
     * @return the database
     */
    public @NotNull Database getDatabase(String name) {
        return new Database(this, name);
    }

    /**
     * Get a database by it's name, or null if the database doesn't exist
     * @param name the database name
     * @return the database, or null if the database doesn't exist
     */
    public CompletableFuture<@Nullable Database> getDatabaseIfExists(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if(Arrays.asList(this.getDatabases().get()).contains(name)) {
                    return this.getDatabase(name);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private void read() {
        new Thread(() -> {
            ByteBuffer inputBuffer = ByteBuffer.allocate(65535);
            int size;
            try {
                size = this.socket.read(inputBuffer).get();
            } catch (InterruptedException | ExecutionException e) {
                this.read();
                return;
            }
            byte[] data = Arrays.copyOfRange(inputBuffer.array(), 0, size);

            for(PacketIn packet : NetworkManager.getInstance().deserializePacketsIn(data, this.encryptionSecret)) {
                if(this.waiting.containsKey(packet.getClass())) {
                    Iterator<CompletableFuture<? extends PacketIn>> futures = this.waiting.get(packet.getClass()).iterator();
                    while(futures.hasNext()) {
                        CompletableFuture future = futures.next();
                        if(future != null) {
                            future.complete(packet);
                            futures.remove();
                            break;
                        }
                    }
                }
            }

            this.read();
        }, "read()").start();
    }

    public void sendPacket(PacketOut packet) {
        if(this.socket == null || !this.socket.isOpen()) {
            return;
        }
        try {
            this.socket.write(ByteBuffer.wrap(NetworkManager.getInstance().serializePacketOut(packet, this.encryptionSecret))).get();
        } catch (Exception ignored) {}
    }

    public <T extends PacketIn> CompletableFuture<T> waitForResponse(Class<T> packetClass) {
        if(!this.waiting.containsKey(packetClass)) {
            this.waiting.put(packetClass, new LinkedList<>());
        }
        CompletableFuture<T> future = new CompletableFuture<>();
        this.waiting.get(packetClass).add(future);
        return future;
    }
}