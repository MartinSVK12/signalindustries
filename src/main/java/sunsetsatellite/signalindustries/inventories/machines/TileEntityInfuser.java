package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.client.Minecraft;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockGlowingObsidian;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.items.containers.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;

import java.util.ArrayList;

public class TileEntityInfuser extends TileEntityTieredMachineSimple implements IBoostable {

   // public MachineRecipesBase<ArrayList<Object>, ItemStack> recipes = InfuserRecipes.instance;

    public TileEntityInfuser(){
        fluidContents = new FluidStack[2];
        fluidCapacity = new int[2];
        fluidCapacity[0] = 2000;
        fluidCapacity[1] = 4000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        acceptedFluids.get(1).addAll(CatalystFluids.FLUIDS.getAllFluids());
        acceptedFluids.get(1).remove((BlockFluid) SignalIndustries.energyFlowing);
        itemContents = new ItemStack[3];
        energySlot = 0;
        recipeGroup = SIRecipes.INFUSER;
        itemInputs = new int[]{0,1};
        itemOutputs = new int[]{2};
        fluidInputs = new int[]{1};
    }
    @Override
    public String getInvName() {
        return "Infuser";
    }

    @Override
    public void processItem() {
        super.processItem();
        if(itemContents[itemOutputs[0]].itemID == SignalIndustries.glowingObsidian.id){
            Minecraft.getMinecraft(this).thePlayer.triggerAchievement(SignalIndustriesAchievementPage.RELIC);
        }
    }

}
