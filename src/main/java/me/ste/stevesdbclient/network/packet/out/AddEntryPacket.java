package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

import java.util.Map;

@PacketId(18)
public class AddEntryPacket extends PacketOut {
    private final String database;
    private final String table;
    private final Map<String, String> values;

    public AddEntryPacket(String database, String table, Map<String, String> values) {
        this.database = database;
        this.table = table;
        this.values = values;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);

        writer.writeInt(this.values.size());
        for(Map.Entry<String, String> entry : this.values.entrySet()) {
            writer.writeString(entry.getKey());
            writer.writeString(entry.getValue());
        }
    }
}