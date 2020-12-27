package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

@PacketId(19)
public class RemoveEntryResponsePacket extends PacketIn {
    private boolean success;
    private int removed;

    @Override
    public void deserialize(DataReader reader) {
        this.success = reader.readBoolean();
        this.removed = reader.readInt();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getRemoved() {
        return this.removed;
    }
}