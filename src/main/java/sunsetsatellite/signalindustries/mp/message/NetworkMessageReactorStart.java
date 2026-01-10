package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntitySignalumReactor;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageReactorStart implements NetworkMessage {

    public Vec3i pos;
    public Class<? extends TileEntity> tileClass;

    public NetworkMessageReactorStart(Vec3i pos, Class<? extends TileEntity> tileClass) {
        this.pos = pos;
        this.tileClass = tileClass;
    }

    public NetworkMessageReactorStart() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        CompoundTag nbt = new CompoundTag();
        pos.writeToNBT(nbt);
        packet.writeCompoundTag(nbt);
        packet.writeString(TileEntityDispatcher.getIDFromClass(tileClass).toString());
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        pos = new Vec3i(packet.readCompoundTag());
        tileClass = TileEntityDispatcher.getClassFromID(packet.readString());
    }

    @Override
    public void handle(NetworkContext context) {
        if (EnvironmentHelper.isServerEnvironment()) {
            if (context.player.world != null) {
                TileEntity tileEntity = context.player.world.getTileEntity(pos.x, pos.y, pos.z);
                if (tileEntity instanceof TileEntitySignalumReactor && tileEntity.worldObj != null) {
                    ((TileEntitySignalumReactor) tileEntity).start();
                }
            }
        }
    }
}

