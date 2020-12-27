package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.util.DataReader;

import java.util.HashMap;
import java.util.Map;

@PacketId(16)
public class ListEntriesResponsePacket extends PacketIn {
    private boolean success;
    private int total;
    private Map<Integer, Map<Integer, String>> entries = new HashMap<>();

    @Override
    public void deserialize(DataReader reader) {
        this.success = reader.readBoolean();
        this.total = reader.readInt();

        int size = reader.readInt();
        for(int i = 0; i < size; i++) {
            Map<Integer, String> entry = new HashMap<>();
            int size2 = reader.readInt();
            for(int i2 = 0; i2 < size2; i2++) {
                entry.put(i2, reader.readString());
            }
            this.entries.put(i, entry);
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getTotal() {
        return this.total;
    }

    public Map<Integer, Map<Integer, String>> getEntries() {
        return this.entries;
    }
}