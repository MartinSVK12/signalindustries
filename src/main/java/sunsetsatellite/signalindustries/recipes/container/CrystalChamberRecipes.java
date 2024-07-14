package sunsetsatellite.signalindustries.recipes.container;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class CrystalChamberRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("saturation",1000);
        nbt.putInt("size",1);
        CompoundTag nbt2 = new CompoundTag();
        nbt2.putInt("saturation",2000);
        nbt2.putInt("size",2);
        group.register(
                "size_up",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystalBattery,1,0,nbt)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystalBattery,1,0,nbt))
                        },
                        new ItemStack(SIItems.signalumCrystalBattery,1,0,nbt2),
                        new RecipeProperties(800,80, Tier.BASIC,false)
                )
        );
        group.register(
                "awakened_combine",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.awakenedSignalumFragment,4,0)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.awakenedSignalumFragment,4,0))
                        },
                        new ItemStack(SIItems.awakenedSignalumCrystal,1,0),
                        new RecipeProperties(3000,320, Tier.REINFORCED,false)
                )
        );
    }
}
