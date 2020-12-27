package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.entry.filter.EntryFilter;
import me.ste.stevesdbclient.util.DataWriter;

import java.util.Map;

@PacketId(17)
public class ListEntriesPacket extends PacketOut {
    private final String database;
    private final String table;
    private final int start;
    private final int end;
    private final Map<String, EntryFilter> filters;

    public ListEntriesPacket(String database, String table, int start, int end, Map<String, EntryFilter> filters) {
        this.database = database;
        this.table = table;
        this.start = start;
        this.end = end;
        this.filters = filters;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);
        writer.writeInt(this.start);
        writer.writeInt(this.end);

        writer.writeInt(this.filters.size());
        for(Map.Entry<String, EntryFilter> entry : this.filters.entrySet()) {
            writer.writeString(entry.getKey());
            writer.writeUnsignedByte(entry.getValue().getOperation().ordinal());
            writer.writeString(entry.getValue().getComparedValue());
        }
    }
}