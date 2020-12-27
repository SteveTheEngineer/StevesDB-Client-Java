package me.ste.stevesdbclient.entry.value;

import me.ste.stevesdbclient.table.TableColumn;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is intended to help you build a {@link String} -> {@link EntryValueModifier} map
 */
public class EntryModifierBuilder {
    private Map<String, EntryValueModifier> map = new HashMap<>();

    public EntryModifierBuilder modify(String column, EntryValueOperation operation, boolean value) {
        this.map.put(column, new EntryValueModifier(operation, String.valueOf(value)));
        return this;
    }

    public EntryModifierBuilder modify(String column, EntryValueOperation operation, int value) {
        this.map.put(column, new EntryValueModifier(operation ,String.valueOf(value)));
        return this;
    }

    public EntryModifierBuilder modify(String column, EntryValueOperation operation, long value) {
        this.map.put(column, new EntryValueModifier(operation, String.valueOf(value)));
        return this;
    }

    public EntryModifierBuilder modify(String column, EntryValueOperation operation, double value) {
        this.map.put(column, new EntryValueModifier(operation, String.valueOf(value)));
        return this;
    }

    public EntryModifierBuilder modify(String column, EntryValueOperation operation, String value) {
        this.map.put(column, new EntryValueModifier(operation, value));
        return this;
    }

    public EntryModifierBuilder modify(TableColumn column, EntryValueOperation operation, boolean value) {
        return this.modify(column.getName(), operation, value);
    }

    public EntryModifierBuilder modify(TableColumn column, EntryValueOperation operation, int value) {
        return this.modify(column.getName(), operation, value);
    }

    public EntryModifierBuilder modify(TableColumn column, EntryValueOperation operation, long value) {
        return this.modify(column.getName(), operation, value);
    }

    public EntryModifierBuilder modify(TableColumn column, EntryValueOperation operation, double value) {
        return this.modify(column.getName(), operation, value);
    }

    public EntryModifierBuilder modify(TableColumn column, EntryValueOperation operation, String value) {
        return this.modify(column.getName(), operation, value);
    }

    public Map<String, EntryValueModifier> build() {
        return this.map;
    }
}