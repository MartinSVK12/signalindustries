package sunsetsatellite.signalindustries.tiles;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;

public class TileEntityRedstoneClock extends TileEntity implements IActiveForm, IScreenActionListener {

    public boolean active = false;
    public boolean disabled = false;

    public int ticksOn = 20;
    public int ticksOff = 20;

    public int timer = 0;

    @Override
    public void tick() {
        super.tick();
        if (worldObj == null) return;
        worldObj.markBlockDirty(tilePos);
		//FIXME:
		worldObj.notifyBlockChange(tilePos, getBlock());
        //worldObj.notifyBlocksOfNeighborChange(tilePos, active ? 15 : 0);
        if (disabled) {
            active = false;
            return;
        }
        if (!active) {
            timer++;
            if (timer >= ticksOff) {
                active = true;
                timer = 0;
            }
        } else {
            timer++;
            if (timer >= ticksOn) {
                active = false;
                timer = 0;
            }
        }
    }

	@Override
	public void writeAdditionalData(@NotNull CompoundTag tag) {
		tag.putBoolean("Active", active);
		tag.putBoolean("Disabled", disabled);
		tag.putInt("TicksOn", ticksOn);
		tag.putInt("TicksOff", ticksOff);
	}

	@Override
	public void readAdditionalData(@NotNull CompoundTag tag) {
		active = tag.getBoolean("Active");
		disabled = tag.getBoolean("Disabled");
		ticksOn = tag.getInteger("TicksOn");
		ticksOff = tag.getInteger("TicksOff");
	}

	@Override
    public boolean isBurning() {
        return active;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 1) {
            ticksOn++;
        } else if (id == 2 && ticksOn > 1) {
            ticksOn--;
        }
        if (id == 3) {
            ticksOff++;
        } else if (id == 4 && ticksOff > 1) {
            ticksOff--;
        }
    }
}
