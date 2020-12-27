package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(7)
public class CreateDatabasePacket extends PacketOut {
    private final String name;

    public CreateDatabasePacket(String name) {
        this.name = name;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.name);
    }
}