package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class AlloySmelterRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "crystal_alloy_ingot",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.ingotSteel)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.emptySignalumCrystalDust))
                        },
                        new ItemStack(SignalIndustries.crystalAlloyIngot,1),
                        new RecipeProperties(200,40,Tier.PROTOTYPE,false)
                )
        );
    }
}
