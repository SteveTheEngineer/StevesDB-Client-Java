package me.ste.stevesdbclient.table;

import me.ste.stevesdbclient.StevesDBClient;
import me.ste.stevesdbclient.database.Database;
import me.ste.stevesdbclient.entry.filter.EntryFilter;
import me.ste.stevesdbclient.entry.value.EntryValueModifier;
import me.ste.stevesdbclient.entry.value.EntryValue;
import me.ste.stevesdbclient.entry.value.EntryValuesBuilder;
import me.ste.stevesdbclient.network.packet.in.*;
import me.ste.stevesdbclient.network.packet.out.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Table {
    private final StevesDBClient client;
    private final Database database;
    private String name;

    public Table(Database database, String name) {
        this.client = database.getClient();
        this.database = database;
        this.name = name;
    }

    /**
     * Get the parent database of the table
     * @return the parent database of the table
     */
    public Database getDatabase() {
        return this.database;
    }

    /**
     * Get the name of the table
     * @return the name of the table
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add a column to the table
     * @param type column type
     * @param name column name
     * @return the column if the column was successfully added, or null otherwise
     */
    public CompletableFuture<@Nullable TableColumn> addColumn(TableColumnType type, String name) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new AddColumnPacket(this.database.getName(), this.name, type, name));
            try {
                if(this.client.waitForResponse(AddColumnResponsePacket.class).get().isSuccess()) {
                    return new TableColumn(type, name);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Add an entry to the table
     * @see EntryValuesBuilder
     * @param values table values
     * @return true, if the entry was successfully added
     */
    public CompletableFuture<Boolean> addEntry(Map<String, EntryValue> values) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> map = new HashMap<>();
            for(String column : values.keySet()) {
                map.put(column, values.get(column).asString());
            }
            this.client.sendPacket(new AddEntryPacket(this.database.getName(), this.name, map));
            try {
                return this.client.waitForResponse(AddEntryResponsePacket.class).get().isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Create the table
     * @return true, if the table was successfully created
     */
    public CompletableFuture<Boolean> create() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new CreateTablePacket(this.database.getName(), this.name));
            try {
                return this.client.waitForResponse(CreateTableResponsePacket.class).get().isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Delete the table
     * @return true, if the table was successfully deleted
     */
    public CompletableFuture<Boolean> delete() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new DeleteTablePacket(this.database.getName(), this.name));
            try {
                return this.client.waitForResponse(DeleteTableResponsePacket.class).get().isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Get the entries in the table
     * @param filter the filter to apply
     * @param start start index
     * @param end end index
     * @return the entries
     */
    public CompletableFuture<Map<String, EntryValue>[]> getEntries(Map<String, EntryFilter> filter, int start, int end) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new ListEntriesPacket(this.database.getName(), this.name, start, end, filter));
            try {
                ListEntriesResponsePacket response = this.client.waitForResponse(ListEntriesResponsePacket.class).get();
                if(response.isSuccess()) {
                    TableColumn[] columns = this.getColumns().get();
                    Map<Integer, Map<Integer, String>> entries = response.getEntries();
                    Map<String, EntryValue>[] columnKeyedEntries = new Map[entries.size()];
                    for(Map.Entry<Integer, Map<Integer, String>> entry : entries.entrySet()) {
                        Map<String, EntryValue> columnKeyedEntry = new HashMap<>();
                        int i = 0;
                        for(TableColumn column : columns) {
                            String value = entry.getValue().get(i);
                            if(column.getType() == TableColumnType.BOOLEAN) {
                                columnKeyedEntry.put(column.getName(), new EntryValue(value.equals("true")));
                            } else if(column.getType() == TableColumnType.INTEGER) {
                                columnKeyedEntry.put(column.getName(), new EntryValue(Integer.parseInt(value)));
                            } else if(column.getType() == TableColumnType.LONG) {
                                columnKeyedEntry.put(column.getName(), new EntryValue(Long.parseLong(value)));
                            } else if(column.getType() == TableColumnType.DOUBLE) {
                                columnKeyedEntry.put(column.getName(), new EntryValue(Double.parseDouble(value)));
                            } else {
                                columnKeyedEntry.put(column.getName(), new EntryValue(value));
                            }
                            i++;
                        }
                        columnKeyedEntries[entry.getKey()] = columnKeyedEntry;
                    }
                    return columnKeyedEntries;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return new Map[0];
        });
    }

    /**
     * Get the entries in the table
     * @param filter the filter to apply
     * @param start start index
     * @return the entries
     */
    public CompletableFuture<Map<String, EntryValue>[]> getEntries(Map<String, EntryFilter> filter, int start) {
        return this.getEntries(filter, start, Integer.MAX_VALUE);
    }

    /**
     * Get all entries in the table
     * @param filter the filter to apply
     * @return the entries
     */
    public CompletableFuture<Map<String, EntryValue>[]> getEntries(Map<String, EntryFilter> filter) {
        return this.getEntries(filter, Integer.MIN_VALUE);
    }

    /**
     * Get all entries in the table
     * @return the entries
     */
    public CompletableFuture<Map<String, EntryValue>[]> getEntries() {
        return this.getEntries(new HashMap<>());
    }

    /**
     * Get the entry in the table by it's index
     * @param filter the filter to apply
     * @param index entry index
     * @return the entry
     */
    public CompletableFuture<Map<String, EntryValue>> getEntry(Map<String, EntryFilter> filter, int index) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, EntryValue>[] entries = this.getEntries(filter, index, index).get();
                if(entries.length > 0) {
                    return entries[0];
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Get the first entry in the table
     * @param filter the filter to apply
     * @return the entry
     */
    public CompletableFuture<Map<String, EntryValue>> getEntry(Map<String, EntryFilter> filter) {
        return this.getEntry(filter, 0);
    }

    /**
     * Get the first entry in the table
     * @return the entry
     */
    public CompletableFuture<Map<String, EntryValue>> getEntry() {
        return this.getEntry(new HashMap<>());
    }

    /**
     * Get whether the table exists in the database
     * @return true, if the table exsits
     */
    public CompletableFuture<Boolean> exists() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new ListTablesPacket(this.database.getName()));
            try {
                return this.client.waitForResponse(ListTablesResponsePacket.class).get().getTables().contains(this.name);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Get the column by it's name
     * @param name column name
     * @return the column, or null if it doesn't exist
     */
    public CompletableFuture<@Nullable TableColumn> getColumn(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TableColumn[] tableColumns = this.getColumns().get();
                for(TableColumn column : tableColumns) {
                    if(column.getName().equals(name)) {
                        return column;
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Remove the column by it's name
     * @param name column name
     * @return true, if the column was removed
     */
    public CompletableFuture<Boolean> removeColumn(String name) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new RemoveColumnPacket(this.database.getName(), this.name, name));
            try {
                return this.client.waitForResponse(RemoveColumnResponsePacket.class).get().isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    /**
     * Remove the column from the table
     * @param column the column
     * @return true, if the column was removed
     */
    public CompletableFuture<Boolean> removeColumn(TableColumn column) {
        return this.removeColumn(column.getName());
    }

    /**
     * Rename the column by it's name
     * @param name the column name
     * @param newName the new name
     * @return true, if the column was renamed
     */
    public CompletableFuture<Boolean> renameColumn(String name, String newName) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new RenameColumnPacket(this.database.getName(), this.name, name, newName));
            try {
                return this.client.waitForResponse(RenameColumnResponsePacket.class).get().isSuccess();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    /**
     * Rename the column
     * @param column the column
     * @param newName the new name
     * @return true, if the column was renamed
     */
    public CompletableFuture<Boolean> renameColumn(TableColumn column, String newName) {
        return this.renameColumn(column.getName(), newName);
    }

    /**
     * Get the columns of the table
     * @return the columns of the table
     */
    public CompletableFuture<TableColumn[]> getColumns() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new ListColumnsPacket(this.database.getName(), this.name));
            try {
                ListColumnsResponsePacket response = this.client.waitForResponse(ListColumnsResponsePacket.class).get();
                if(response.isSuccess()) {
                    TableColumn[] columns = new TableColumn[response.getColumns().size()];
                    for(Map.Entry<Integer, TableColumn> entry : response.getColumns().entrySet()) {
                        columns[entry.getKey()] = entry.getValue();
                    }
                    return columns;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return new TableColumn[0];
        });
    }

    /**
     * Get the total amount of entries in the table
     * @return the total amount of entries in the table
     */
    public CompletableFuture<Integer> getTotalEntries() {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new ListEntriesPacket(this.database.getName(), this.name, 0, 0, new HashMap<>()));
            try {
                return this.client.waitForResponse(ListEntriesResponsePacket.class).get().getTotal();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    /**
     * Modify the entries in the table
     * @param filter the filter to apply
     * @param modifier the modifications to do
     * @param start start index
     * @param end end index
     * @return the amount of entries modified
     */
    public CompletableFuture<Integer> modifyEntries(Map<String, EntryFilter> filter, Map<String, EntryValueModifier> modifier, int start, int end) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new ModifyEntryPacket(this.database.getName(), this.name, filter, start, end, modifier));
            try {
                return this.client.waitForResponse(ModifyEntryResponsePacket.class).get().getModified();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    /**
     * Modify the entries in the table
     * @param filter the filter to apply
     * @param modifier the modifications to do
     * @param start start index
     * @return the amount of entries modified
     */
    public CompletableFuture<Integer> modifyEntries(Map<String, EntryFilter> filter, Map<String, EntryValueModifier> modifier, int start) {
        return this.modifyEntries(filter, modifier, start, Integer.MAX_VALUE);
    }

    /**
     * Modify the entries in the table
     * @param filter the filter to apply
     * @param modifier the modifications to do
     * @return the amount of entries modified
     */
    public CompletableFuture<Integer> modifyEntries(Map<String, EntryFilter> filter, Map<String, EntryValueModifier> modifier) {
        return this.modifyEntries(filter, modifier, Integer.MIN_VALUE);
    }

    /**
     * Remove the entries in the table
     * @param filter the filter to apply
     * @param start start index
     * @param end end index
     * @return the amount of entries removed
     */
    public CompletableFuture<Integer> removeEntries(Map<String, EntryFilter> filter, int start, int end) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new RemoveEntryPacket(this.database.getName(), this.name, filter, start, end));
            try {
                return this.client.waitForResponse(RemoveEntryResponsePacket.class).get().getRemoved();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    /**
     * Remove the entries in the table
     * @param filter the filter to apply
     * @param start start index
     * @return the amount of entries removed
     */
    public CompletableFuture<Integer> removeEntries(Map<String, EntryFilter> filter, int start) {
        return this.removeEntries(filter, start, Integer.MAX_VALUE);
    }

    /**
     * Remove the entries in the table
     * @param filter the filter to apply
     * @return the amount of entries removed
     */
    public CompletableFuture<Integer> removeEntries(Map<String, EntryFilter> filter) {
        return this.removeEntries(filter, Integer.MIN_VALUE);
    }

    /**
     * Rename the table
     * @param newName the new name for the table
     * @return true, if the table was renamed
     */
    public CompletableFuture<Boolean> rename(String newName) {
        return CompletableFuture.supplyAsync(() -> {
            this.client.sendPacket(new RenameTablePacket(this.database.getName(), this.name, newName));
            try {
                if(this.client.waitForResponse(RenameTableResponsePacket.class).get().isSuccess()) {
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