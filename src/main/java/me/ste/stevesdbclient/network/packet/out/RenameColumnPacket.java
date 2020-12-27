package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(15)
public class RenameColumnPacket extends PacketOut {
    private final String database;
    private final String table;
    private final String column;
    private final String newName;

    public RenameColumnPacket(String database, String table, String column, String newName) {
        this.database = database;
        this.table = table;
        this.column = column;
        this.newName = newName;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);
        writer.writeString(this.column);
        writer.writeString(this.newName);
    }
}