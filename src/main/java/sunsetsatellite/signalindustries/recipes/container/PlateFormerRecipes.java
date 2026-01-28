package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class PlateFormerRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>> {
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "redstone_to_block",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.DUST_REDSTONE,9))
                        },
                        new ItemStack(Blocks.BLOCK_REDSTONE, 1),
                        new RecipeProperties(20, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "lapis_to_block",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.DYE, 9, DyeColor.BLUE.itemMeta))
                        },
                        new ItemStack(Blocks.BLOCK_LAPIS, 1),
                        new RecipeProperties(20, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "coal_to_block",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.COAL, 8))
                        },
                        new ItemStack(Blocks.BLOCK_COAL, 1),
                        new RecipeProperties(20, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "nethercoal_to_block",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.NETHERCOAL, 8))
                        },
                        new ItemStack(Blocks.BLOCK_NETHER_COAL, 1),
                        new RecipeProperties(20, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "signalite_to_block",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal, 9))
                        },
                        new ItemStack(SIBlocks.rawCrystalBlock, 1),
                        new RecipeProperties(20, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "stone_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol("minecraft:stones")
                        },
                        new ItemStack(SIItems.stonePlate, 2),
                        new RecipeProperties(100, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "cobblestone_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol("minecraft:cobblestones")
                        },
                        new ItemStack(SIItems.cobblestonePlate, 2),
                        new RecipeProperties(100, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "steel_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.INGOT_STEEL))
                        },
                        new ItemStack(SIItems.steelPlate, 1),
                        new RecipeProperties(200, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "crystal_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.crystalAlloyIngot))
                        },
                        new ItemStack(SIItems.crystalAlloyPlate, 1),
                        new RecipeProperties(200, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "caramel_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.bucketCaramel))
                        },
                        new ItemStack(SIItems.caramelPlate, 1),
                        new RecipeProperties(200, 20, Tier.PROTOTYPE, false)
                )
        );
        group.register(
                "saturated_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.saturatedSignalumAlloyIngot))
                        },
                        new ItemStack(SIItems.saturatedSignalumAlloyPlate, 1),
                        new RecipeProperties(200, 40, Tier.BASIC, false)
                )
        );
        group.register(
                "reinforced_crystal_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.reinforcedCrystalAlloyIngot))
                        },
                        new ItemStack(SIItems.reinforcedCrystalAlloyPlate, 1),
                        new RecipeProperties(200, 40, Tier.BASIC, false)
                )
        );
        group.register(
                "dilithium_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.dilithiumShard, 2))
                        },
                        new ItemStack(SIItems.dilithiumPlate, 1),
                        new RecipeProperties(200, 80, Tier.REINFORCED, false)
                )
        );
        group.register(
                "void_alloy_plate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.voidAlloyIngot))
                        },
                        new ItemStack(SIItems.voidAlloyPlate, 1),
                        new RecipeProperties(400, 120, Tier.REINFORCED, false)
                )
        );
    }
}
