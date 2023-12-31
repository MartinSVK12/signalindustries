package sunsetsatellite.signalindustries.recipes.container;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
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
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.signalumCrystal,1,0,nbt)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.signalumCrystal,1,0,nbt))
                        },
                        new ItemStack(SignalIndustries.signalumCrystal,1,0,nbt2),
                        new RecipeProperties(800,80, Tier.BASIC,false)
                )
        );
    }
}
