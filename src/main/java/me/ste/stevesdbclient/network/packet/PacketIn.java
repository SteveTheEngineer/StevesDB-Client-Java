package me.ste.stevesdbclient.network.packet;

import me.ste.stevesdbclient.util.DataReader;

public abstract class PacketIn {
    public abstract void deserialize(DataReader reader);
}