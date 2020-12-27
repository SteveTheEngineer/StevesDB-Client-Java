package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(2)
public class EncryptionEnabledPacket extends PacketOut {
    private final byte[] encryptionSecret;

    public EncryptionEnabledPacket(byte[] encryptionSecret) {
        this.encryptionSecret = encryptionSecret;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeByteArray(this.encryptionSecret);
    }
}