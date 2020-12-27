package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(1)
public class EncryptionPacket extends PacketOut {
    public EncryptionPacket() {

    }

    @Override
    public void serialize(DataWriter writer) {

    }
}