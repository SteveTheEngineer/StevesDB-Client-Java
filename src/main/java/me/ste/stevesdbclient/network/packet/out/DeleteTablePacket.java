package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(12)
public class DeleteTablePacket extends PacketOut {
    private final String database;
    private final String table;

    public DeleteTablePacket(String database, String table) {
        this.database = database;
        this.table = table;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);
    }
}