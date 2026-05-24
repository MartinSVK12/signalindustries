package sunsetsatellite.signalindustries.tiles;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;

public class TileEntityUVLamp extends TileEntity implements IActiveForm {

    @Override
    public void tick() {
        super.tick();
        if (SignalIndustries.uvLamps.stream().noneMatch((B) -> B.pos.equals(new Vec3i(tilePos)))) {
            if (worldObj != null && worldObj.getBlockType(tilePos) == SIBlocks.uvLamp) {
                SignalIndustries.uvLamps.add(new BlockInstance(SIBlocks.uvLamp, new Vec3i(tilePos), null));
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        SignalIndustries.uvLamps.removeIf((B) -> B.pos.equals(new Vec3i(tilePos)));
    }

	@Override
	public void readAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void writeAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
    public boolean isBurning() {
        return worldObj != null && worldObj.getBlockData(tilePos) == 1;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }
}
