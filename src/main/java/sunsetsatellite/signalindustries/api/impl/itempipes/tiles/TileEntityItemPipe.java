package sunsetsatellite.signalindustries.api.impl.itempipes.tiles;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.EntityPipeItem;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.IItemConnection;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.PipeItem;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3f;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.*;

public class TileEntityItemPipe extends TileEntity implements IItemConnection {

    public ArrayList<PipeItem> items = new ArrayList<>();
    private final Random random = new Random(System.nanoTime());
    public boolean roundRobin = true;
    public int roundRobinN = 0;
    public double speed = 0.02;

    public TileEntityItemPipe(){
        super();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        moveItems();
    }

    public void moveItems(){
        for (PipeItem item : items) {
            if(item.goingToCenter){
                if(item.offset.distanceTo(new Vec3f(0.5f,0.5f,0.5f)) <= 0.02){
                    item.goingToCenter = false;
                    continue;
                }
                switch (item.inDir){
                    case X_POS:
                        item.offset.x -= speed;
                        break;
                    case X_NEG:
                        item.offset.x += speed;
                        break;
                    case Y_POS:
                        item.offset.y -= speed;
                        break;
                    case Y_NEG:
                        item.offset.y += speed;
                        break;
                    case Z_POS:
                        item.offset.z -= speed;
                        break;
                    case Z_NEG:
                        item.offset.z += speed;
                        break;
                }
            } else if (!item.atEnd) {
                Vec3f vec = item.outDir.getVecF().divide(2).add(0.5);
                if(item.offset.distanceTo(vec) <= 0.02){
                    item.atEnd = true;
                    TileEntity tile = item.outDir.getTileEntity(worldObj,this);
                    if(tile instanceof TileEntityItemPipe) {
                        ((TileEntityItemPipe) tile).addItem(item.entity.item, item.outDir.getOpposite());
                    } else if (tile instanceof TileEntityFilter) {
                        ((TileEntityFilter) tile).filter(item.entity.item, item.outDir.getOpposite());
                    } else if (tile instanceof TileEntityFluidItemContainer){
                        Direction tileIn = item.outDir.getOpposite();
                        Integer activeSlot = ((TileEntityFluidItemContainer) tile).activeItemSlots.get(tileIn);
                        Connection connection = ((TileEntityFluidItemContainer) tile).itemConnections.get(tileIn);
                        if(connection == Connection.INPUT || connection == Connection.BOTH){
                            ItemStack stack = ((TileEntityFluidItemContainer) tile).getStackInSlot(activeSlot);
                            if(stack == null){
                                ((TileEntityFluidItemContainer) tile).setInventorySlotContents(activeSlot,item.entity.item);
                                item.entity.setEntityDead();
                                break;
                            } else if (stack.isItemEqual(item.entity.item) && ((INBTCompound)stack.tag).equals(item.entity.item.tag) && 64-stack.stackSize >= item.entity.item.stackSize) {
                                stack.stackSize += item.entity.item.stackSize;
                                item.entity.setEntityDead();
                                break;
                            } else {
                                item.entity.setEntityDead();
                                dropItem(item);
                            }
                        } else {
                            item.entity.setEntityDead();
                            dropItem(item);
                        }

                    } else if (tile instanceof IInventory) {
                        IInventory inv = (IInventory) tile;
                        for (int i = 0; i < inv.getSizeInventory(); i++) {
                            ItemStack stack = inv.getStackInSlot(i);
                            if(stack == null){
                                inv.setInventorySlotContents(i,item.entity.item);
                                item.entity.setEntityDead();
                                break;
                            } else if (stack.isItemEqual(item.entity.item) && ((INBTCompound)stack.tag).equals(item.entity.item.tag) && 64-stack.stackSize >= item.entity.item.stackSize) {
                                stack.stackSize += item.entity.item.stackSize;
                                item.entity.setEntityDead();
                                break;
                            }
                        }
                        if(item.entity.isEntityAlive()){ 
                            item.entity.setEntityDead();
                            dropItem(item);
                        }
                    } else {
                        dropItem(item);
                    }
                    item.entity.setEntityDead();
                    continue;
                }
                switch (item.outDir){
                    case X_POS:
                        item.offset.x += speed;
                        break;
                    case X_NEG:
                        item.offset.x -= speed;
                        break;
                    case Y_POS:
                        item.offset.y += speed;
                        break;
                    case Y_NEG:
                        item.offset.y -= speed;
                        break;
                    case Z_POS:
                        item.offset.z += speed;
                        break;
                    case Z_NEG:
                        item.offset.z -= speed;
                        break;
                }
            }
            item.entity.setPosition(xCoord+item.offset.x,yCoord+item.offset.y,zCoord+item.offset.z);
            if(!items.isEmpty()){
                //SignalIndustries.LOGGER.info(item.offset.toString());
            }
            if(Math.abs(item.offset.x) > 1.2 || Math.abs(item.offset.y) > 1.2 || Math.abs(item.offset.z) > 1.2){
                item.entity.setEntityDead();
                dropItem(item);
            }
        }
        items.removeIf((V)-> !V.entity.isEntityAlive());
    }

    public void addItem(ItemStack stack, Direction in){
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(TileEntityItemPipe.class);
        tiles.add(IInventory.class);
        tiles.add(IItemConnection.class);
        HashMap<Direction,TileEntity> connectedTiles = getConnectedTileEntities(tiles);
        TileEntity sender = in.getTileEntity(worldObj,this);
        connectedTiles.replace(in,sender,null);
        connectedTiles.keySet().removeIf((D)->connectedTiles.get(D) == null);
        int connections = connectedTiles.size();
        //SignalIndustries.LOGGER.info(String.valueOf(connections));
        Direction out = null;
        if(connections == 1){
           out = (Direction) connectedTiles.keySet().toArray()[0];
        } else if(connections > 1) {
            if(!roundRobin){
                int r = random.nextInt(connectedTiles.keySet().toArray().length);
                out = (Direction) connectedTiles.keySet().toArray()[r];
            } else {
                if(roundRobinN >= connections){
                    roundRobinN = 0;
                }
                out = (Direction) connectedTiles.keySet().toArray()[roundRobinN];
                roundRobinN++;
            }

        } else {
            out = in.getOpposite();
        }
        Vec3f offset = in.getVecF().divide(2).add(0.5f);
        EntityPipeItem item;
        if(getBlockType() == SignalIndustries.itemPipeOpaque){
            item = new EntityPipeItem(worldObj,xCoord+offset.x,yCoord+offset.y,zCoord+offset.z,stack,this).setRender(false);
        } else {
            item = new EntityPipeItem(worldObj,xCoord+offset.x,yCoord+offset.y,zCoord+offset.z,stack,this).setRender(true);
        }

        items.add(new PipeItem(item,in,out,offset));
        worldObj.entityJoinedWorld(item);
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

    public void dropItem(PipeItem item){
        if(items.contains(item)){
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(worldObj, (float) xCoord + f, (float) yCoord + f1, (float) zCoord + f2, item.entity.item);
            float f3 = 0.05F;
            entityitem.motionX = (float) random.nextGaussian() * f3;
            entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
            entityitem.motionZ = (float) random.nextGaussian() * f3;
            worldObj.entityJoinedWorld(entityitem);
        }
    }

    public void destroyItems(){
        for (PipeItem item : items) {
            item.entity.setEntityDead();
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(worldObj, (float) xCoord + f, (float) yCoord + f1, (float) zCoord + f2, item.entity.item);
            float f3 = 0.05F;
            entityitem.motionX = (float) random.nextGaussian() * f3;
            entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
            entityitem.motionZ = (float) random.nextGaussian() * f3;
            worldObj.entityJoinedWorld(entityitem);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }
}
