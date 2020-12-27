package me.ste.stevesdbclient.entry.filter;

public class EntryFilter {
    private final ComparatorOperation operation;
    private final String comparedValue;

    public EntryFilter(ComparatorOperation operation, String comparedValue) {
        this.operation = operation;
        this.comparedValue = comparedValue;
    }

    public ComparatorOperation getOperation() {
        return this.operation;
    }

    public String getComparedValue() {
        return this.comparedValue;
    }

    @Override
    public String toString() {
        return this.operation.name() + ":" + this.comparedValue;
    }
}