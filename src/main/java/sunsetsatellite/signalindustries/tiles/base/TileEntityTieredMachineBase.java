package sunsetsatellite.signalindustries.tiles.base;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.signalindustries.covers.DilithiumLensCover;
import sunsetsatellite.signalindustries.covers.SwitchCover;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.interfaces.IBooster;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IO;
import sunsetsatellite.signalindustries.util.Tier;

public abstract class TileEntityTieredMachineBase extends TileEntityTieredContainer implements IHasIOPreview, IActiveForm {
    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public float speedMultiplier = 1;
    public float yield = 1;
    public IO preview = IO.NONE;
    public TickTimer IOPreviewTimer = new TickTimer(this, this::disableIOPreview, 20, false);
    public boolean disabled = false;


    @Override
    public void disableIOPreview() {
        preview = IO.NONE;
    }

    @Override
    public void setTemporaryIOPreview(IO preview, int ticks) {
        IOPreviewTimer.value = ticks;
        IOPreviewTimer.max = ticks;
        IOPreviewTimer.unpause();
        this.preview = preview;
    }

    @Override
    public boolean isBurning() {
        return fuelBurnTicks > 0;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void tick() {
        super.tick();
		if(worldObj == null) return;
        if (worldObj.isClientSide) return;
        IOPreviewTimer.tick();
        Block<?> block = getBlock();
        if (block != null) {
            applyModifiers();
        }
    }

    public void applyModifiers() {
        speedMultiplier = 1;
        yield = 1;
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
            if (tile instanceof IBooster && this instanceof IBoostable) {
                if (((IBooster) tile).isBurning()) {
                    int meta = tile.getBlockMeta();
                    Direction side = Direction.getDirectionFromSide(meta);
                    if (side.getOpposite() == dir) {
                        if (((IBooster) tile).getTier() == Tier.BASIC) {
                            speedMultiplier = 1.5f;
                            //yield = 1.05f;
                            if (((IBooster) tile).hasCover(side, DilithiumLensCover.class)) {
                                speedMultiplier = 1.75f;
                                //yield = 1.15f;
                            }
                        } else if (((IBooster) tile).getTier() == Tier.REINFORCED) {
                            speedMultiplier = 2;
                            //yield = 1.25f;
                            if (((IBooster) tile).hasCover(side, DilithiumLensCover.class)) {
                                speedMultiplier = 2.5f;
                                //yield = 1.35f;
                            }
                        } else if (((IBooster) tile).getTier() == Tier.AWAKENED) {
                            speedMultiplier = 3;
                            //yield = 2f;
                            if (((IBooster) tile).hasCover(side, DilithiumLensCover.class)) {
                                speedMultiplier = 4f;
                                //yield = 2.1f;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeAdditionalData(@NonNull CompoundTag tag) {
        super.writeAdditionalData(tag);
        tag.putShort("BurnTime", (short) this.fuelBurnTicks);
        tag.putShort("ProcessTime", (short) this.progressTicks);
        tag.putShort("MaxBurnTime", (short) this.fuelMaxBurnTicks);
        tag.putInt("MaxProcessTime", this.progressMaxTicks);
        tag.putBoolean("Disabled", disabled);
    }

    @Override
    public void readAdditionalData(@NonNull CompoundTag tag) {
        super.readAdditionalData(tag);
        fuelBurnTicks = tag.getShort("BurnTime");
        progressTicks = tag.getShort("ProcessTime");
        progressMaxTicks = tag.getInteger("MaxProcessTime");
        fuelMaxBurnTicks = tag.getShort("MaxBurnTime");
        disabled = tag.getBoolean("Disabled");
    }

    public int getTieredProgressDuration(int defaultTicks) {
        return (int) (((float) defaultTicks / (tier.ordinal() + 1)) / speedMultiplier);
    }

    public int getProgressDuration(int defaultTicks) {
        return (int) (defaultTicks / speedMultiplier);
    }

    public int getProgressScaled(int paramInt) {
        return this.progressTicks * paramInt / progressMaxTicks;
    }

    public int getBurnTimeRemainingScaled(int paramInt) {
        if (this.fuelMaxBurnTicks == 0) {
            this.fuelMaxBurnTicks = 200;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
    }

    @Override
    public IO getPreview() {
        return preview;
    }

    @Override
    public void setPreview(IO preview) {
        this.preview = preview;
    }

    public void onPoweredBlockChange(boolean powered) {
        if (hasCoverAnywhere(SwitchCover.class)) {
            disabled = powered;
        }
    }

	@Override
	public void sort() {

	}
}
