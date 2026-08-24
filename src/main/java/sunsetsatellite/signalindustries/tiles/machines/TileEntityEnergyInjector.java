package sunsetsatellite.signalindustries.tiles.machines;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IInjectable;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityEnergyInjector extends TileEntityTieredMachineBase {

    public int injectSpeed = 5;
	public boolean isInjecting = false;

    public TileEntityEnergyInjector() {
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity[0] = 4000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }

    @Override
    public void tick() {
        if (worldObj != null) {
            worldObj.markBlockDirty(tilePos);
        }
        extractFluids();
        if (isBurning()) {
            ItemStack stack = getItem(0);
            if (stack != null) {
                if (stack.getItem() instanceof IItemFluidContainer) {
                    IItemFluidContainer item = (IItemFluidContainer) getItem(0).getItem();
                    if (item.canFill(stack)) {
                        ItemStack itemStack = item.fill(getFluidInSlot(0), stack, this, injectSpeed);
                        if (itemStack != null) {
                            setItem(0, itemStack);
                        }
                    }
                } else if (stack.getItem() instanceof IInjectable) {
                    IInjectable item = (IInjectable) getItem(0).getItem();
                    if (item.canFill(stack)) {
                        item.fill(getFluidInSlot(0), stack, this, injectSpeed);
                    }
                }

            }
            /*for (float i = 0; i < 0.5; i+=0.01f) {
                SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+0.5,y+i,z+0.5,0,0,0,1.0f,1.0f,0.0f,0.0f,2));
            }*/
        }
    }

    @Override
    public boolean isBurning() {
        ItemStack stack = getItem(0);
        if (getFluidInSlot(0) != null
                && getFluidInSlot(0).amount >= transferSpeed
                && stack != null
                && (stack.getItem() instanceof IItemFluidContainer || stack.getItem() instanceof IInjectable || isInjecting)) {
            if (stack.getItem() instanceof IItemFluidContainer) {
                return ((IItemFluidContainer) stack.getItem()).canFill(stack);
            } else if(stack.getItem() instanceof IInjectable) {
                return ((IInjectable) stack.getItem()).canFill(stack);
            } else {
				return true;
			}
        } else return false;
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.energyInjector";
    }

	public void onEntityCollision(@NotNull Entity entity) {
		if (tier != Tier.REINFORCED) {
			return;
		}
		if(entity instanceof Player player){
			for (ItemStack stack : player.inventory.armorInventory) {
				if(getFluidInSlot(0) != null && getFluidInSlot(0).amount >= transferSpeed){
					if(stack != null && stack.getItem() instanceof IInjectable injectable){
						if(injectable.canFill(stack)){
							((IInjectable) stack.getItem()).fill(getFluidInSlot(0), stack, this, transferSpeed);
							isInjecting = true;
						}
					}
				}
			}
			for (ItemStack stack : player.inventory.mainInventory) {
				if(getFluidInSlot(0) != null && getFluidInSlot(0).amount >= transferSpeed){
					if(stack != null && stack.getItem() instanceof IInjectable injectable){
						if(injectable.canFill(stack)){
							((IInjectable) stack.getItem()).fill(getFluidInSlot(0), stack, this, transferSpeed);
							isInjecting = true;
						}
					} else if (stack != null && stack.getItem() instanceof IItemFluidContainer fluidContainer) {
						if(stack.getItem().equals(SIItems.infiniteSignalumCrystal)) continue;
						if(fluidContainer.canFill(stack)){
							((IItemFluidContainer) stack.getItem()).fill(getFluidInSlot(0), stack, this, transferSpeed);
							isInjecting = true;
						}
					}
				}
			}
			if(((IPlayerPowerSuit<?>) player).getPowerSuit() != null){
				((IPlayerPowerSuit<?>) player).getPowerSuit().reload();
			}
		}
		isInjecting = false;
	}
}
