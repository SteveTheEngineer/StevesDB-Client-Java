package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(9)
public class RenameDatabasePacket extends PacketOut {
    private final String database;
    private final String newName;

    public RenameDatabasePacket(String database, String newName) {
        this.database = database;
        this.newName = newName;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.newName);
    }
}