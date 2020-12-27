package me.ste.stevesdbclient.network.packet;

import me.ste.stevesdbclient.util.DataWriter;

public abstract class PacketOut {
    public abstract void serialize(DataWriter writer);
}