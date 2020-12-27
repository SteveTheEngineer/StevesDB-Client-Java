package me.ste.stevesdbclient.entry.value;

import me.ste.stevesdbclient.table.TableColumnType;
import me.ste.stevesdbclient.util.MathUtil;

public class EntryValue {
    private final TableColumnType type;
    private final Object value;

    public EntryValue(boolean value) {
        this.type = TableColumnType.BOOLEAN;
        this.value = value;
    }
    public EntryValue(int value) {
        this.type = TableColumnType.INTEGER;
        this.value = value;
    }
    public EntryValue(long value) {
        this.type = TableColumnType.LONG;
        this.value = value;
    }
    public EntryValue(double value) {
        this.type = TableColumnType.DOUBLE;
        this.value = value;
    }
    public EntryValue(String value) {
        this.type = TableColumnType.STRING;
        this.value = value;
    }

    public TableColumnType getType() {
        return this.type;
    }

    public Object get() {
        return this.value;
    }

    public boolean asBoolean() {
        if(this.type == TableColumnType.BOOLEAN) {
            return (boolean) this.value;
        } else if(this.type == TableColumnType.INTEGER) {
            return (int) this.value != 0;
        } else if(this.type == TableColumnType.LONG) {
            return (long) this.value != 0L;
        } else if(this.type == TableColumnType.DOUBLE) {
            return (double) this.value != 0D;
        } else if(this.type == TableColumnType.STRING) {
            return this.value != null && ((String) this.value).length() > 0;
        } else {
            return false;
        }
    }

    public int asInteger() {
        if(this.type == TableColumnType.BOOLEAN) {
            return (boolean) this.value ? 1 : 0;
        } else if(this.type == TableColumnType.INTEGER) {
            return (int) this.value;
        } else if(this.type == TableColumnType.LONG) {
            return (int) MathUtil.clamp((long) this.value, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else if(this.type == TableColumnType.DOUBLE) {
            return (int) MathUtil.clamp(Math.round((double) this.value), Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else if(this.type == TableColumnType.STRING) {
            if(this.value != null) {
                try {
                    return Integer.parseInt((String) this.value);
                } catch(NumberFormatException ignored) {}
            }
        }
        return 0;
    }

    public long asLong() {
        if(this.type == TableColumnType.BOOLEAN) {
            return (boolean) this.value ? 1L : 0L;
        } else if(this.type == TableColumnType.INTEGER) {
            return (long) this.value;
        } else if(this.type == TableColumnType.LONG) {
            return (long) this.value;
        } else if(this.type == TableColumnType.DOUBLE) {
            return (long) MathUtil.clamp(Math.round((double) this.value), Long.MIN_VALUE, Long.MAX_VALUE);
        } else if(this.type == TableColumnType.STRING) {
            if(this.value != null) {
                try {
                    return Long.parseLong((String) this.value);
                } catch(NumberFormatException ignored) {}
            }
        }
        return 0L;
    }

    public double asDouble() {
        if(this.type == TableColumnType.BOOLEAN) {
            return (boolean) this.value ? 1D : 0D;
        } else if(this.type == TableColumnType.INTEGER) {
            return (double) this.value;
        } else if(this.type == TableColumnType.LONG) {
            return (double) this.value;
        } else if(this.type == TableColumnType.DOUBLE) {
            return (double) this.value;
        } else if(this.type == TableColumnType.STRING) {
            if(this.value != null) {
                try {
                    return Double.parseDouble((String) this.value);
                } catch(NumberFormatException ignored) {}
            }
        }
        return 0D;
    }

    public String asString() {
        if(this.type == TableColumnType.BOOLEAN) {
            return String.valueOf((boolean) this.value);
        } else if(this.type == TableColumnType.INTEGER) {
            return String.valueOf((int) this.value);
        } else if(this.type == TableColumnType.LONG) {
            return String.valueOf((long) this.value);
        } else if(this.type == TableColumnType.DOUBLE) {
            return String.valueOf((double) this.value);
        } else if(this.type == TableColumnType.STRING) {
            return (String) this.value;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.type.name() + ":" + this.value;
    }
}