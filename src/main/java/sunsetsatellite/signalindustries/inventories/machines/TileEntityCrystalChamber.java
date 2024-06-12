package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.items.containers.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.recipes.SIRecipes;

import java.util.ArrayList;

public class TileEntityCrystalChamber extends TileEntityTieredMachineSimple implements IBoostable {

    //public CrystalChamberRecipes recipes = CrystalChamberRecipes.instance;

    public TileEntityCrystalChamber(){
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 4000;
        itemContents = new ItemStack[3];
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SIBlocks.energyFlowing);
        energySlot = 0;
        recipeGroup = SIRecipes.CRYSTAL_CHAMBER;
        itemInputs = new int[]{0,2};
        itemOutputs = new int[]{1};
    }
    @Override
    public String getInvName() {
        return "Crystal Chamber";
    }

    @Override
    public boolean canProcess() {
        ItemStack c1 = this.itemContents[itemInputs[0]];
        ItemStack c2 = this.itemContents[itemInputs[1]];
        if(c1 != null && c2 != null){
            if(c1.getData().getInteger("size") + c2.getData().getInteger("size") > 8){
                return false;
            }
        }
        return super.canProcess();
    }

    @Override
    public void processItem() {
        if(canProcess()){
            int size1 = this.itemContents[itemInputs[0]].getData().getInteger("size");
            int sat1 = this.itemContents[itemInputs[0]].getData().getInteger("saturation");
            int size2 = this.itemContents[itemInputs[1]].getData().getInteger("size");
            int sat2 = this.itemContents[itemInputs[1]].getData().getInteger("saturation");
            super.processItem();
            if(this.itemContents[itemOutputs[0]] != null){
                this.itemContents[itemOutputs[0]].getData().putInt("size",size1+size2);
                this.itemContents[itemOutputs[0]].getData().putInt("saturation", sat1 + sat2);
            }
            if(itemContents[itemOutputs[0]].getItem() instanceof ItemSignalumCrystal && !Global.isServer){
                Minecraft.getMinecraft(this).thePlayer.triggerAchievement(SIAchievements.COMBINED);
            }
        }
    }
}
