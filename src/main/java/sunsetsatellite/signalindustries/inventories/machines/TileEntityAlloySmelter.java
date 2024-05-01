package sunsetsatellite.signalindustries.inventories.machines;


import net.minecraft.client.Minecraft;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.recipes.SIRecipes;


public class TileEntityAlloySmelter extends TileEntityTieredMachineSimple implements IBoostable {

    public TileEntityAlloySmelter(){
        itemContents = new ItemStack[3];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        energySlot = 0;
        recipeGroup = SIRecipes.ALLOY_SMELTER;
        itemInputs = new int[]{0,2};
        itemOutputs = new int[]{1};
    }

    @Override
    public String getInvName() {
        return "Alloy Smelter";
    }

    @Override
    public void processItem() {
        super.processItem();
        if(itemContents[itemOutputs[0]].itemID == SignalIndustries.reinforcedCrystalAlloyIngot.id){
            Minecraft.getMinecraft(this).thePlayer.triggerAchievement(SignalIndustriesAchievementPage.KNIGHTS_ALLOY);
        }
    }

}
