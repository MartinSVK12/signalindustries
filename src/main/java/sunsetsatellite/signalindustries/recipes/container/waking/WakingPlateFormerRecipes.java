package sunsetsatellite.signalindustries.recipes.container.waking;

import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.container.MachineRecipesBase;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class WakingPlateFormerRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>> {
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "stone_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol("minecraft:stones")
                        },
                        new ItemStack(SIItems.stonePlate, 2),
                        new RecipeProperties(50, 20, Tier.REINFORCED, false)
                )
        );
        group.register(
                "cobblestone_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol("minecraft:cobblestones")
                        },
                        new ItemStack(SIItems.cobblestonePlate, 2),
                        new RecipeProperties(50, 20, Tier.REINFORCED, false)
                )
        );
        group.register(
                "steel_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.INGOT_STEEL))
                        },
                        new ItemStack(SIItems.steelPlate, 1),
                        new RecipeProperties(100, 20, Tier.REINFORCED, false)
                )
        );
        group.register(
                "crystal_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.crystalAlloyIngot))
                        },
                        new ItemStack(SIItems.crystalAlloyPlate, 1),
                        new RecipeProperties(100, 20, Tier.REINFORCED, false)
                )
        );
        group.register(
                "caramel_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.bucketCaramel))
                        },
                        new ItemStack(SIItems.caramelPlate, 1),
                        new RecipeProperties(50, 20, Tier.REINFORCED, false)
                )
        );
        group.register(
                "saturated_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.saturatedSignalumAlloyIngot))
                        },
                        new ItemStack(SIItems.saturatedSignalumAlloyPlate, 1),
                        new RecipeProperties(100, 40, Tier.REINFORCED, false)
                )
        );
        group.register(
                "reinforced_crystal_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.reinforcedCrystalAlloyIngot))
                        },
                        new ItemStack(SIItems.reinforcedCrystalAlloyPlate, 1),
                        new RecipeProperties(100, 40, Tier.REINFORCED, false)
                )
        );
        group.register(
                "dilithium_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.dilithiumShard, 2))
                        },
                        new ItemStack(SIItems.dilithiumPlate, 1),
                        new RecipeProperties(150, 80, Tier.REINFORCED, false)
                )
        );
        group.register(
                "void_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.voidAlloyIngot))
                        },
                        new ItemStack(SIItems.voidAlloyPlate, 1),
                        new RecipeProperties(300, 120, Tier.REINFORCED, false)
                )
        );
        group.register(
                "awakened_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.awakenedAlloyIngot))
                        },
                        new ItemStack(SIItems.awakenedAlloyPlate, 1),
                        new RecipeProperties(600, 120, Tier.REINFORCED, false)
                )
        );
    }
}
