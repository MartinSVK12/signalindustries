package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.client.Minecraft;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.items.ItemRomChip;
import sunsetsatellite.signalindustries.items.ItemTrigger;

public class TileEntityProgrammer extends TileEntityTieredMachineBase {

    public int cost;

    public TileEntityProgrammer(){
        cost = 120;
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SIBlocks.energyFlowing);
    }

    @Override
    public String getInvName() {
        return "EEPROM Programmer";
    }

    public void tick() {
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        boolean update = false;
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null) {
            tier = block.tier;
        }
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(itemContents[0] == null || itemContents[1] == null){
            progressTicks = 0;
        } else if(canProcess()) {
            progressMaxTicks = (int) (1000 / speedMultiplier);
        }
        if(!worldObj.isClientSide){
            if (progressTicks == 0 && canProcess()){
                update = fuel();
            }
            if(isBurning() && canProcess()){
                progressTicks++;
                if(progressTicks >= progressMaxTicks){
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if(canProcess()){
                fuel();
                if(fuelBurnTicks > 0){
                    fuelBurnTicks++;
                }
            }
        }

        if(update) {
            this.onInventoryChanged();
        }

    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if(burn > 0 && canProcess() && fluidContents[0].amount >= cost){
            progressMaxTicks = (int) (200 / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if(fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    public void processItem(){
        if(canProcess()){
            ItemStack chip = itemContents[0];
            ItemStack trigger = itemContents[1];
            String[] key = chip.getItemName().split("\\.");
            trigger.getData().putString("ability",key[key.length-1]);
            Minecraft.getMinecraft(this).thePlayer.triggerAchievement(SIAchievements.PROGRAMMER);
        }
    }

    private boolean canProcess() {
        if(itemContents[0] == null || itemContents[1] == null) {
            return false;
        } else {
            if(itemContents[0].getItem() instanceof ItemRomChip){
                if(itemContents[1].getItem() instanceof ItemTrigger){
                    return !itemContents[1].getData().containsKey("ability");
                }
            }
        }
        return false;
    }

}
