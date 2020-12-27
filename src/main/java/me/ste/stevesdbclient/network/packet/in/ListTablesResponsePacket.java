package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@PacketId(5)
public class ListTablesResponsePacket extends PacketIn {
    private boolean success;
    private Collection<String> tables = new HashSet<>();

    @Override
    public void deserialize(DataReader reader) {
        this.success = reader.readBoolean();
        this.tables.addAll(Arrays.asList(reader.readStringArray()));
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Collection<String> getTables() {
        return this.tables;
    }
}