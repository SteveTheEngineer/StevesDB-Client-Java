package me.ste.stevesdbclient.network.packet.in;

import me.ste.stevesdbclient.network.packet.PacketId;
import me.ste.stevesdbclient.network.packet.PacketIn;
import me.ste.stevesdbclient.table.TableColumn;
import me.ste.stevesdbclient.table.TableColumnType;
import me.ste.stevesdbclient.util.DataReader;

import java.util.HashMap;
import java.util.Map;

@PacketId(12)
public class ListColumnsResponsePacket extends PacketIn {
    private boolean success;
    private final Map<Integer, TableColumn> columns = new HashMap<>();

    @Override
    public void deserialize(DataReader reader) {
        this.success = reader.readBoolean();

        int size = reader.readInt();
        for(int i = 0; i < size; i++) {
            this.columns.put(i, new TableColumn(TableColumnType.values()[reader.readUnsignedByte()], reader.readString()));
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Map<Integer, TableColumn> getColumns() {
        return this.columns;
    }
}