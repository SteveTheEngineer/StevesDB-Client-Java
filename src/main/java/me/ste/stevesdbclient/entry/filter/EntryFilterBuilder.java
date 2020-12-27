package me.ste.stevesdbclient.entry.filter;

import me.ste.stevesdbclient.table.TableColumn;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is intended to help you build a {@link String} -&gt; {@link EntryFilter} map
 */
public class EntryFilterBuilder {
    private Map<String, EntryFilter> map = new HashMap<>();

    public EntryFilterBuilder filter(String column, ComparatorOperation operation, boolean value) {
        this.map.put(column, new EntryFilter(operation, String.valueOf(value)));
        return this;
    }

    public EntryFilterBuilder filter(String column, ComparatorOperation operation, int value) {
        this.map.put(column, new EntryFilter(operation, String.valueOf(value)));
        return this;
    }

    public EntryFilterBuilder filter(String column, ComparatorOperation operation, long value) {
        this.map.put(column, new EntryFilter(operation, String.valueOf(value)));
        return this;
    }

    public EntryFilterBuilder filter(String column, ComparatorOperation operation, double value) {
        this.map.put(column, new EntryFilter(operation, String.valueOf(value)));
        return this;
    }

    public EntryFilterBuilder filter(String column, ComparatorOperation operation, String value) {
        this.map.put(column, new EntryFilter(operation, value));
        return this;
    }

    public EntryFilterBuilder filter(TableColumn column, ComparatorOperation operation, boolean value) {
        return this.filter(column.getName(), operation, value);
    }

    public EntryFilterBuilder filter(TableColumn column, ComparatorOperation operation, int value) {
        return this.filter(column.getName(), operation, value);
    }

    public EntryFilterBuilder filter(TableColumn column, ComparatorOperation operation, long value) {
        return this.filter(column.getName(), operation, value);
    }

    public EntryFilterBuilder filter(TableColumn column, ComparatorOperation operation, double value) {
        return this.filter(column.getName(), operation, value);
    }

    public EntryFilterBuilder filter(TableColumn column, ComparatorOperation operation, String value) {
        return this.filter(column.getName(), operation, value);
    }

    public Map<String, EntryFilter> build() {
        return this.map;
    }
}