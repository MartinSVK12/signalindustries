package sunsetsatellite.signalindustries.recipes;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

public class BasicCrusherRecipes extends CrusherRecipes {
    public static final BasicCrusherRecipes instance = new BasicCrusherRecipes();

    private BasicCrusherRecipes() {
        addRecipe(Item.nethercoal.id,new ItemStack(SignalIndustries.netherCoalDust,1));
    }
}