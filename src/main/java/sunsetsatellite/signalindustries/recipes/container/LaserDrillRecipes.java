package sunsetsatellite.signalindustries.recipes.container;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DyeColor;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class LaserDrillRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineRandomOutput>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineRandomOutput> group) {
        WeightedRandomBag<WeightedRandomLootObject> bag = new WeightedRandomBag<>();
        bag.addEntry(new WeightedRandomLootObject(Items.ORE_RAW_IRON.getDefaultStack(),3),29);
        bag.addEntry(new WeightedRandomLootObject(Items.ORE_RAW_GOLD.getDefaultStack(),3),8);
        bag.addEntry(new WeightedRandomLootObject(Items.COAL.getDefaultStack(),3),32);
        bag.addEntry(new WeightedRandomLootObject(Items.DUST_REDSTONE.getDefaultStack(),3 * 4, 3 * 6),12);
        bag.addEntry(new WeightedRandomLootObject(new ItemStack(Items.DYE, 1, DyeColor.BLUE.itemMeta),3 * 4, 3 * 9),3);
        bag.addEntry(new WeightedRandomLootObject(Items.DIAMOND.getDefaultStack(),3),1);
        bag.addEntry(new WeightedRandomLootObject(SIItems.rawSignalumCrystal.getDefaultStack(),3 * 2,3 * 4),15);
        group.register(
                "mine",
                new RecipeEntryMachineRandomOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,1000)),
                        },
                        bag,
                        new RecipeProperties(60,80, Tier.REINFORCED,false)
                )
        );
    }
}
