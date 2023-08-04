package sunsetsatellite.signalindustries.util;


import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IFluidInventory;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;

public class NBTHelper {

    public static void saveInvToNBT(ItemStack source_item, IInventory inv){
        NBTTagCompound itemData = source_item.tag.getCompoundTag("inventory");
        for(int i = 0; i < inv.getSizeInventory();i++){
            ItemStack item = inv.getStackInSlot(i);
            NBTTagCompound itemNBT = new NBTTagCompound();
            if(item != null){
                item.writeToNBT(itemNBT);
                itemData.setCompoundTag(String.valueOf(i),itemNBT);
            } else {
                ((INBTCompound)itemData).removeTag(String.valueOf(i));
            }
        }
        source_item.tag.setCompoundTag("inventory",itemData);
        if(inv instanceof IFluidInventory){
            IFluidInventory fluidInv = (IFluidInventory) inv;
            NBTTagCompound fluidData = source_item.tag.getCompoundTag("fluidInventory");
            for(int i = 0; i < fluidInv.getFluidInventorySize();i++){
                FluidStack fluid = ((IFluidInventory) inv).getFluidInSlot(i);
                NBTTagCompound fluidNBT = new NBTTagCompound();
                if(fluid != null){
                    fluid.writeToNBT(fluidNBT);
                    fluidData.setCompoundTag(String.valueOf(i),fluidNBT);
                } else {
                    ((INBTCompound)fluidData).removeTag(String.valueOf(i));
                }
            }
            source_item.tag.setCompoundTag("fluidInventory",fluidData);
        }
    }

    /*public static NBTTagCompound savePureNBT(NBTTagCompound nbt, IInventory inv){
        for(int i = 0; i < inv.getSizeInventory();i++){
            ItemStack item = inv.getStackInSlot(i);
            NBTTagCompound itemNBT = new NBTTagCompound();
            if(item != null){
                itemNBT.setByte("Count", (byte)item.stackSize);
                itemNBT.setShort("id", (short)item.itemID);
                itemNBT.setShort("Damage", (short)item.getMetadata());
                itemNBT.setCompoundTag("Data", (NBTTagCompound)item.tag);
                nbt.setCompoundTag(String.valueOf(i),itemNBT);
            } else {
                ((INBTCompound)nbt).removeTag(String.valueOf(i));
            }
        }
        return nbt;
    }*/

    public static void loadInvFromNBT(ItemStack source_item, IInventory inv, int amount, int fluidAmount){
        NBTTagCompound itemNBT = source_item.tag.getCompoundTag("inventory");
        NBTTagCompound fluidNBT = source_item.tag.getCompoundTag("fluidInventory");
        for(int i = 0; i < amount;i++){
            if(itemNBT.hasKey(String.valueOf(i))){
                ItemStack item = new ItemStack(itemNBT.getCompoundTag(String.valueOf(i)));
                inv.setInventorySlotContents(i,item);
            }
        }
        for (int i = 0; i < fluidAmount; i++) {
            if(inv instanceof IFluidInventory){
                IFluidInventory fluidInv = (IFluidInventory) inv;
                FluidStack fluid = new FluidStack(fluidNBT.getCompoundTag(String.valueOf(i)));
                fluidInv.setFluidInSlot(i,fluid);
            }
        }
    }

    /*public static void loadPureNBT(NBTTagCompound nbt, IInventory inv, int amount){
        for(int i = 0; i < amount;i++){
            if(nbt.hasKey(String.valueOf(i))){
                ItemStack item = new ItemStack(nbt.getCompoundTag(String.valueOf(i)));
                inv.setInventorySlotContents(i,item);
            }
        }
    }*/

    /*public static NBTBase loadNBTFile(String name) {
        try {
            RandomAccessFile file = new RandomAccessFile(name, "r");
            NBTBase nbt = NBTBase.readTag(file);
            return nbt;
        } catch(IOException e){
            e.printStackTrace();
        }
        return new NBTTagCompound();
    }

    public static void saveNBTFile(String name, NBTBase nbt) {
        try {
            RandomAccessFile file = new RandomAccessFile(name, "rw");
            NBTBase.writeTag(nbt, file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }*/
}
