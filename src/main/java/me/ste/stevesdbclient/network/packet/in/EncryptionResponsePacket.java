package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@PacketId(1)
public class EncryptionResponsePacket extends PacketIn {
    private PublicKey publicKey;

    @Override
    public void deserialize(DataReader reader) {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            this.publicKey = factory.generatePublic(new X509EncodedKeySpec(reader.readByteArray()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {}
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}