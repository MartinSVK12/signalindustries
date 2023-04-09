package sunsetsatellite.signalindustries.mp.packets;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.EntityPipeItem;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.PipeItem;
import sunsetsatellite.signalindustries.interfaces.mixins.INetClientHandler;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPipeItemSpawn extends Packet {
    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public Direction inDir = Direction.Y_POS;
    public Direction outDir = Direction.Y_NEG;
    public Vec3f offset = new Vec3f(0,0,0);
    public ItemStack stack;
    public int tileX;
    public int tileY;
    public int tileZ;
    public boolean center = false;
    public boolean end = false;

    public PacketPipeItemSpawn(){}

    public PacketPipeItemSpawn(EntityPipeItem entity){
        this.entityId = entity.entityId;
        this.xPosition = MathHelper.floor_double(entity.posX);
        this.yPosition = MathHelper.floor_double(entity.posY);
        this.zPosition = MathHelper.floor_double(entity.posZ);
        this.stack = entity.item;
        if(entity.pipe != null){
            PipeItem pipeItem = entity.getPipeItem();
            this.inDir = pipeItem.inDir;
            this.outDir = pipeItem.outDir;
            this.tileX = entity.pipe.xCoord;
            this.tileY = entity.pipe.yCoord;
            this.tileZ = entity.pipe.zCoord;
            this.offset = pipeItem.offset;
            this.center = pipeItem.goingToCenter;
            this.end = pipeItem.goingToCenter;
        } else {
            this.tileX = 0;
            this.tileY = 0;
            this.tileZ = 0;
        }


    }

    @Override
    public void readPacketData(DataInputStream dataInputStream) throws IOException {
        entityId = dataInputStream.readInt();
        xPosition = dataInputStream.readInt();
        yPosition = dataInputStream.readInt();
        zPosition = dataInputStream.readInt();
        inDir = Direction.values()[dataInputStream.readInt()];
        outDir = Direction.values()[dataInputStream.readInt()];
        tileX = dataInputStream.readInt();
        tileY = dataInputStream.readInt();
        tileZ = dataInputStream.readInt();
        offset = new Vec3f(dataInputStream.readDouble(),dataInputStream.readDouble(),dataInputStream.readDouble());
        center = dataInputStream.readBoolean();
        end = dataInputStream.readBoolean();

        short id = dataInputStream.readShort();
        if (id >= 0) {
            byte amount = dataInputStream.readByte();
            short metadata = dataInputStream.readShort();
            NBTTagCompound tag = readNBTTagCompound(dataInputStream);
            this.stack = new ItemStack(id, amount, metadata, tag);
        } else {
            this.stack = null;
        }
    }

    @Override
    public void writePacketData(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(entityId);
        dataOutputStream.writeInt(xPosition);
        dataOutputStream.writeInt(yPosition);
        dataOutputStream.writeInt(zPosition);
        dataOutputStream.writeInt(inDir.ordinal());
        dataOutputStream.writeInt(outDir.ordinal());
        dataOutputStream.writeInt(tileX);
        dataOutputStream.writeInt(tileY);
        dataOutputStream.writeInt(tileZ);
        dataOutputStream.writeDouble(offset.x);
        dataOutputStream.writeDouble(offset.y);
        dataOutputStream.writeDouble(offset.z);
        dataOutputStream.writeBoolean(center);
        dataOutputStream.writeBoolean(end);

        if (this.stack == null) {
            dataOutputStream.writeShort(-1);
        } else {
            dataOutputStream.writeShort(this.stack.itemID);
            dataOutputStream.writeByte(this.stack.stackSize);
            dataOutputStream.writeShort(this.stack.getMetadata());
            writeNBTTagCompound(this.stack.tag, dataOutputStream);
        }
    }

    @Override
    public void processPacket(NetHandler netHandler) {
        ((INetClientHandler)netHandler).handlePipeItemSpawn(this);
    }

    @Override
    public int getPacketSize() {
        return 41+24+2;
    }
}
