package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.tiles.TileEntityFilter;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageFilterConfig implements NetworkMessage {

    public Vec3i pos;
    public Class<? extends TileEntity> tileClass;
    public TileEntityFilter.FilterSide defaultSide;
    public boolean ignoreMeta;

    public NetworkMessageFilterConfig(Vec3i pos, Class<? extends TileEntity> tileClass, TileEntityFilter.FilterSide defaultSide, boolean ignoreMeta) {
        this.pos = pos;
        this.tileClass = tileClass;
        this.defaultSide = defaultSide;
        this.ignoreMeta = ignoreMeta;
    }

    public NetworkMessageFilterConfig() {
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        CompoundTag nbt = new CompoundTag();
        pos.writeToNBT(nbt);
        packet.writeCompoundTag(nbt);
        packet.writeString(TileEntityDispatcher.getIDFromClass(tileClass).toString());
        packet.writeByte(defaultSide.ordinal());
        packet.writeBoolean(ignoreMeta);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        pos = new Vec3i(packet.readCompoundTag());
        tileClass = TileEntityDispatcher.getClassFromID(packet.readString());
        defaultSide = TileEntityFilter.FilterSide.values()[packet.readByte()];
        ignoreMeta = packet.readBoolean();
    }

    @Override
    public void handle(NetworkContext context) {
        if (EnvironmentHelper.isServerEnvironment()) {
            if (context.player.world != null) {
                TileEntity tileEntity = context.player.world.getTileEntity(pos.x, pos.y, pos.z);
                if (tileEntity instanceof TileEntityFilter && tileEntity.worldObj != null) {
                    ((TileEntityFilter) tileEntity).defaultSide = defaultSide;
                    ((TileEntityFilter) tileEntity).ignoreMeta = ignoreMeta;
                }
            }
        }
    }
}

