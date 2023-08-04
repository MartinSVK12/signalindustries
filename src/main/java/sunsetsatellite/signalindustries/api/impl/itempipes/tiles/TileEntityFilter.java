package sunsetsatellite.signalindustries.api.impl.itempipes.tiles;


import sunsetsatellite.signalindustries.api.impl.itempipes.misc.IItemConnection;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.*;

public class TileEntityFilter extends TileEntity implements IItemConnection, IInventory {

    public ArrayList<Map.Entry<String,Direction>> sides = new ArrayList<>();
    private final Random random = new Random(System.nanoTime());

    public TileEntityFilter(){
        sides.add(new AbstractMap.SimpleEntry<>("RED",Direction.getFromName("UP")));
        sides.add(new AbstractMap.SimpleEntry<>("GREEN",Direction.getFromName("DOWN")));
        sides.add(new AbstractMap.SimpleEntry<>("BLUE",Direction.getFromName("WEST")));
        sides.add(new AbstractMap.SimpleEntry<>("CYAN",Direction.getFromName("NORTH")));
        sides.add(new AbstractMap.SimpleEntry<>("MAGENTA",Direction.getFromName("EAST")));
        sides.add(new AbstractMap.SimpleEntry<>("YELLOW",Direction.getFromName("SOUTH")));
    }
    public void filter(ItemStack stack, Direction inDir){
        Map.Entry<String, Direction> side = getFilterColor(stack);
        Direction outDir;
        if(!Objects.equals(side.getKey(), "UNKNOWN")){
            outDir = side.getValue();
        } else {
            outDir = inDir.getOpposite();
        }
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IInventory.class);
        tiles.add(IItemConnection.class);
        HashMap<Direction,TileEntity> connectedTiles = getConnectedTileEntities(tiles);
        TileEntity sender = inDir.getTileEntity(worldObj,this);
        connectedTiles.replace(inDir,sender,null);
        connectedTiles.keySet().removeIf((D)->connectedTiles.get(D) == null);
        if(connectedTiles.size() == 0 || !connectedTiles.containsKey(outDir)){
            dropItem(stack);
        } else {
            TileEntity tile = outDir.getTileEntity(worldObj,this);
            if(tile instanceof TileEntityItemPipe) {
                ((TileEntityItemPipe) tile).addItem(stack, outDir.getOpposite());
            } else if (tile instanceof TileEntityFilter){
                ((TileEntityFilter) tile).filter(stack, outDir.getOpposite());
            } else if (tile instanceof IInventory) {
                IInventory inv = (IInventory) tile;
                boolean s = false;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    ItemStack stack2 = inv.getStackInSlot(i);
                    if(stack2 == null){
                        inv.setInventorySlotContents(i,stack);
                        s = true;
                        break;
                    } else if (stack2.isItemEqual(stack) && ((INBTCompound)stack2.tag).equals(stack.tag) && 64-stack2.stackSize >= stack.stackSize) {
                        stack2.stackSize += stack.stackSize;
                        s = true;
                        break;
                    }
                }
                if(!s){
                    dropItem(stack);
                }
            } else {
                dropItem(stack);
            }
        }
    }

    public void dropItem(ItemStack stack){
        float f = random.nextFloat() * 0.8F + 0.1F;
        float f1 = random.nextFloat() * 0.8F + 0.1F;
        float f2 = random.nextFloat() * 0.8F + 0.1F;
        EntityItem entityitem = new EntityItem(worldObj, (float) xCoord + f, (float) yCoord + f1, (float) zCoord + f2, stack);
        float f3 = 0.05F;
        entityitem.motionX = (float) random.nextGaussian() * f3;
        entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
        entityitem.motionZ = (float) random.nextGaussian() * f3;
        worldObj.entityJoinedWorld(entityitem);
    }

    public HashMap<Direction,TileEntity> getConnectedTileEntities(List<Class<?>> allowedTileList){
        HashMap<Direction, TileEntity> sides = new HashMap<>();
        for (Direction dir : Direction.values()) {
            sides.put(dir,null);
        }

        for (Direction dir : Direction.values()) {
            Vec3i V = dir.getVec();
            TileEntity tile = worldObj.getBlockTileEntity(xCoord+V.x, yCoord+V.y, zCoord+V.z);
            if(tile != null){
                if(allowedTileList.stream().anyMatch((T) -> T.isAssignableFrom(tile.getClass()))){
                    sides.put(dir,tile);
                }
            }
        }

        return sides;
    }

    public Map.Entry<String, Direction> getFilterColor(ItemStack stack) {
        for(int i2 = 0; i2 < this.itemContents.length; ++i2) {
            if(this.itemContents[i2] != null && this.itemContents[i2].itemID == stack.itemID && this.itemContents[i2].getMetadata() == stack.getMetadata()) {
                return sides.get(i2/9);
            }
        }

        return new AbstractMap.SimpleEntry<>("UNKNOWN",null);
    }

    protected ItemStack[] itemContents = new ItemStack[9*6];

    public int getSizeInventory() {
        return itemContents.length;
    }

    public ItemStack getStackInSlot(int i1) {
        return this.itemContents[i1];
    }

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

    public void setInventorySlotContents(int i1, ItemStack itemStack2) {
        this.itemContents[i1] = itemStack2;
        if(itemStack2 != null && itemStack2.stackSize > this.getInventoryStackLimit()) {
            itemStack2.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    public String getInvName() {
        return "Filter";
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("Items");
        this.itemContents = new ItemStack[this.getSizeInventory()];

        for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
            NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3);
            int i5 = nBTTagCompound4.getByte("Slot") & 255;
            if(i5 < this.itemContents.length) {
                this.itemContents[i5] = new ItemStack(nBTTagCompound4);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        NBTTagList nbtTagList = new NBTTagList();

        for(int i3 = 0; i3 < this.itemContents.length; ++i3) {
            if(this.itemContents[i3] != null) {
                NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
                nBTTagCompound4.setByte("Slot", (byte)i3);
                this.itemContents[i3].writeToNBT(nBTTagCompound4);
                nbtTagList.setTag(nBTTagCompound4);
            }
        }
        nBTTagCompound1.setTag("Items", nbtTagList);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityPlayer1) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && entityPlayer1.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

}
