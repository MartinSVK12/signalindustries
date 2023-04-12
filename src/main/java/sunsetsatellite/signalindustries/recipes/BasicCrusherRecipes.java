package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;

public class BasicCrusherRecipes extends CrusherRecipes {
    public static final BasicCrusherRecipes instance = new BasicCrusherRecipes();

    private BasicCrusherRecipes() {
        addRecipe(Item.nethercoal.itemID,new ItemStack(SignalIndustries.netherCoalDust,1));
    }
}