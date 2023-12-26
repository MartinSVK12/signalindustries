package sunsetsatellite.signalindustries.recipes.legacy;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
@Deprecated
public class BasicAlloySmelterRecipes extends AlloySmelterRecipes {
    public static final BasicAlloySmelterRecipes instance = new BasicAlloySmelterRecipes();

    protected BasicAlloySmelterRecipes(){
        addRecipe(new Integer[]{SignalIndustries.glowingObsidian.id, SignalIndustries.crystalAlloyIngot.id},new ItemStack(SignalIndustries.reinforcedCrystalAlloyIngot,1));
    }
}
