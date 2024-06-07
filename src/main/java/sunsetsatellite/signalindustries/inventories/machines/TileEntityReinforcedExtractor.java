package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.inventories.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.inventories.TileEntityItemBus;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.SIRecipes;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.*;

public class TileEntityReinforcedExtractor extends TileEntityTieredMachineBase implements IMultiblock {

    public Multiblock multiblock;
    public TileEntityItemBus input;
    public TileEntityFluidHatch output;
    public RecipeGroupSI<?> recipeGroup;
    public RecipeEntrySI<?,?, RecipeProperties> currentRecipe;
    private boolean isValidMultiblock = false;
    private final TickTimer verifyTimer = new TickTimer(this,this::verifyIntegrity,20,true);
    public TileEntityReinforcedExtractor(){
        multiblock = Multiblock.multiblocks.get("extractionManifold");
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        recipeGroup = SIRecipes.EXTRACTOR;
    }

    @Override
    public Multiblock getMultiblock() {
        return multiblock;
    }

    public void verifyIntegrity(){
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            isValidMultiblock = multiblock.isValidAtSilent(worldObj,new BlockInstance(block,new Vec3i(x,y,z),this),Direction.getDirectionFromSide(worldObj.getBlockMetadata(x,y,z)));
        } else {
            isValidMultiblock = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        input = null;
        output = null;
        verifyTimer.tick();
        if(isValidMultiblock){
            ArrayList<BlockInstance> tileEntities = multiblock.getTileEntities(worldObj,new Vec3i(x,y,z), Direction.Z_POS);
            for (BlockInstance tileEntity : tileEntities) {
                if (tileEntity.tile instanceof IMultiblockPart) {
                    if(tileEntity.tile instanceof TileEntityItemBus && tileEntity.block == SIBlocks.reinforcedItemInputBus){
                        input = (TileEntityItemBus) tileEntity.tile;
                    } else if(tileEntity.tile instanceof TileEntityFluidHatch && tileEntity.block == SIBlocks.reinforcedFluidOutputHatch){
                        output = (TileEntityFluidHatch) tileEntity.tile;
                    }
                    ((IMultiblockPart) tileEntity.tile).connect(this);
                }
            }
            if(block != null && input != null && output != null){
                setCurrentRecipe();
                work();
            }
        }
    }

    public void work(){
        if(input != null && output != null && isValidMultiblock){
            boolean update = false;
            if(fuelBurnTicks > 0){
                fuelBurnTicks--;
            }
            ArrayList<ItemStack> inputContents = getInputContents();
            if(inputContents.isEmpty()){
                progressTicks = 0;
            } else if(canProcess()) {
                progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
            }
            if(!worldObj.isClientSide) {
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

            if(update) {
                this.onInventoryChanged();
            }
        }
    }

    @Override
    public boolean isBurning() {
        return super.isBurning() && isValidMultiblock;
    }

    @NotNull
    private ArrayList<ItemStack> getInputContents() {
        return SignalIndustries.condenseList(Arrays.asList(input.itemContents));
    }

    private void processItem() {
        if (currentRecipe instanceof RecipeEntryMachineFluid && isValidMultiblock) {
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
                if(k == inputStack.stackSize) {
                    output.insertFluid(0,fluidStack);
                }
            }
        }
    }

    public boolean fuel(){
        int burn = getItemBurnTime(itemContents[0]);
        if(burn > 0 && canProcess()){
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            if(itemContents[0].getItem().hasContainerItem()) {
                itemContents[0] = new ItemStack(itemContents[0].getItem().getContainerItem());
            } else {
                itemContents[0].stackSize--;
                if(itemContents[0].stackSize == 0) {
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
        if(input != null && output != null && isValidMultiblock){
            ArrayList<ItemStack> inputContents = getInputContents();
            if(inputContents.isEmpty()){
                return false;
            }
            FluidStack output = recipeGroup.findFluidOutput(RecipeExtendedSymbol.arrayOf(inputContents),tier);
            return output != null && (this.output.fluidContents[0] == null || (this.output.fluidContents[0].isFluidEqual(output) && this.output.fluidContents[0].amount + output.amount <= this.output.fluidCapacity[0]));
        }
        return false;
    }


    public void setCurrentRecipe(){
        if(input != null && isValidMultiblock){
            ArrayList<ItemStack> inputContents = getInputContents();
            currentRecipe = recipeGroup.findRecipe(RecipeExtendedSymbol.arrayOf(inputContents),tier);
        }
    }
}
