package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(10)
public class CreateTablePacket extends PacketOut {
    private final String database;
    private final String name;

    public CreateTablePacket(String database, String name) {
        this.database = database;
        this.name = name;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.name);
    }
}