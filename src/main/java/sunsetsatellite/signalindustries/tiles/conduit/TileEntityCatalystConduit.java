package sunsetsatellite.signalindustries.tiles.conduit;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.Blocks;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.simple.impl.TileEntityEnergyConductor;
//import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
//import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.HashMap;
import java.util.Map;


public class TileEntityCatalystConduit extends TileEntityEnergyConductor /*implements ISupportsMultiparts*/ {

    public Tier tier = Tier.PROTOTYPE;

    public TileEntityCatalystConduit() {
    }

    @Override
    public void tick() {
        if (worldObj != null && getBlock() != Blocks.AIR) {
			ITiered logic = Catalyst.blockLogic(getBlock(), ITiered.class);
			if(logic != null) tier = logic.getTier();
        }

        throughput = 128 * (tier.ordinal() + 1);
        super.tick();
    }

	@Override
	public void readAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void writeAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	//public final HashMap<Direction, Multipart> parts = (HashMap<Direction, Multipart>) Catalyst.mapOf(Direction.values(), new Multipart[Direction.values().length]);

	/*@Override
	public void writeAdditionalData(@NotNull CompoundTag tag) {
		CompoundTag coversNbt = new CompoundTag();

		for (Map.Entry<Direction, Multipart> entry : parts.entrySet()) {
			if (entry.getValue() == null) continue;
			CompoundTag partNbt = new CompoundTag();
			entry.getValue().writeToNbt(partNbt);
			coversNbt.putCompound(String.valueOf(entry.getKey().ordinal()), partNbt);
		}

		tag.putCompound("Parts", coversNbt);
	}

	@Override
	public void readAdditionalData(@NotNull CompoundTag tag) {
		CompoundTag coversNbt = tag.getCompound("Parts");

		for (Map.Entry<String, Tag<?>> entry : coversNbt.getValue().entrySet()) {
			Direction dir = Direction.values()[Integer.parseInt(entry.getKey())];
			CompoundTag partTag = (CompoundTag) entry.getValue();
			parts.put(dir, new Multipart(partTag));
		}
	}

    @Override
    public HashMap<Direction, Multipart> getParts() {
        return parts;
    }*/
}
