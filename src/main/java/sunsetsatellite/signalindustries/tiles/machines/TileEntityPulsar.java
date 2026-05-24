package sunsetsatellite.signalindustries.tiles.machines;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.entities.EntityRealityTear;
import sunsetsatellite.signalindustries.entities.EntityShockwave;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class TileEntityPulsar extends TileEntityTieredMachineBase {

    public float orbRotation = 0;
    public boolean charging = false;

    public TileEntityPulsar() {
        itemContents = new ItemStack[1];
        fluidCapacity[0] = 32000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        fuelMaxBurnTicks = 100;
    }

    @Override
    public void tick() {
        super.tick();
        if (orbRotation < 360) {
            orbRotation += 0.1f;
        } else {
            orbRotation = 0;
        }

        if (charging && progressTicks < progressMaxTicks && fluidContents[0] != null && fluidContents[0].amount >= 30) {
            progressTicks++;
            if (getItem(0) != null && getItem(0).getItem() instanceof ItemWarpOrb) {
                fluidContents[0].amount -= 40;
            } else {
                fluidContents[0].amount -= 20;
            }
        }
        if (fuelBurnTicks > 0) {
            charging = false;
            progressTicks = 0;
            fuelBurnTicks--;
        }
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.pulsarBlock";
    }

    public void activate() {
        if (charging && progressTicks >= progressMaxTicks) {
            charging = false;
            progressTicks = 0;
            fuelBurnTicks = fuelMaxBurnTicks;
            if (worldObj != null && getItem(0) != null && getItem(0).getItem() instanceof ItemWarpOrb) {
                EntityRealityTear tear = new EntityRealityTear(worldObj, getPosition(), getItem(0));
                worldObj.entityJoinedWorld(tear);
                setItem(0, null);
            } else if (worldObj != null) {
                EntityShockwave s = new EntityShockwave(worldObj, getPosition());
                worldObj.entityJoinedWorld(s);
            }
        } else if (fuelBurnTicks <= 0 && fluidContents[0] != null && (getItem(0) == null && fluidContents[0] != null && fluidContents[0].amount >= 4000) || (getItem(0) != null && getItem(0).getItem() instanceof ItemWarpOrb && fluidContents[0] != null && fluidContents[0].amount >= 8000)) {
            charging = true;
        }
    }

    @Override
    public boolean isBurning() {
        return progressTicks > 0;
    }

    @Override
    public boolean locked(int index) {
        return isBurning();
    }
}
