package sunsetsatellite.signalindustries.inventories.machines;


import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.items.containers.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;

import java.util.ArrayList;

public class TileEntityCrystalCutter extends TileEntityTieredMachineSimple implements IBoostable {

    public int recipeSelector = 0;

    public TileEntityCrystalCutter(){
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 1000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        acceptedFluids.get(1).add((BlockFluid) Block.fluidWaterFlowing);
        energySlot = 0;
        recipeGroup = SIRecipes.CRYSTAL_CUTTER;
        itemInputs = new int[]{0};
        itemOutputs = new int[]{1};
        fluidInputs = new int[]{1};
    }
    @Override
    public String getInvName() {
        return "Crystal Cutter";
    }

    @Override
    public void readFromNBT(CompoundTag nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        recipeSelector = nBTTagCompound1.getInteger("SelectedRecipe");
    }

    @Override
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        nBTTagCompound1.putInt("SelectedRecipe",recipeSelector);
    }

    @Override
    public void tick() {
        recipeId = recipeSelector;
        fluidCapacity[1] = (int) (1000 * (Math.pow(2,tier.ordinal())));
        super.tick();
    }

    @Override
    public void processItem() {
        super.processItem();
        if(itemContents[itemOutputs[0]].getItem() instanceof ItemSignalumCrystal){
            Minecraft.getMinecraft(this).thePlayer.triggerAchievement(SignalIndustriesAchievementPage.SHINING);
        }
        if(itemContents[itemOutputs[0]].getItem() == SignalIndustries.signalumCrystalEmpty){
            if(fluidContents[energySlot].amount <= fluidCapacity[energySlot]+1000){
                fluidContents[energySlot].amount += 1000;
            }
        }
    }

}
