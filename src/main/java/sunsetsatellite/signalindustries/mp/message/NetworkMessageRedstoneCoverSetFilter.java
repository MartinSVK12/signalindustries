package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.covers.RedstoneCover;
import sunsetsatellite.signalindustries.tiles.TileEntityItemConduit;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.util.PipeType;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageRedstoneCoverSetFilter implements NetworkMessage {

    public Vec3i pos;
    public Class<? extends TileEntity> tileClass;
    public ItemStack stack;
    public Direction dir;

    public NetworkMessageRedstoneCoverSetFilter(Vec3i pos, ItemStack stack, Class<? extends TileEntity> tileClass, Direction dir) {
        this.pos = pos;
        this.stack = stack;
        this.tileClass = tileClass;
        this.dir = dir;
    }

    public NetworkMessageRedstoneCoverSetFilter() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(dir.ordinal());
        CompoundTag nbt = new CompoundTag();
        pos.writeToNBT(nbt);
        packet.writeCompoundTag(nbt);
        packet.writeString(TileEntityDispatcher.getIDFromClass(tileClass).toString());
        if(stack == null) {
            packet.writeInt(-1);
        } else {
            packet.writeInt(1);
            nbt = new CompoundTag();
            stack.writeToNBT(nbt);
            packet.writeCompoundTag(nbt);
        }
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        dir = Direction.values()[packet.readInt()];
        pos = new Vec3i(packet.readCompoundTag());
        tileClass = TileEntityDispatcher.getClassFromID(packet.readString());
        if(packet.readInt() == -1) {
            stack = null;
        } else {
            stack = ItemStack.readItemStackFromNbt(packet.readCompoundTag());
        }
    }

    @Override
    public void handle(NetworkContext context) {
        if(EnvironmentHelper.isServerEnvironment()) {
            if (context.player.world != null) {
                TileEntity tileEntity = context.player.world.getTileEntity(pos.x, pos.y, pos.z);
                if(tileEntity instanceof TileEntityCoverable && tileEntity.worldObj != null){
                    TileEntityCoverable machine = (TileEntityCoverable) tileEntity;
                    ((RedstoneCover) machine.getCovers().get(dir)).sensorStack = stack;
                }
            }
        }
    }
}

