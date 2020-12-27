package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

@PacketId(6)
public class CreateDatabaseResponsePacket extends PacketIn {
    private boolean success;

    @Override
    public void deserialize(DataReader reader) {
        this.success = reader.readBoolean();
    }

    public boolean isSuccess() {
        return this.success;
    }
}