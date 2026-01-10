package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.util.IOPreview;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageIOChange implements NetworkMessage {

    public Vec3i pos;
    public Connection connection;
    public Direction dir;
    public IOPreview io;
    public int slot;
    public Class<? extends TileEntity> tileClass;

    public NetworkMessageIOChange(Vec3i pos, Connection connection, Direction dir, IOPreview io, int slot, Class<? extends TileEntity> tileClass) {
        this.pos = pos;
        this.connection = connection;
        this.dir = dir;
        this.io = io;
        this.slot = slot;
        this.tileClass = tileClass;
    }

    public NetworkMessageIOChange() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(connection.ordinal());
        packet.writeInt(dir.ordinal());
        packet.writeInt(io.ordinal());
        packet.writeInt(slot);
        CompoundTag nbt = new CompoundTag();
        pos.writeToNBT(nbt);
        packet.writeCompoundTag(nbt);
        packet.writeString(TileEntityDispatcher.getIDFromClass(tileClass).toString());
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        connection = Connection.values()[packet.readInt()];
        dir = Direction.values()[packet.readInt()];
        io = IOPreview.values()[packet.readInt()];
        slot = packet.readInt();
        pos = new Vec3i(packet.readCompoundTag());
        tileClass = TileEntityDispatcher.getClassFromID(packet.readString());
    }

    @Override
    public void handle(NetworkContext context) {
        if (EnvironmentHelper.isServerEnvironment()) {
            if (context.player.world != null) {
                TileEntity tileEntity = context.player.world.getTileEntity(pos.x, pos.y, pos.z);
                switch (io) {
                    case NONE:
                        return;
                    case ITEM:
                        if (tileClass == tileEntity.getClass() && tileEntity instanceof IItemIO) {
                            ((IItemIO) tileEntity).setItemIOForSide(dir, connection);
                            ((IItemIO) tileEntity).setActiveItemSlotForSide(dir, slot);
                        }
                        break;
                    case FLUID:
                        if (tileClass == tileEntity.getClass() && tileEntity instanceof IFluidIO) {
                            ((IFluidIO) tileEntity).setFluidIOForSide(dir, connection);
                            ((IFluidIO) tileEntity).setActiveFluidSlotForSide(dir, slot);
                        }
                        break;
                }
            }
        }
    }
}
