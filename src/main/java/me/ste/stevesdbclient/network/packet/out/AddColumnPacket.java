package me.ste.stevesdbclient.network.packet.out;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketOut;
import me.ste.stevesdbclient.table.TableColumnType;
import me.ste.stevesdbclient.util.DataWriter;

@PacketId(14)
public class AddColumnPacket extends PacketOut {
    private final String database;
    private final String table;
    private final TableColumnType type;
    private final String name;

    public AddColumnPacket(String database, String table, TableColumnType type, String name) {
        this.database = database;
        this.table = table;
        this.type = type;
        this.name = name;
    }

    @Override
    public void serialize(DataWriter writer) {
        writer.writeString(this.database);
        writer.writeString(this.table);
        writer.writeUnsignedByte(this.type.ordinal());
        writer.writeString(this.name);
    }
}