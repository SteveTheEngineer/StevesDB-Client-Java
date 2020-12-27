package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(3)
public class LoginPacket extends PacketOut {
    private final String username;
    private final String password;

    public LoginPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.username);
        writer.writeString(this.password);
    }
}