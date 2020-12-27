package me.ste.stevesdbclient.entry.value;

import me.ste.stevesdbclient.table.TableColumn;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is intended to help you build a {@link String} -&gt; {@link EntryValue} map
 */
public class EntryValuesBuilder {
    private final Map<String, EntryValue> map = new HashMap<>();

    public EntryValuesBuilder value(String column, boolean value) {
        this.map.put(column, new EntryValue(value));
        return this;
    }

    public EntryValuesBuilder value(String column, int value) {
        this.map.put(column, new EntryValue(value));
        return this;
    }

    public EntryValuesBuilder value(String column, long value) {
        this.map.put(column, new EntryValue(value));
        return this;
    }

    public EntryValuesBuilder value(String column, double value) {
        this.map.put(column, new EntryValue(value));
        return this;
    }

    public EntryValuesBuilder value(String column, String value) {
        this.map.put(column, new EntryValue(value));
        return this;
    }

    public EntryValuesBuilder value(TableColumn column, boolean value) {
        return this.value(column.getName(), value);
    }

    public EntryValuesBuilder value(TableColumn column, int value) {
        return this.value(column.getName(), value);
    }

    public EntryValuesBuilder value(TableColumn column, long value) {
        return this.value(column.getName(), value);
    }

    public EntryValuesBuilder value(TableColumn column, double value) {
        return this.value(column.getName(), value);
    }

    public EntryValuesBuilder value(TableColumn column, String value) {
        return this.value(column.getName(), value);
    }

    public Map<String, EntryValue> build() {
        return this.map;
    }
}