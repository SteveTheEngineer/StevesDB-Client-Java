package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(11)
public class RenameTablePacket extends PacketOut {
    private final String database;
    private final String table;
    private final String newName;

    public RenameTablePacket(String database, String table, String newName) {
        this.database = database;
        this.table = table;
        this.newName = newName;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);
        writer.writeString(this.newName);
    }
}