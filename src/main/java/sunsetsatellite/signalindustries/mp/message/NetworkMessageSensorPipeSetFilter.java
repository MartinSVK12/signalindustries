package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.tiles.TileEntityItemConduit;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntitySignalumReactor;
import sunsetsatellite.signalindustries.util.PipeType;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageSensorPipeSetFilter implements NetworkMessage {

    public Vec3i pos;
    public Class<? extends TileEntity> tileClass;
    public ItemStack stack;

    public NetworkMessageSensorPipeSetFilter(Vec3i pos, ItemStack stack, Class<? extends TileEntity> tileClass) {
        this.pos = pos;
        this.stack = stack;
        this.tileClass = tileClass;
    }

    public NetworkMessageSensorPipeSetFilter() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
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
                if(tileEntity instanceof TileEntityItemConduit && tileEntity.worldObj != null){
                    TileEntityItemConduit itemConduit = (TileEntityItemConduit) tileEntity;
                    if(itemConduit.type == PipeType.SENSOR){
                        itemConduit.sensorStack = stack;
                    }
                }
            }
        }
    }
}

