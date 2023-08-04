package sunsetsatellite.signalindustries.recipes;



import sunsetsatellite.signalindustries.SignalIndustries;

public class BasicAlloySmelterRecipes extends AlloySmelterRecipes {
    public static final BasicAlloySmelterRecipes instance = new BasicAlloySmelterRecipes();

    protected BasicAlloySmelterRecipes(){
        addRecipe(new Integer[]{SignalIndustries.glowingObsidian.blockID, SignalIndustries.crystalAlloyIngot.itemID},new ItemStack(SignalIndustries.reinforcedCrystalAlloyIngot,1));
    }
}
