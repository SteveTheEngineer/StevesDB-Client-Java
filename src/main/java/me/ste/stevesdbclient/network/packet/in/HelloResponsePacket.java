package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

@PacketId(0)
public class HelloResponsePacket extends PacketIn {
    private int protocolVersion;
    private String serverVersion;

    @Override
    public void deserialize(DataReader reader) {
        this.protocolVersion = reader.readInt();
        this.serverVersion = reader.readString();
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public String getServerVersion() {
        return this.serverVersion;
    }
}