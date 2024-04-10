package sunsetsatellite.signalindustries.inventories.machines;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingDynamic;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IFluidIO;
import sunsetsatellite.catalyst.core.util.IItemIO;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerAssembler;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.TileEntityExternalIO;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;

import java.util.*;
import java.util.stream.Collectors;

public class TileEntityAssembler extends TileEntityTieredMachineBase implements IBoostable {

    public final AssemblerTemplate template = new AssemblerTemplate();
    public RecipeEntryCrafting<?,ItemStack> recipe;

    public TileEntityAssembler() {
        itemContents = new ItemStack[19];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }

    @Override
    public String getInvName() {
        return "Assembler";
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            work();
        }
    }

    @Override
    public void applyModifiers() {
        super.applyModifiers();
        yield = 1;
        if(speedMultiplier > 1){
            speedMultiplier *= 1.25f;
        }
    }

    private void work() {
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(!canProcess()){
            progressTicks = 0;
        } else
        if(canProcess()) {
            progressMaxTicks = (int) (90 / speedMultiplier);
        }
        if(!worldObj.isClientSide){
            if (progressTicks == 0 && canProcess()){
                update = fuel();
            }
            if(isBurning() && canProcess()){
                progressTicks++;
                if(progressTicks >= progressMaxTicks){
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if(canProcess()){
                fuel();
                if(fuelBurnTicks > 0){
                    fuelBurnTicks++;
                }
            }
        }

        if(update) {
            this.onInventoryChanged();
        }
    }

    public void processItem() {
        if(!canProcess()) return;
        if(recipe == null) return;
        ArrayList<ItemStack> condensedInv = SignalIndustries.condenseList(Arrays.asList(itemContents));
        ArrayList<RecipeSymbol> recipeInputs = new ArrayList<>();
        if(recipe.getInput() instanceof RecipeSymbol[]){
            recipeInputs = new ArrayList<>(Arrays.asList(((RecipeSymbol[]) recipe.getInput())));
        } else if (recipe.getInput() instanceof List) {
            recipeInputs = (ArrayList<RecipeSymbol>) recipe.getInput();
        }
        if(recipeInputs.isEmpty()) return;
        recipeInputs.removeIf(Objects::isNull);
        if(!(SignalIndustries.hasItems(recipeInputs,condensedInv))){
            return;
        }
        int s = 0;
        int sReq = (int) recipeInputs.stream().filter(Objects::nonNull).count();
        label:
        for (RecipeSymbol symbol : recipeInputs) {
            for (ItemStack stack : itemContents) {
                if(symbol.matches(stack)){
                    if(stack == null || stack.stackSize <= 0) continue;
                    stack.stackSize--;
                    if(stack.getItem().hasContainerItem()){
                        ItemStack container = new ItemStack(stack.getItem().getContainerItem());
                        boolean added = false;
                        for (int i = 1; i < itemContents.length; i++) {
                            ItemStack content = itemContents[i];
                            if (content == null) {
                                setInventorySlotContents(i, container);
                                added = true;
                                break;
                            } else if (content.isItemEqual(container) && content.stackSize < content.getMaxStackSize()) {
                                itemContents[i].stackSize++;
                                added = true;
                                break;
                            }
                        }
                        if(!added){
                            EntityItem entityitem = new EntityItem(worldObj, (float) x, (float)y+1, (float)z, container);
                            worldObj.entityJoinedWorld(entityitem);
                        }
                    }
                    s++;
                    continue label;
                }
            }
        }
        itemContents = Arrays.stream(itemContents).filter((S)->S == null || S.stackSize > 0).toArray((A)-> new ItemStack[19]);
        if(s == sReq){
            final int multiplier = 1;
            /*float fraction = Float.parseFloat("0."+(String.valueOf(yield).split("\\.")[1]));
            if(fraction <= 0) fraction = 1;
            if(yield > 1 && Math.random() <= fraction){
                multiplier = (int) Math.ceil(yield);
            }*/
            ItemStack output = recipe.getOutput().copy();
            if(itemContents[0] != null && (!(itemContents[0].isItemEqual(output)) || itemContents[0].stackSize+(output.stackSize*multiplier) > itemContents[0].getMaxStackSize())) return;
            if(itemContents[0] == null){
                output.stackSize *= multiplier;
                itemContents[0] = output;
            } else {
                itemContents[0].stackSize += (output.stackSize * multiplier);
            }
        }
    }

    public boolean canProcess(){
        if(recipe == null) return false;
        final int multiplier = 1;
        /*float fraction = Float.parseFloat("0."+(String.valueOf(yield).split("\\.")[1]));
        if(fraction <= 0) fraction = 1;
        if(yield > 1 && Math.random() <= fraction){
            multiplier = (int) Math.ceil(yield);
        }*/
        ItemStack output = recipe.getOutput().copy();
        if(itemContents[0] != null && (!(itemContents[0].isItemEqual(output)) || itemContents[0].stackSize+(output.stackSize*multiplier) > itemContents[0].getMaxStackSize())) return false;
        ArrayList<ItemStack> condensedInv = SignalIndustries.condenseList(Arrays.asList(itemContents));
        ArrayList<RecipeSymbol> recipeInputs = new ArrayList<>();
        if(recipe.getInput() instanceof RecipeSymbol[]){
           recipeInputs = new ArrayList<>(Arrays.asList(((RecipeSymbol[]) recipe.getInput())));
        } else if (recipe.getInput() instanceof List) {
            recipeInputs = (ArrayList<RecipeSymbol>) recipe.getInput();
        }
        if(recipeInputs.isEmpty()) return false;
        recipeInputs.removeIf(Objects::isNull);
        return SignalIndustries.hasItems(recipeInputs,condensedInv);
    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if(burn > 0 && canProcess() && recipe != null){
            if(fluidContents[0].amount >= 100){
                progressMaxTicks = (int) (90 / speedMultiplier);
                fuelMaxBurnTicks = fuelBurnTicks = burn;
                fluidContents[0].amount -= 100;
                if (fluidContents[0].amount == 0) {
                    fluidContents[0] = null;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        CompoundTag templateTag = new CompoundTag();
        template.writeToNBT(templateTag);
        nBTTagCompound1.put("Template", templateTag);
    }

    @Override
    public void readFromNBT(CompoundTag nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        template.readFromNBT(nBTTagCompound1.getCompound("Template"));
        template.onInventoryChanged();
    }

    @Override
    public int getActiveItemSlotForSide(Direction dir, ItemStack stack) {
        if(activeItemSlots.get(dir) == -1) {
            if (itemConnections.get(dir) == Connection.INPUT) {
                for (int i = 1; i < itemContents.length; i++) {
                    ItemStack content = itemContents[i];
                    if (content == null || (content.isItemEqual(stack) && content.stackSize+stack.stackSize <= content.getMaxStackSize())) {
                        return i;
                    }
                }
            }
        }
        return super.getActiveItemSlotForSide(dir,stack);
    }

    //extending inventorycrafting is a dirty hack lmao
    public class AssemblerTemplate extends InventoryCrafting implements IInventory {

        public ItemStack[] itemContents = new ItemStack[9];

        public AssemblerTemplate() {
            super(null, 3, 3);
        }

        @Override
        public int getSizeInventory() {
            return itemContents.length;
        }

        @Override
        public ItemStack getStackInSlot(int i1) {
            return this.itemContents[i1];
        }

        @Override
        public ItemStack decrStackSize(int i1, int i2) {
            if(this.itemContents[i1] != null) {
                ItemStack itemStack3;
                if(this.itemContents[i1].stackSize <= i2) {
                    itemStack3 = this.itemContents[i1];
                    this.itemContents[i1] = null;
                    this.onInventoryChanged();
                    return itemStack3;
                } else {
                    itemStack3 = this.itemContents[i1].splitStack(i2);
                    if(this.itemContents[i1].stackSize == 0) {
                        this.itemContents[i1] = null;
                    }

                    this.onInventoryChanged();
                    return itemStack3;
                }
            } else {
                return null;
            }
        }

        @Override
        public void setInventorySlotContents(int i1, ItemStack itemStack2) {
            this.itemContents[i1] = itemStack2;
            if(itemStack2 != null && itemStack2.stackSize > this.getInventoryStackLimit()) {
                itemStack2.stackSize = this.getInventoryStackLimit();
            }

            this.onInventoryChanged();
        }

        @Override
        public String getInvName() {
            return "Assembler Template Grid";
        }

        public void readFromNBT(CompoundTag CompoundTag1) {
            ListTag nBTTagList2 = CompoundTag1.getList("Items");
            this.itemContents = new ItemStack[this.getSizeInventory()];

            for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
                CompoundTag CompoundTag4 = (CompoundTag)nBTTagList2.tagAt(i3);
                int i5 = CompoundTag4.getByte("Slot") & 255;
                if(i5 >= 0 && i5 < this.itemContents.length) {
                    this.itemContents[i5] = ItemStack.readItemStackFromNbt(CompoundTag4);
                }
            }
        }

        public void writeToNBT(CompoundTag CompoundTag1) {
            ListTag nbtTagList = new ListTag();

            for(int i3 = 0; i3 < this.itemContents.length; ++i3) {
                if(this.itemContents[i3] != null) {
                    CompoundTag CompoundTag4 = new CompoundTag();
                    CompoundTag4.putByte("Slot", (byte)i3);
                    this.itemContents[i3].writeToNBT(CompoundTag4);
                    nbtTagList.addTag(CompoundTag4);
                }
            }

            CompoundTag1.put("Items", nbtTagList);
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public void onInventoryChanged() {
            TileEntityAssembler.this.recipe = findMatchingRecipe(this);
        }

        @Override
        public boolean canInteractWith(EntityPlayer entityPlayer1) {
            return true;
        }

        @Override
        public void sortInventory() {

        }

        public RecipeEntryCrafting<?,ItemStack> findMatchingRecipe(AssemblerTemplate template){
            for(int i = 0; i < Registries.RECIPES.getAllCraftingRecipes().size(); i++)
            {
                RecipeEntryCrafting<?,?> recipe =  Registries.RECIPES.getAllCraftingRecipes().get(i);
                if(recipe.matches(template) && !(recipe instanceof RecipeEntryCraftingDynamic))
                {
                    return (RecipeEntryCrafting<?, ItemStack>) recipe;
                }
            }

            return null;
        }
    }
}
