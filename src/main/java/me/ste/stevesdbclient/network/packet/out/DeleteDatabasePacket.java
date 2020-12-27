package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(8)
public class DeleteDatabasePacket extends PacketOut {
    private final String database;

    public DeleteDatabasePacket(String database) {
        this.database = database;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
    }
}