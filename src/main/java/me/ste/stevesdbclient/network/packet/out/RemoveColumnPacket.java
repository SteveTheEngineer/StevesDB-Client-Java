package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(16)
public class RemoveColumnPacket extends PacketOut {
    private final String database;
    private final String table;
    private final String column;

    public RemoveColumnPacket(String database, String table, String column) {
        this.database = database;
        this.table = table;
        this.column = column;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);
        writer.writeString(this.column);
    }
}