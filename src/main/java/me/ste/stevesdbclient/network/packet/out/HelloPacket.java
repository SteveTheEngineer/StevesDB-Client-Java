package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(0)
public class HelloPacket extends PacketOut {
    private final int protocolVersion;
    private final String clientVersion;

    public HelloPacket(int protocolVersion, String clientVersion) {
        this.protocolVersion = protocolVersion;
        this.clientVersion = clientVersion;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeInt(this.protocolVersion);
        writer.writeString(this.clientVersion);
    }
}