package sunsetsatellite.signalindustries.recipes.container;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIItems;
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
        CompoundTag nbt2 = new CompoundTag();
        nbt2.putInt("size",1);
        group.register(
                "signalum_crystal",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal,8))
                        },
                        new ItemStack(SIItems.signalumCrystal,1,0),
                        new RecipeProperties(200,80,0, Tier.PROTOTYPE,true)
                )
        );
        group.register(
                "signalum_crystal_battery",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystal,1))
                        },
                        new ItemStack(SIItems.signalumCrystalBattery,1,0,nbt),
                        new RecipeProperties(100,80,1, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "empty_signalum_crystal",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal,8))
                        },
                        new ItemStack(SIItems.signalumCrystalEmpty,1,0,nbt2),
                        new RecipeProperties(200,80,2, Tier.PROTOTYPE,true)
                )
        );
        group.register(
                "basic_signalum_crystal",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal,4))
                        },
                        new ItemStack(SIItems.signalumCrystal,1,0,nbt),
                        new RecipeProperties(200,80,0, Tier.BASIC,false)
                )
        );
        group.register(
                "basic_empty_signalum_crystal",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal,4))
                        },
                        new ItemStack(SIItems.signalumCrystalEmpty,1,0,nbt2),
                        new RecipeProperties(200,80,2, Tier.BASIC,false)
                )
        );
        group.register(
                "volatile_signalum_crystal",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystal,1))
                        },
                        new ItemStack(SIItems.volatileSignalumCrystal,4,0),
                        new RecipeProperties(200,80,3, Tier.BASIC,false)
                )
        );
        group.register(
                "crystal_chip",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,500)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal,2))
                        },
                        new ItemStack(SIItems.crystalChip,1,0),
                        new RecipeProperties(100,80,4, Tier.BASIC,false)
                )
        );
        group.register(
                "pure_crystal_chip",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,2000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystal,1))
                        },
                        new ItemStack(SIItems.pureCrystalChip,2,0),
                        new RecipeProperties(100,80,5, Tier.REINFORCED,false)
                )
        );
        group.register(
                "dimensional_chip",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,2000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.dimensionalShard,1))
                        },
                        new ItemStack(SIItems.dimensionalChip,2,0),
                        new RecipeProperties(200,160,6, Tier.REINFORCED,false)
                )
        );
        group.register(
                "dilithium_chip",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,2000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.dilithiumShard,1))
                        },
                        new ItemStack(SIItems.dilithiumChip,2,0),
                        new RecipeProperties(100,160,7, Tier.REINFORCED,false)
                )
        );
        group.register(
                "signalum_alloy_mesh",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) BlockFluid.fluidWaterFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.saturatedSignalumAlloyPlate,1))
                        },
                        new ItemStack(SIItems.signalumAlloyMesh,1,0),
                        new RecipeProperties(100,80,8, Tier.BASIC,false)
                )
        );
        group.register(
                "krowka",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.caramelPlate,1))
                        },
                        new ItemStack(SIItems.krowka,8,0,nbt),
                        new RecipeProperties(50,20,9, Tier.PROTOTYPE,false)
                )
        );
    }
}
