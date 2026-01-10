package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.Dimension;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class LaserDrillRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineRandomOutput>> {
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineRandomOutput> group) {
        WeightedRandomBag<WeightedRandomLootObject> bag = new WeightedRandomBag<>();
        bag.addEntry(new WeightedRandomLootObject(Items.ORE_RAW_IRON.getDefaultStack(), 3), 29);
        bag.addEntry(new WeightedRandomLootObject(Items.ORE_RAW_GOLD.getDefaultStack(), 3), 8);
        bag.addEntry(new WeightedRandomLootObject(Items.COAL.getDefaultStack(), 3), 32);
        bag.addEntry(new WeightedRandomLootObject(Items.DUST_REDSTONE.getDefaultStack(), 3 * 4, 3 * 6), 12);
        bag.addEntry(new WeightedRandomLootObject(new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta), 3 * 4, 3 * 9), 3);
        bag.addEntry(new WeightedRandomLootObject(Items.DIAMOND.getDefaultStack(), 3), 1);
        bag.addEntry(new WeightedRandomLootObject(SIItems.rawSignalumCrystal.getDefaultStack(), 3 * 2, 3 * 4), 15);
        WeightedRandomBag<WeightedRandomLootObject> bagNether = new WeightedRandomBag<>();
        bagNether.addEntry(new WeightedRandomLootObject(Items.NETHERCOAL.getDefaultStack(), 3), 3);
        bagNether.addEntry(new WeightedRandomLootObject(Items.DUST_GLOWSTONE.getDefaultStack(), 3 * 4), 1);
        WeightedRandomBag<WeightedRandomLootObject> bagEternity = new WeightedRandomBag<>();
        bagEternity.addEntry(new WeightedRandomLootObject(SIItems.dilithiumShard.getDefaultStack(), 3), 1);
        group.register(
                "collect",
                new RecipeEntryMachineRandomOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER, 1000)),
                        },
                        bag,
                        new RecipeProperties(60, 80, Tier.REINFORCED, false).setAllowedDimensions(Catalyst.listOf(Dimension.OVERWORLD))
                )
        );
        group.register(
                "collect_nether",
                new RecipeEntryMachineRandomOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA, 10)),
                        },
                        bagNether,
                        new RecipeProperties(60, 80, Tier.REINFORCED, false).setAllowedDimensions(Catalyst.listOf(Dimension.NETHER))
                )
        );
        group.register(
                "collect_eternity",
                new RecipeEntryMachineRandomOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(SIFluids.WORLD_RESIN, 10)),
                        },
                        bagEternity,
                        new RecipeProperties(100, 160, Tier.REINFORCED, false).setAllowedDimensions(Catalyst.listOf(SIDimensions.ETERNITY))
                )
        );
    }
}
