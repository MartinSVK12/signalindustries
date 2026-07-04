package sunsetsatellite.signalindustries.tiles.machines.multiblocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.ITileEntityInit;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.ArrayList;
import java.util.Arrays;

public class TileEntityReinforcedExtractor extends TileEntityTieredMachineBase implements IMultiblock, ITileEntityInit {

    public MultiblockInstance multiblock;
    public TileEntityItemBus input;
    public TileEntityFluidHatch output;
    public RecipeGroupSI<?> recipeGroup;
    public RecipeEntrySI<?, ?, RecipeProperties> currentRecipe;

    public TileEntityReinforcedExtractor() {
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        recipeGroup = SIRecipes.EXTRACTOR;
        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("extractionManifold"));
    }

    @Override
    public void init(Block<?> block) {
        super.init(block);
        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("extractionManifold"));
    }

    @Override
    public MultiblockInstance getMultiblock() {
        return multiblock;
    }

    @Override
    public void tick() {
        if (multiblock == null) {
            return;
        }
        super.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        input = null;
        output = null;
        if (multiblock.isValid()) {
            Direction dir = Direction.getDirectionFromSide(getBlockMeta());
            ArrayList<BlockInstance> tileEntities = multiblock.data.getTileEntities(worldObj, new Vec3i(x, y, z), dir);
            for (BlockInstance tileEntity : tileEntities) {
                if (tileEntity.tile instanceof IMultiblockPart) {
                    if (tileEntity.tile instanceof TileEntityItemBus && tileEntity.block == SIBlocks.reinforcedItemInputBus) {
                        input = (TileEntityItemBus) tileEntity.tile;
                    } else if (tileEntity.tile instanceof TileEntityFluidHatch && tileEntity.block == SIBlocks.reinforcedFluidOutputHatch) {
                        output = (TileEntityFluidHatch) tileEntity.tile;
                    }
                    ((IMultiblockPart) tileEntity.tile).connect(this);
                }
            }
            if (input != null && output != null) {
                setCurrentRecipe();
                work();
            }

            if (isBurning()) {
                ArrayList<BlockInstance> blocks = multiblock.data.getBlocks(new Vec3i(x, y, z), dir);
                for (BlockInstance structBlock : blocks) {
                    if (structBlock.block == SIBlocks.reinforcedCasing2 || structBlock.block == SIBlocks.awakenedSocketCasing || structBlock.block == SIBlocks.awakenedCasing2) {
                        if (structBlock.pos.getBlockMetadata(worldObj) != 1) {
                            worldObj.setBlockMetadata(structBlock.pos.x, structBlock.pos.y, structBlock.pos.z, 1);
                        }
                    }
                }
            } else {
                ArrayList<BlockInstance> blocks = multiblock.data.getBlocks(new Vec3i(x, y, z), dir);
                for (BlockInstance structBlock : blocks) {
                    if (structBlock.block == SIBlocks.reinforcedCasing2 || structBlock.block == SIBlocks.awakenedSocketCasing || structBlock.block == SIBlocks.awakenedCasing2) {
                        if (structBlock.pos.getBlockMetadata(worldObj) == 1) {
                            worldObj.setBlockMetadata(structBlock.pos.x, structBlock.pos.y, structBlock.pos.z, 0);
                        }
                    }
                }
            }
        }
    }

    public void work() {
        if (input != null && output != null && multiblock.isValid()) {
            boolean update = false;
            if (fuelBurnTicks > 0) {
                fuelBurnTicks--;
            }
            ArrayList<ItemStack> inputContents = getInputContents();
            if (inputContents.isEmpty()) {
                progressTicks = 0;
            } else if (canProcess()) {
                progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
            }
            if (!worldObj.isClientSide) {
                if (progressTicks == 0 && canProcess() && fuelBurnTicks < 2) {
                    update = fuel();
                }
                if (isBurning() && canProcess()) {
                    progressTicks++;
                    if (progressTicks >= progressMaxTicks) {
                        progressTicks = 0;
                        processItem();
                        update = true;
                    }
                } else if (canProcess()) {
                    fuel();
                    if (fuelBurnTicks > 0) {
                        fuelBurnTicks++;
                    }
                }
            }

            if (update) {
                this.setChanged();
            }
        }
    }

    @Override
    public boolean isBurning() {
        return super.isBurning() && multiblock.isValid();
    }

    @NotNull
    private ArrayList<ItemStack> getInputContents() {
        return Catalyst.condenseItemList(Arrays.asList(input.itemContents));
    }

    private void processItem() {
        if (currentRecipe instanceof RecipeEntryMachineFluid && multiblock.isValid()) {
            RecipeEntryMachineFluid recipe = ((RecipeEntryMachineFluid) currentRecipe);
            FluidStack fluidStack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
            int k = 0;
            if (fluidStack != null) {
                ItemStack inputStack = recipe.getInput()[0].resolve().get(0);
                for (int i = 0; i < inputStack.stackSize; i++) {
                    ItemStack[] contents = input.itemContents;
                    for (int j = 0; j < contents.length; j++) {
                        ItemStack itemContent = contents[j];
                        if (itemContent != null && itemContent.isItemEqual(inputStack)) {
                            itemContent.stackSize--;
                            k++;
                            if (itemContent.stackSize <= 0) {
                                contents[j] = null;
                            }
                            break;
                        }
                    }
                }
                if (k == inputStack.stackSize) {
                    output.insertFluid(0, fluidStack);
                }
            }
        }
    }

    public boolean fuel() {
        int burn = getItemBurnTime(itemContents[0]);
        if (burn > 0 && canProcess()) {
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            if (itemContents[0].getItem().hasContainerItem()) {
                itemContents[0] = new ItemStack(itemContents[0].getItem().getContainerItem());
            } else {
                itemContents[0].stackSize--;
                if (itemContents[0].stackSize == 0) {
                    itemContents[0] = null;
                }
            }
            return true;
        }
        return false;
    }

    private int getItemBurnTime(ItemStack stack) {
        return stack == null ? 0 : LookupFuelFurnace.instance.getFuelYield(stack.getItem().id);
    }

    private boolean canProcess() {
        if (input != null && output != null && multiblock.isValid()) {
            ArrayList<ItemStack> inputContents = getInputContents();
            if (inputContents.isEmpty()) {
                return false;
            }
            FluidStack output = recipeGroup.findFluidOutput(RecipeExtendedSymbol.arrayOf(inputContents), tier);
            return output != null && (this.output.fluidContents[0] == null || (this.output.fluidContents[0].isFluidEqual(output) && this.output.fluidContents[0].amount + output.amount <= this.output.fluidCapacity[0]));
        }
        return false;
    }


    public void setCurrentRecipe() {
        if (input != null && multiblock.isValid()) {
            ArrayList<ItemStack> inputContents = getInputContents();
            currentRecipe = recipeGroup.findRecipe(RecipeExtendedSymbol.arrayOf(inputContents), tier);
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.extractor";
    }
}
