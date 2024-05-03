package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class TileEntityRecipeMaker extends TileEntity
    implements IInventory
{
    public String currentRecipeName = "";
    public boolean shaped = true;
    public boolean useContainers = false;

    public TileEntityRecipeMaker() {
        contents = new ItemStack[10];
    }

    public int getSizeInventory()
    {
        return contents.length;
    }

    public boolean isEmpty() {
        for(int i = 0; i < getSizeInventory()-1; i++) {
            if(getStackInSlot(i) != null) {
                return false;
            } else
            {
                continue;
            }
        }
        return true;
    }

    public ItemStack getStackInSlot(int i)
    {
        return contents[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(contents[i] != null)
        {
            if(contents[i].stackSize <= j)
            {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if(contents[i].stackSize == 0)
            {
                contents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Recipe Maker";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public void readFromNBT(CompoundTag nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(CompoundTag nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte)i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.addTag(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
    }

    @Override
    public void sortInventory() {

    }

    private ItemStack[] contents;

    public void makeRecipe() {
        if(shaped){
            int j = 0;
            List<String> strings = new ArrayList<>();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                ItemStack stack = getStackInSlot(i);
                if(stack != null){
                    s.append(i);
                }
                j++;
                if(j % 3 == 0 && s.length() > 0){
                    strings.add(s.toString());
                    s = new StringBuilder();
                }
            }
            List<Object> list = new ArrayList<>(strings);
            for (int i = 0; i < 9; i++) {
                ItemStack stack = getStackInSlot(i);
                if (stack != null) {
                    list.add((String.valueOf(i).charAt(0)));
                    list.add(stack.copy());
                }
            }
            if(!strings.isEmpty() && !Objects.equals(currentRecipeName, "")){
                RecipeEntryCraftingShaped recipe = parseRecipe(getStackInSlot(9),useContainers,list.toArray());
                try {
                    Registries.RECIPES.addCustomRecipe(currentRecipeName,recipe);
                    Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Recipe created!");
                    SignalIndustries.LOGGER.info(DataLoader.serializeRecipe(recipe));
                } catch (IllegalArgumentException e) {
                    Minecraft.getMinecraft(this).ingameGUI.addChatMessage(TextFormatting.RED+e.getMessage());
                }
            }
        }
    }

    public static RecipeEntryCraftingShaped parseRecipe(ItemStack itemstack, boolean consumeContainerItem, Object... aobj) {
        StringBuilder s = new StringBuilder();
        int i = 0;
        int j = 0;
        int k = 0;
        int i1;
        if (aobj[i] instanceof String[]) {
            String[] as = (String[])aobj[i++];
            i1 = as.length;

            for(int var10 = 0; var10 < i1; ++var10) {
                String s2 = as[var10];
                ++k;
                j = s2.length();
                s.append(s2);
            }
        } else {
            while(aobj[i] instanceof String) {
                String s1 = (String)aobj[i++];
                ++k;
                j = s1.length();
                s = new StringBuilder(s + s1);
            }
        }

        HashMap<Character,RecipeSymbol> map;
        for(map = new HashMap<>(); i < aobj.length; i += 2) {
            Character character = (Character)aobj[i];
            RecipeSymbol recipeSymbol = null;
            if (aobj[i + 1] instanceof Item) {
                recipeSymbol = new RecipeSymbol(character, new ItemStack((Item)aobj[i + 1]));
            } else if (aobj[i + 1] instanceof Block) {
                recipeSymbol = new RecipeSymbol(character, new ItemStack((Block)aobj[i + 1]));
            } else if (aobj[i + 1] instanceof ItemStack) {
                recipeSymbol = new RecipeSymbol(character, (ItemStack)aobj[i + 1]);
            }

            if (aobj[i + 1] instanceof String) {
                recipeSymbol = new RecipeSymbol(character, null, (String)aobj[i + 1]);
            }

            if (aobj[i + 1] instanceof RecipeSymbol) {
                recipeSymbol = (RecipeSymbol)aobj[i + 1];
            }

            map.put(character, recipeSymbol);
        }

        RecipeSymbol[] symbols = new RecipeSymbol[j * k];

        for(i1 = 0; i1 < j * k; ++i1) {
            char c = s.charAt(i1);
            if (map.containsKey(c)) {
                symbols[i1] = map.get(c).copy();
            } else {
                symbols[i1] = null;
            }
        }

        return new RecipeEntryCraftingShaped(j, k, symbols, itemstack, consumeContainerItem);
    }

    public void deleteContents() {
        contents = new ItemStack[10];
    }

    public static String getItemFieldName(Item item){
        try{
            ArrayList<Field> fields = new ArrayList<>(Arrays.asList(Item.class.getDeclaredFields()));
            //fields.addAll(Arrays.asList(mod_RetroStorage.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Item.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Item fieldItem = (Item) field.get(null);
                    if(fieldItem.equals(item)){
                        return "Item."+field.getName();
                    }
                }
            }
            fields = new ArrayList<>(Arrays.asList(SignalIndustries.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Item.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Item fieldItem = (Item) field.get(null);
                    if(fieldItem.equals(item)){
                        return "SignalIndustries."+field.getName();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getBlockFieldName(Block item){
        try{
            ArrayList<Field> fields = new ArrayList<>(Arrays.asList(Block.class.getDeclaredFields()));
            //fields.addAll(Arrays.asList(mod_RetroStorage.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Block.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Block fieldItem = (Block) field.get(null);
                    if(fieldItem.equals(item)){
                        return "Block."+field.getName();
                    }
                }
            }
            fields = new ArrayList<>(Arrays.asList(SignalIndustries.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Block.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Block fieldItem = (Block) field.get(null);
                    if(fieldItem.equals(item)){
                        return "SignalIndustries."+field.getName();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
