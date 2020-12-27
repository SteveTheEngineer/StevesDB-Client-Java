package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

@PacketId(18)
public class ModifyEntryResponsePacket extends PacketIn {
    private boolean success;
    private int modified;

    @Override
    public void deserialize(DataReader reader) {
        this.success = reader.readBoolean();
        this.modified = reader.readInt();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getModified() {
        return this.modified;
    }
}