package me.ste.stevesdbclient.table;

public class TableColumn {
    private final TableColumnType type;
    private String name;

    public TableColumn(TableColumnType type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Get the type of the table column
     * @return the type of the table column
     */
    public TableColumnType getType() {
        return this.type;
    }

    /**
     * Get the name of the table column
     * @return the name of the table column
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.type.name() + ":" + this.name;
    }
}