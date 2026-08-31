package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.tiles.TileEntityExternalIO;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageExternalIOLinkBreak implements NetworkMessage {

    public Vec3i pos;
    public Class<? extends TileEntity> tileClass;

    public NetworkMessageExternalIOLinkBreak(Vec3i pos, Class<? extends TileEntity> tileClass) {
        this.pos = pos;
        this.tileClass = tileClass;
    }

    public NetworkMessageExternalIOLinkBreak() {
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
        if (EnvironmentHelper.isMultiplayerServer()) {
			TileEntity tileEntity = context.player.world.getTileEntity(pos.tilePos());
			if (tileEntity instanceof TileEntityExternalIO externalIO && tileEntity.worldObj != null) {
				if(externalIO.tier == Tier.BASIC) {
					if(externalIO.lockedDirection == null && externalIO.externalTileSide != null){
						externalIO.lockedDirection = externalIO.externalTileSide;
					} else if(externalIO.lockedDirection != null) {
						externalIO.lockedDirection = null;
					}
					return;
				}
				externalIO.externalTile = null;
				externalIO.externalTileSide = null;
				externalIO.externalTilePos = null;
			}
		}
    }
}

