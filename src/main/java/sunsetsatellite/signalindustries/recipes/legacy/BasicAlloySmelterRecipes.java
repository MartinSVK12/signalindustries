package sunsetsatellite.signalindustries.recipes.legacy;


import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;

@Deprecated
public class BasicAlloySmelterRecipes extends AlloySmelterRecipes {
    public static final BasicAlloySmelterRecipes instance = new BasicAlloySmelterRecipes();

    protected BasicAlloySmelterRecipes(){
        addRecipe(new Integer[]{SIBlocks.glowingObsidian.id, SIItems.crystalAlloyIngot.id},new ItemStack(SIItems.reinforcedCrystalAlloyIngot,1));
    }
}
