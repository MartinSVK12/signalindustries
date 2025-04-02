package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBuilder;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.ArrayList;

public class NetworkMessageBuilderConfig implements NetworkMessage {

    public Vec3i pos;
    public Direction dir;
    public boolean toggle;
    public Class<? extends TileEntity> tileClass;

    // for toggle, true = on, false = off
    public NetworkMessageBuilderConfig(Vec3i pos, Direction dir, boolean toggle, Class<? extends TileEntity> tileClass) {
        this.pos = pos;
        this.dir = dir;
        this.toggle = toggle;
        this.tileClass = tileClass;
    }

    public NetworkMessageBuilderConfig() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(dir.ordinal());
        packet.writeBoolean(toggle);
        CompoundTag nbt = new CompoundTag();
        pos.writeToNBT(nbt);
        packet.writeCompoundTag(nbt);
        packet.writeString(TileEntityDispatcher.getIDFromClass(tileClass).toString());
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        dir = Direction.values()[packet.readInt()];
        toggle = packet.readBoolean();
        pos = new Vec3i(packet.readCompoundTag());
        tileClass = TileEntityDispatcher.getClassFromID(packet.readString());
    }

    @Override
    public void handle(NetworkContext context) {
        if(EnvironmentHelper.isServerEnvironment()) {
            if (context.player.world != null) {
                TileEntity tileEntity = context.player.world.getTileEntity(pos.x, pos.y, pos.z);
                if(tileEntity instanceof TileEntityBuilder && tileEntity.worldObj != null){
                    TileEntityBuilder builder = (TileEntityBuilder) tileEntity;
                    if(!pos.equals(builder.offset) || !dir.equals(builder.rotation)){
                        builder.offset = pos;
                        builder.rotation = dir;
                        builder.reset();
                    }
                    if(toggle && builder.workTimer.isPaused()){
                        if(builder.fluidContents[0] != null && builder.itemContents[0] != null && builder.itemContents[0].getItem() instanceof ItemBlueprint) {
                            builder.workTimer.unpause();
                            builder.setStructureToBuild();
                        }
                        for (BlockInstance block : new ArrayList<>(builder.buildingBlocks)) {
                            if(block.exists(builder.worldObj)){
                                builder.buildingBlocks.remove(block);
                                builder.builtBlocks++;
                            }
                        }
                        if(builder.buildingBlockIndex >= builder.buildingBlocks.size()){
                            builder.buildingBlockIndex = 0;
                        }
                    } else {
                        builder.workTimer.pause();
                    }
                }
            }
        }
    }
}

