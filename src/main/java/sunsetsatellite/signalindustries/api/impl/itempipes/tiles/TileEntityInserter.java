package sunsetsatellite.signalindustries.api.impl.itempipes.tiles;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.IItemConnection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.TickTimer;

import java.util.List;

public class TileEntityInserter extends TileEntity implements IItemConnection {

    public Direction inputDir;
    public Direction outputDir;
    public TickTimer workTImer;
    public int stackSize = 8;
    public int delay = 60;

    public TileEntityInserter() {
        try {
            workTImer = new TickTimer(this,this.getClass().getMethod("work"),delay,true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
    }

    @Override
    public void updateEntity() {
        workTImer.tick();
        inputDir = Direction.getDirectionFromSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord));
        outputDir = inputDir.getOpposite();
        super.updateEntity();

    }

    public void work(){
        TileEntity inv = inputDir.getTileEntity(worldObj,this);
        TileEntity pipe = outputDir.getTileEntity(worldObj,this);
        ItemStack stack = null;
        AxisAlignedBB aabb = getBlockType().getSelectedBoundingBoxFromPool(worldObj,xCoord,yCoord,zCoord).expand(inputDir.getVecF().x,inputDir.getVecF().y,inputDir.getVecF().z);
        List<Entity> entities = worldObj.getEntitiesWithinAABB(EntityItem.class,aabb);
        if(entities.size() > 0){
            if (pipe instanceof TileEntityItemPipe){
                EntityItem entity = (EntityItem) entities.get(0);
                stack = entity.item.copy();
                stack.stackSize = Math.min(stackSize,stack.stackSize);
                entity.item.stackSize -= stack.stackSize;
                ((TileEntityItemPipe) pipe).addItem(stack,this.outputDir.getOpposite());
                if(entity.item.stackSize <= 0){
                    entity.setEntityDead();
                }
            }
            return;
        }
        if(inv instanceof TileEntityFluidItemContainer){
            Direction tileIn = outputDir;
            Integer activeSlot = ((TileEntityFluidItemContainer) inv).activeItemSlots.get(tileIn);
            Connection connection = ((TileEntityFluidItemContainer) inv).itemConnections.get(tileIn);
            if(connection == Connection.OUTPUT || connection == Connection.BOTH){
                ItemStack invStack = ((IInventory) inv).getStackInSlot(activeSlot);
                if(invStack != null){
                    stack = invStack.copy();
                    stack.stackSize = Math.min(stackSize,stack.stackSize);
                    ((IInventory) inv).decrStackSize(activeSlot,stackSize);
                }
            }
        } else if (inv instanceof IInventory) {
            for (int i = 0; i < ((IInventory) inv).getSizeInventory(); i++) {
                ItemStack invStack = ((IInventory) inv).getStackInSlot(i);
                if(invStack != null){
                    stack = invStack.copy();
                    stack.stackSize = Math.min(stackSize,stack.stackSize);
                    ((IInventory) inv).decrStackSize(i,stackSize);
                    break;
                }
            }
        }
        if (pipe instanceof TileEntityItemPipe && stack != null){
            ((TileEntityItemPipe) pipe).addItem(stack,outputDir.getOpposite());
        }
    }

}
