package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.entry.filter.EntryFilter;
import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

import java.util.Map;

@PacketId(20)
public class RemoveEntryPacket extends PacketOut {
    private final String database;
    private final String table;
    private final Map<String, EntryFilter> filters;
    private final int startIndex;
    private final int endIndex;

    public RemoveEntryPacket(String database, String table, Map<String, EntryFilter> filters, int startIndex, int endIndex) {
        this.database = database;
        this.table = table;
        this.filters = filters;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);

        writer.writeInt(this.filters.size());
        for(Map.Entry<String, EntryFilter> entry : this.filters.entrySet()) {
            writer.writeString(entry.getKey());
            writer.writeUnsignedByte(entry.getValue().getOperation().ordinal());
            writer.writeString(entry.getValue().getComparedValue());
        }

        writer.writeInt(this.startIndex);
        writer.writeInt(this.endIndex);
    }
}