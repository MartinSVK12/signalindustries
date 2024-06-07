package sunsetsatellite.signalindustries.recipes.legacy;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIItems;

@Deprecated
public class BasicCrusherRecipes extends CrusherRecipes {
    public static final BasicCrusherRecipes instance = new BasicCrusherRecipes();

    private BasicCrusherRecipes() {
        addRecipe(Item.nethercoal.id,new ItemStack(SIItems.netherCoalDust,1));
    }
}