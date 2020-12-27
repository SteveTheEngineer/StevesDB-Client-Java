package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@PacketId(4)
public class ListDatabasesResponsePacket extends PacketIn {
    private boolean success;
    private Collection<String> databases = new HashSet<>();

    @Override
    public void deserialize(DataReader reader) {
        this.success = reader.readBoolean();

        this.databases.addAll(Arrays.asList(reader.readStringArray()));
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Collection<String> getDatabases() {
        return this.databases;
    }
}