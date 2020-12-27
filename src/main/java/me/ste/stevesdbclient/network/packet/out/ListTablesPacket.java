package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(6)
public class ListTablesPacket extends PacketOut {
    private final String database;

    public ListTablesPacket(String database) {
        this.database = database;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
    }
}