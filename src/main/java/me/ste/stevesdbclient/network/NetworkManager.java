package me.ste.stevesdbclient.network;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataReader;
import me.ste.stevesdbclient.util.DataWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkManager {
    private static final NetworkManager instance = new NetworkManager();

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Map<Integer, Class<? extends PacketIn>> packetInMap = new HashMap<>();
    private final Map<Class<? extends PacketOut>, Integer> packetOutMap = new HashMap<>();

    private NetworkManager() {}

    public static NetworkManager getInstance() {
        return NetworkManager.instance;
    }

    public void registerPacketIn(Class<? extends PacketIn> packetIn) {
        if(packetIn.isAnnotationPresent(PacketId.class)) {
            this.packetInMap.put(packetIn.getAnnotation(PacketId.class).value(), packetIn);
        } else {
            this.logger.log(Level.SEVERE, String.format("Unable to register input packet %s as it is not annotated with PacketId", packetIn.getName()));
        }
    }
    @SafeVarargs
    public final void registerPacketsIn(Class<? extends PacketIn>... packetsIn) {
        for(Class<? extends PacketIn> packetIn : packetsIn) {
            this.registerPacketIn(packetIn);
        }
    }
    public void registerPacketOut(Class<? extends PacketOut> packetOut) {
        if(packetOut.isAnnotationPresent(PacketId.class)) {
            this.packetOutMap.put(packetOut, packetOut.getAnnotation(PacketId.class).value());
        } else {
            this.logger.log(Level.SEVERE, String.format("Unable to register output packet %s as it is not annotated with PacketId", packetOut.getName()));
        }
    }
    @SafeVarargs
    public final void registerPacketsOut(Class<? extends PacketOut>... packetsOut) {
        for(Class<? extends PacketOut> packetOut : packetsOut) {
            this.registerPacketOut(packetOut);
        }
    }

    public byte[] serializePacketOut(PacketOut packetOut, byte[] encryptionSecret) {
        if(this.packetOutMap.containsKey(packetOut.getClass())) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            try {
                dataOutputStream.writeInt(this.packetOutMap.get(packetOut.getClass()));
                packetOut.serialize(new DataWriter(dataOutputStream));
                byte[] bytes = outputStream.toByteArray();
                if(encryptionSecret != null) {
                    Cipher c = Cipher.getInstance("AES/CFB8/NoPadding");
                    c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionSecret, "AES"), new IvParameterSpec(encryptionSecret));
                    bytes = c.doFinal(bytes);
                }
                outputStream.reset();
                dataOutputStream.writeInt(bytes.length);
                dataOutputStream.write(bytes);
            } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
                e.printStackTrace();
                return null;
            }
            return outputStream.toByteArray();
        } else {
            return null;
        }
    }

    public Collection<PacketIn> deserializePacketsIn(byte[] bytes, byte[] encryptionSecret) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        try {
            Collection<PacketIn> packets = new HashSet<>();
            while(dataInputStream.available() > 0) {
                try {
                    int length = dataInputStream.readInt();
                    byte[] data = new byte[length];
                    for(int i = 0; i < length; i++) {
                        data[i] = dataInputStream.readByte();
                    }
                    if(encryptionSecret != null) {
                        Cipher c = Cipher.getInstance("AES/CFB8/NoPadding");
                        c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encryptionSecret, "AES"), new IvParameterSpec(encryptionSecret));
                        data = c.doFinal(data);
                    }
                    ByteArrayInputStream packetInputStream = new ByteArrayInputStream(data);
                    DataInputStream packetDataInputStream = new DataInputStream(packetInputStream);
                    int id = packetDataInputStream.readInt();
                    if(this.packetInMap.containsKey(id)) {
                        PacketIn packetIn = this.packetInMap.get(id).getConstructor().newInstance();
                        packetIn.deserialize(new DataReader(packetDataInputStream));
                        packets.add(packetIn);
                    }
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | OutOfMemoryError ignored) {}
            }
            return packets;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}