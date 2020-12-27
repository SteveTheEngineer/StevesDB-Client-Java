package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(5)
public class ListDatabasesPacket extends PacketOut {
    public ListDatabasesPacket() {

    }

    @Override
    public void serialize(DataWriter writer) {

    }
}