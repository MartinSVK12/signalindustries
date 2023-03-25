package sunsetsatellite.signalindustries.tiles;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class TileEntityRecipeMaker extends TileEntity
    implements IInventory
{
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

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = new ItemStack(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private ItemStack[] contents;

    public void makeRecipe() {
        StringBuilder s = new StringBuilder("RecipeHelper.Crafting.createRecipe(");
        ItemStack output = getStackInSlot(9);
        Object item;

        if(output.itemID > 16384){
            item = output.getItem();
            s.append(getItemFieldName((Item) item));
            //items
        } else {
            item = Block.blocksList[output.itemID];
            s.append(getBlockFieldName((Block) item));
            //blocks
        }
        s.append(", ").append(output.stackSize).append(", new Object[]{\"012\",\"345\",\"678\"");
        for(int i = 0;i<9;i++){
            ItemStack stack = getStackInSlot(i);
            if(stack == null){
                continue;
            }
            Object inputItem;
            if(stack.itemID > 16384){
                inputItem = stack.getItem();
                s.append(",'").append(i).append("',").append("new ItemStack(").append(getItemFieldName((Item) inputItem)).append(",1,").append(stack.getMetadata()).append(")");
                //items
            } else {
                inputItem = Block.blocksList[stack.itemID];
                s.append(",'").append(i).append("',").append("new ItemStack(").append(getBlockFieldName((Block) inputItem)).append(",1,").append(stack.getMetadata()).append(")");
                //blocks
            }

        }

        s.append("});");
        SignalIndustries.LOGGER.info(s.toString());
    }

    public void deleteContents() {
        contents = new ItemStack[10];
    }

    private String getItemFieldName(Item item){
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

    private String getBlockFieldName(Block item){
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
