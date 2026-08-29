package sunsetsatellite.signalindustries.tiles.conduit;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidPipe;
//import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
//import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.HashMap;
import java.util.Map;

public class TileEntityConduit extends TileEntityFluidPipe /*implements ISupportsMultiparts*/ {

    public TileEntityConduit() {
        acceptedFluids.get(0).clear();
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }

    @Override
    public void tick() {
        if (fluidContents[0] != null && fluidContents[0].amount < 0) {
            fluidContents[0] = null;
        }
        ITiered tiered = Catalyst.blockLogic(getBlock(), ITiered.class);
        if (tiered != null) {
            Tier tier = tiered.getTier();
            switch (tier){
                case PROTOTYPE:
                    transferSpeed = 20;
                    break;
                case BASIC:
                    transferSpeed = 100;
                    break;
                case REINFORCED:
                    transferSpeed = 500;
                    break;
                case AWAKENED:
                    transferSpeed = 1000;
                    break;
                case INFINITE:
                    transferSpeed = Integer.MAX_VALUE;
                    break;
            }
			fluidCapacity[0] = transferSpeed;
        }
        super.tick();
    }

    /*public final HashMap<Direction, Multipart> parts = (HashMap<Direction, Multipart>) Catalyst.mapOf(Direction.values(), new Multipart[Direction.values().length]);

	@Override
	public void writeAdditionalData(@NonNull CompoundTag tag) {
		super.writeAdditionalData(tag);
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
		super.readAdditionalData(tag);
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
