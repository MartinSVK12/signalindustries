package sunsetsatellite.signalindustries.inventories.machines;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;

public class TileEntityChunkloader extends TileEntity implements IActiveForm {

    public boolean active = false;

    @Override
    public boolean isBurning() {
        return active;
    }

    @Override
    public void tick() {
        worldObj.markBlockDirty(x,y,z);
        super.tick();
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("Active",active);
        super.writeToNBT(nbttagcompound);
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        active = nbttagcompound.getBoolean("Active");
        super.readFromNBT(nbttagcompound);
    }

    @Override
    public boolean isDisabled() {
        return false;
    }
}
