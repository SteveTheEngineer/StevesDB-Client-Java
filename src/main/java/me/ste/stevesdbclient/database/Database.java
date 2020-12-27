package me.ste.stevesdbclient.database;

import me.ste.stevesdbclient.StevesDBClient;
import me.ste.stevesdbclient.network.packet.in.CreateDatabaseResponsePacket;
import me.ste.stevesdbclient.network.packet.in.DeleteDatabaseResponsePacket;
import me.ste.stevesdbclient.network.packet.in.ListTablesResponsePacket;
import me.ste.stevesdbclient.network.packet.in.RenameDatabaseResponsePacket;
import me.ste.stevesdbclient.network.packet.out.*;
import me.ste.stevesdbclient.table.Table;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Database {
    private final StevesDBClient client;
    private String name;

    public Database(StevesDBClient client, String name) {
        this.client = client;
        this.name = name;
    }

    /**
     * Get the parent client of the database
     * @return the client
     */
    public StevesDBClient getClient() {
        return this.client;
    }

    /**
     * Create the database
     * @return true, if the database was created
     */
    public CompletableFuture<Boolean> create() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new CreateDatabasePacket(this.name));
            try {
                return this.client.waitForResponse(CreateDatabaseResponsePacket.class).get().isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Delete the database
     * @return true, if the database was deleted
     */
    public CompletableFuture<Boolean> delete() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new DeleteDatabasePacket(this.name));
            try {
                return this.client.waitForResponse(DeleteDatabaseResponsePacket.class).get().isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Get whether the database exists
     * @return true, if the database exists
     */
    public CompletableFuture<Boolean> exists() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Arrays.asList(this.client.getDatabases().get()).contains(this.name);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Get the name of the database
     * @return the name of the database
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the table by it's name
     * @param name table name
     * @return the table
     */
    public Table getTable(String name) {
        return new Table(this, name);
    }

    /**
     * Get the table by it's name, or null if the table doesn't exist
     * @param name table name
     * @return the table
     */
    public CompletableFuture<@Nullable Table> getTableIfExists(String name) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new ListTablesPacket(this.name));
            try {
                if(this.client.waitForResponse(ListTablesResponsePacket.class).get().getTables().contains(name)) {
                    return this.getTable(name);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Get the tables in the database
     * @return the tables
     */
    public CompletableFuture<Table[]> getTables() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new ListTablesPacket(this.name));
            try {
                Set<Table> tables = new HashSet<>();
                for(String name : this.client.waitForResponse(ListTablesResponsePacket.class).get().getTables()) {
                    tables.add(this.getTable(name));
                }
                return tables.toArray(new Table[0]);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Table[0];
            }
        });
    }

    /**
     * Rename the database
     * @param newName the new name
     * @return true, if the database was renamed
     */
    public CompletableFuture<Boolean> rename(String newName) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new RenameDatabasePacket(this.name, newName));
            try {
                RenameDatabaseResponsePacket response = this.client.waitForResponse(RenameDatabaseResponsePacket.class).get();
                if(response.isSuccess()) {
                    this.name = newName;
                    return true;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        });
    }
}