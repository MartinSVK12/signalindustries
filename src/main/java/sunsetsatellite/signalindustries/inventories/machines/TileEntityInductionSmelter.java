package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
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
import sunsetsatellite.signalindustries.inventories.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.inventories.TileEntityItemBus;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;

import java.util.*;

public class TileEntityInductionSmelter extends TileEntityTieredMachineBase implements IMultiblock {

    public Multiblock multiblock;
    public TileEntityItemBus input;
    public TileEntityItemBus output;
    public TileEntityEnergyConnector energy;
    public RecipeGroup<RecipeEntryFurnace> recipeGroup;
    public RecipeEntryFurnace currentRecipe;
    private boolean isValidMultiblock = false;
    private final TickTimer verifyTimer = new TickTimer(this,this::verifyIntegrity,20,true);
    private int ticks = 100;
    public Random random = new Random();
    private int cost = 40;

    public TileEntityInductionSmelter(){
        multiblock = Multiblock.multiblocks.get("basicInductionSmelter");
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        recipeGroup = Registries.RECIPES.FURNACE;
        progressMaxTicks = ticks;
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
        energy = null;
        verifyTimer.tick();
        if(isValidMultiblock){
            ArrayList<BlockInstance> tileEntities = multiblock.getTileEntities(worldObj,new Vec3i(x,y,z), Direction.Z_POS);
            for (BlockInstance tileEntity : tileEntities) {
                if (tileEntity.tile instanceof IMultiblockPart) {
                    if(tileEntity.tile instanceof TileEntityItemBus && tileEntity.block == SIBlocks.basicItemInputBus){
                        input = (TileEntityItemBus) tileEntity.tile;
                    } else if(tileEntity.tile instanceof TileEntityItemBus && tileEntity.block == SIBlocks.basicItemOutputBus){
                        output = (TileEntityItemBus) tileEntity.tile;
                    } else if(tileEntity.tile instanceof TileEntityEnergyConnector && tileEntity.block == SIBlocks.basicEnergyConnector){
                        energy = (TileEntityEnergyConnector) tileEntity.tile;
                    }
                    ((IMultiblockPart) tileEntity.tile).connect(this);
                }
            }
            if(block != null && input != null && output != null && energy != null){
                setCurrentRecipe();
                work();
            }
        }
    }

    public void work(){
        if(input != null && output != null && energy != null && isValidMultiblock){
            boolean update = false;
            if(fuelBurnTicks > 0){
                fuelBurnTicks--;
            }
            ArrayList<ItemStack> inputContents = getInputContents();
            if(inputContents.isEmpty()){
                progressTicks = 0;
            } else if(canProcess()) {
                progressMaxTicks = (int) (ticks / speedMultiplier);
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
        if (currentRecipe instanceof RecipeEntryFurnace && isValidMultiblock && canProcess()) {
            RecipeEntryFurnace recipe = currentRecipe;
            ItemStack stack = recipe.getOutput() == null ? null : recipe.getOutput().copy();
            int parallelAmount = 1;
            int k = 0;
            if (stack != null) {
                ItemStack inputStack = recipe.getInput().resolve().get(0);
                for (int i = 0; i < inputStack.stackSize; i++) {
                    ItemStack[] contents = input.itemContents;
                    for (int j = 0; j < contents.length; j++) {
                        ItemStack itemContent = contents[j];
                        if (itemContent != null && itemContent.isItemEqual(inputStack)) {
                            parallelAmount = Math.min(itemContent.stackSize,16);
                            itemContent.stackSize -= parallelAmount;
                            k++;
                            if (itemContent.stackSize <= 0) {
                                contents[j] = null;
                            }
                            break;
                        }
                    }
                }
                if(k == inputStack.stackSize) {
                    int multiplier = 1;
                    float fraction = Float.parseFloat("0."+(String.valueOf(yield).split("\\.")[1]));
                    if(fraction <= 0) fraction = 1;
                    if(yield > 1 && random.nextFloat() <= fraction){
                        multiplier = (int) Math.ceil(yield);
                    }
                    ItemStack[] itemStacks = this.output.itemContents;
                    for (int i = 0; i < itemStacks.length; i++) {
                        ItemStack itemContent = itemStacks[i];
                        if (itemContent != null && itemContent.isItemEqual(itemContent)) {
                            itemContent.stackSize += ((stack.stackSize * parallelAmount) * multiplier);
                            break;
                        } else if (itemContent == null) {
                            stack.stackSize *= parallelAmount;
                            stack.stackSize *= multiplier;
                            output.setInventorySlotContents(i,stack);
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(energy.fluidContents[0]);
        if(burn > 0 && canProcess() && currentRecipe != null){
            if(energy.fluidContents[0].amount >= cost){
                progressMaxTicks = (int) (ticks / speedMultiplier);
                fuelMaxBurnTicks = fuelBurnTicks = burn;
                energy.fluidContents[0].amount -= cost;
                if (energy.fluidContents[0].amount == 0) {
                    energy.fluidContents[0] = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean canProcess() {
        if(input != null && output != null && isValidMultiblock){
            ArrayList<ItemStack> inputContents = getInputContents();
            if(inputContents.isEmpty()){
                return false;
            }
            List<RecipeEntryFurnace> list = Registries.RECIPES.getAllFurnaceRecipes();
            ItemStack recipeOutput = null;
            label:
            for (RecipeEntryFurnace recipe : list) {
                if(recipe != null){
                    for (ItemStack inputContent : inputContents) {
                        if(recipe.matches(inputContent)){
                            recipeOutput = recipe.getOutput().copy();
                            break label;
                        }
                    }
                }
            }
            if(recipeOutput == null){
                return false;
            }
            if(Arrays.stream(this.output.itemContents).noneMatch(Objects::nonNull)){
                return true;
            }
            boolean can = false;
            for (ItemStack outputStack : this.output.itemContents) {
                if(outputStack != null && outputStack.isItemEqual(recipeOutput)){
                    int parallelAmount = Math.min(outputStack.stackSize,8);
                    recipeOutput.stackSize *= parallelAmount;
                    if(yield > 1){
                        int n = recipeOutput.stackSize+(outputStack.stackSize*((int) Math.ceil(yield)));
                        if (((n <= getInventoryStackLimit()) || n <= recipeOutput.getMaxStackSize()) && n <= outputStack.getMaxStackSize()) {
                            can = true;
                            break;
                        }
                    } else {
                        int n = recipeOutput.stackSize + outputStack.stackSize;
                        if (((n <= getInventoryStackLimit()) || n <= recipeOutput.getMaxStackSize()) && n <= outputStack.getMaxStackSize()) {
                            can = true;
                            break;
                        }
                    }
                } else if(outputStack == null){
                    can = true;
                    break;
                }
            }
            return can;
        }
        return false;
    }


    public void setCurrentRecipe(){
        if(input != null && isValidMultiblock){
            ArrayList<ItemStack> inputContents = getInputContents();
            List<RecipeEntryFurnace> list = Registries.RECIPES.getAllFurnaceRecipes();
            for (RecipeEntryFurnace recipe : list) {
                if(recipe != null){
                    for (ItemStack inputContent : inputContents) {
                        if(recipe.matches(inputContent)){
                            currentRecipe = recipe;
                            return;
                        }
                    }
                }
            }
        }
    }
}
