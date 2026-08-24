package sunsetsatellite.signalindustries.tiles.machines;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.items.ItemRomChip;
import sunsetsatellite.signalindustries.items.applications.ItemTrigger;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithAbility;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityProgrammer extends TileEntityTieredMachineBase {

    public int cost;

    public TileEntityProgrammer() {
        itemContents = new ItemStack[2];
        cost = 120;
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }

    public void tick() {
        worldObj.markBlockDirty(tilePos);
        extractFluids();
        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (itemContents[0] == null || itemContents[1] == null) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = (int) (1000 / speedMultiplier);
        }
        if (!worldObj.isClientSide) {
            if (progressTicks == 0 && canProcess()) {
                update = fuel();
            }
            if (isBurning() && canProcess()) {
                progressTicks++;
                if (progressTicks >= progressMaxTicks) {
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if (canProcess()) {
                fuel();
                if (fuelBurnTicks > 0) {
                    fuelBurnTicks++;
                }
            }
        }

        if (update) {
            this.setChanged();
        }

    }

    public boolean fuel() {
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if (burn > 0 && canProcess() && fluidContents[0].amount >= cost) {
            progressMaxTicks = (int) (200 / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if (fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    public void processItem() {
        if (canProcess()) {
            ItemStack chip = itemContents[0];
			if(tier == Tier.REINFORCED){
				if(itemContents[1] != null && itemContents[1].getItem().equals(SIItems.abilityContainerCasing)){
					setItem(1, SIItems.chipsToAbilityMap.get(((ItemRomChip) chip.getItem())).getDefaultStack());
				} else if (SIItems.blankChip.equals(itemContents[1].getItem())) {
					itemContents[1] = chip.copy();
				}
			} else {
				if(itemContents[1].getItem() instanceof ItemTrigger){
					ItemStack trigger = itemContents[1];
					String[] key = chip.getItemKey().split("\\.");
					trigger.getData().putString("ability", key[key.length - 1]);
				} else if (SIItems.blankChip.equals(itemContents[1].getItem())) {
					itemContents[1] = chip.copy();
				}
			}


            doWithNearPlayers(8,(P)->P.triggerAchievement(SIAchievements.PROGRAMMER));
        }
    }

    public boolean canProcess() {
        if (itemContents[0] == null || itemContents[1] == null) {
            return false;
        } else {
            if (itemContents[0].getItem() instanceof ItemRomChip) {
				if(tier == Tier.REINFORCED){
					if (itemContents[1].getItem().equals(SIItems.abilityContainerCasing) || SIItems.blankChip.equals(itemContents[1].getItem())) {
						return itemContents[1].getItem().equals(SIItems.abilityContainerCasing) || !itemContents[1].getData().containsKey("ability");
					}
				} else {
					if (itemContents[1].getItem() instanceof ItemTrigger || SIItems.blankChip.equals(itemContents[1].getItem())) {
						return !itemContents[1].getData().containsKey("ability");
					}
				}
            }
        }
        return false;
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        if (tier == Tier.REINFORCED) {
            return "container.signalindustries.reinforcedProgrammer";
        }
        return "container.signalindustries.programmer";
    }
}
