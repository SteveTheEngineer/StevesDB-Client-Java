package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(4)
public class LogoutPacket extends PacketOut {
    public LogoutPacket() {

    }

    @Override
    public void serialize(DataWriter writer) {

    }
}