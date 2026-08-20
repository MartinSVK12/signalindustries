package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IO;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageIOPreview implements NetworkMessage {

	public Vec3i pos;
	public Class<? extends TileEntity> tileClass;
	public IO io;
	public int ticks;

	public NetworkMessageIOPreview() {
	}

	public NetworkMessageIOPreview(Vec3i pos, Class<? extends TileEntity> tileClass, IO io, int ticks) {
		this.pos = pos;
		this.tileClass = tileClass;
		this.io = io;
		this.ticks = ticks;
	}

	@Override
	public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {
		packet.writeInt(io.ordinal());
		packet.writeInt(ticks);
		CompoundTag nbt = new CompoundTag();
		pos.writeToNBT(nbt);
		packet.writeCompoundTag(nbt);
		packet.writeString(TileEntityDispatcher.getIDFromClass(tileClass).toString());
	}

	@Override
	public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {
		io = IO.values()[packet.readInt()];
		ticks = packet.readInt();
		pos = new Vec3i(packet.readCompoundTag());
		tileClass = TileEntityDispatcher.getClassFromID(packet.readString());
	}

	@Override
	public void handleClientEnv(NetworkContext context) {
		TileEntity tileEntity = context.player.world.getTileEntity(pos.tilePos());
		if (tileEntity != null && tileClass == tileEntity.getClass() && tileEntity instanceof IHasIOPreview preview) {
			preview.setTemporaryIOPreview(io, ticks);
		}
	}
}
