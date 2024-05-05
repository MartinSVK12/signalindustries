package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class PlateFormerRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "stone_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Block.stone))
                        },
                        new ItemStack(SignalIndustries.stonePlate,2),
                        new RecipeProperties(200,20, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "cobblestone_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Block.cobbleStone))
                        },
                        new ItemStack(SignalIndustries.cobblestonePlate,2),
                        new RecipeProperties(200,20, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "steel_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.ingotSteel))
                        },
                        new ItemStack(SignalIndustries.steelPlate,1),
                        new RecipeProperties(200,20, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "crystal_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.crystalAlloyIngot))
                        },
                        new ItemStack(SignalIndustries.crystalAlloyPlate,1),
                        new RecipeProperties(200,20, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "saturated_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.saturatedSignalumAlloyIngot))
                        },
                        new ItemStack(SignalIndustries.saturatedSignalumAlloyPlate,1),
                        new RecipeProperties(200,40, Tier.BASIC,false)
                )
        );
        group.register(
                "reinforced_crystal_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.reinforcedCrystalAlloyIngot))
                        },
                        new ItemStack(SignalIndustries.reinforcedCrystalAlloyPlate,1),
                        new RecipeProperties(200,40, Tier.BASIC,false)
                )
        );
        group.register(
                "dilithium_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.dilithiumShard,2))
                        },
                        new ItemStack(SignalIndustries.dilithiumPlate,1),
                        new RecipeProperties(200,80, Tier.REINFORCED,false)
                )
        );
    }
}
