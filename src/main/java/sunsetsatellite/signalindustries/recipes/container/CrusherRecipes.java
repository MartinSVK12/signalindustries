package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class CrusherRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "prototype_raw_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.rawSignalumCrystal))
                        },
                        new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.coal))
                        },
                        new ItemStack(SignalIndustries.coalDust,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_empty_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.signalumCrystalEmpty))
                        },
                        new ItemStack(SignalIndustries.emptySignalumCrystalDust,2),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.signalumCrystal))
                        },
                        new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,2),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "basic_nether_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.nethercoal))
                        },
                        new ItemStack(SignalIndustries.netherCoalDust,1),
                        new RecipeProperties(100,80, Tier.BASIC,false)
                )
        );
    }
}
