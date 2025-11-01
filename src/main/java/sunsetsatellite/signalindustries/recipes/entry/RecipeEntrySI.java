package sunsetsatellite.signalindustries.recipes.entry;

import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.tiles.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.tiles.TileEntityItemBus;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.RecipeProperties;

public abstract class RecipeEntrySI<I,O,D> extends RecipeEntryBase<I,O,D> {

    public RecipeEntrySI(I input, O output, D data) {
        super(input, output, data);
    }

    public RecipeEntrySI() {
    }

    public abstract boolean matches(RecipeExtendedSymbol[] symbols);

    public abstract boolean matchesQuery(SearchQuery query);

    public abstract boolean matchesScope(SearchQuery query);

    public abstract boolean matchesRecipe(SearchQuery query);

    public abstract boolean matchesUsage(SearchQuery query);

    public abstract void consumeMultiblockInputs(TileEntityTieredMultiblock multiblock);

    public abstract boolean canMultiblockProcess(TileEntityTieredMultiblock multiblock);

    public abstract void processMultiblockRecipe(TileEntityTieredMultiblock multiblock);

    public abstract void consumeMachineInputs(TileEntityTieredMachineSimple machine);

    public abstract boolean canMachineProcess(TileEntityTieredMachineSimple machine);

    public abstract void processMachineRecipe(TileEntityTieredMachineSimple machine);
}
