package sunsetsatellite.signalindustries.recipes;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.ArrayList;

public class BasicCrystalCutterRecipes extends CrystalCutterRecipes {
    private static final BasicCrystalCutterRecipes instance = new BasicCrystalCutterRecipes();

    public static BasicCrystalCutterRecipes getInstance() {
        return instance;
    }

    protected BasicCrystalCutterRecipes() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(new FluidStack((BlockFluid) Block.fluidWaterFlowing,500));
        list.add(new ItemStack(SignalIndustries.rawSignalumCrystal,4));
        addRecipe(list,new ItemStack(SignalIndustries.crystalChip.id,1,0));
        list = new ArrayList<>();
        list.add(new FluidStack((BlockFluid) Block.fluidWaterFlowing,2000));
        list.add(new ItemStack(SignalIndustries.signalumCrystal,1));
        addRecipe(list,new ItemStack(SignalIndustries.pureCrystalChip.id,2,0));
        //addRecipe(new ArrayList<Object>{new FluidStack((BlockFluid) Block.waterMoving,1000),new ItemStack(mod_SignalIndustries.rawSignalumCrystal,8)},new ItemStack(mod_SignalIndustries.signalumCrystal,1));
    }

}