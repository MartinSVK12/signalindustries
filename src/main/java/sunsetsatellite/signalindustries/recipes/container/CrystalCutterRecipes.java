package sunsetsatellite.signalindustries.recipes.container;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class CrystalCutterRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("saturation",1000);
        nbt.putInt("size",1);
        group.register(
                "signalum_crystal",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.rawSignalumCrystal,8))
                        },
                        new ItemStack(SignalIndustries.signalumCrystal,1,0,nbt),
                        new RecipeProperties(200,80,0, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "volatile_signalum_crystal",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.signalumCrystal,1))
                        },
                        new ItemStack(SignalIndustries.volatileSignalumCrystal,4,0),
                        new RecipeProperties(200,80,3, Tier.BASIC,false)
                )
        );
        group.register(
                "crystal_chip",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,500)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.rawSignalumCrystal,4))
                        },
                        new ItemStack(SignalIndustries.crystalChip,1,0),
                        new RecipeProperties(100,80,1, Tier.BASIC,false)
                )
        );
        group.register(
                "pure_crystal_chip",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,2000)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.signalumCrystal,1))
                        },
                        new ItemStack(SignalIndustries.pureCrystalChip,2,0),
                        new RecipeProperties(100,80,2, Tier.REINFORCED,false)
                )
        );
    }
}
