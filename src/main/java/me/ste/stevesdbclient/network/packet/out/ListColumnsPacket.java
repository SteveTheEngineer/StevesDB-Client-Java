package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(13)
public class ListColumnsPacket extends PacketOut {
    private final String database;
    private final String table;

    public ListColumnsPacket(String database, String table) {
        this.database = database;
        this.table = table;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);
    }
}