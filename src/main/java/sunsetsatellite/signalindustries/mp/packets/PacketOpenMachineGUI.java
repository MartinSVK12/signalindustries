package sunsetsatellite.signalindustries.mp.packets;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import sunsetsatellite.signalindustries.interfaces.mixins.INetClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOpenMachineGUI extends Packet {

    public int windowId;
    public String windowTitle;
    public int slotsCount;
    public int blockX;
    public int blockY;
    public int blockZ;
    public ItemStack stack;

    public PacketOpenMachineGUI(int windowId, String windowTitle, int slotsCount, int x, int y, int z) {
        this.windowId = windowId;
        this.windowTitle = windowTitle;
        this.slotsCount = slotsCount;
        this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
    }

    public PacketOpenMachineGUI(int windowId, String windowTitle, int slotsCount, ItemStack stack) {
        this.windowId = windowId;
        this.windowTitle = windowTitle;
        this.slotsCount = slotsCount;
        this.stack = stack;
    }

    public void processPacket(NetHandler nethandler) {
        ((INetClientHandler)nethandler).handleOpenMachineGUI(this);
    }

    public void readPacketData(DataInputStream datainputstream) throws IOException {
        this.windowId = datainputstream.readByte();
        this.windowTitle = datainputstream.readUTF();
        this.slotsCount = datainputstream.readByte();
        this.blockX = datainputstream.readInt();
        this.blockY = datainputstream.readInt();
        this.blockZ = datainputstream.readInt();
        short id = datainputstream.readShort();
        if (id >= 0) {
            byte amount = datainputstream.readByte();
            short metadata = datainputstream.readShort();
            CompoundTag tag = readCompressedCompoundTag(datainputstream);
            this.stack = new ItemStack(id, amount, metadata, tag);
        } else {
            this.stack = null;
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeByte(this.windowId);
        dataoutputstream.writeUTF(this.windowTitle);
        dataoutputstream.writeByte(this.slotsCount);
        dataoutputstream.writeInt(this.blockX);
        dataoutputstream.writeInt(this.blockY);
        dataoutputstream.writeInt(this.blockZ);
        if (this.stack == null) {
            dataoutputstream.writeShort(-1);
        } else {
            dataoutputstream.writeShort(this.stack.itemID);
            dataoutputstream.writeByte(this.stack.stackSize);
            dataoutputstream.writeShort(this.stack.getMetadata());
            writeCompressedCompoundTag(this.stack.getData(), dataoutputstream);
        }
    }

    public int getPacketSize() {
        return 2 + (3*4) + this.windowTitle.length() + 5;
    }
}
