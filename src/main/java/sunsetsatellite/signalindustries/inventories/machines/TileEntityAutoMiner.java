package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;


public class TileEntityAutoMiner extends TileEntityTieredMachineBase implements IBoostable {

    public Vec3i from = new Vec3i();
    public Vec3i to = new Vec3i();
    public Vec3i current = new Vec3i();
    public TickTimer workTimer = new TickTimer(this,"work",progressMaxTicks,true);
    public int cost;
    public TileEntityAutoMiner(){
        progressMaxTicks = 20;
        cost = 250;
        itemContents = new ItemStack[1];
        fluidCapacity[0] = Short.MAX_VALUE*2;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        from = new Vec3i(0,4,0);
        to = new Vec3i(-16,-y-4,16);
        workTimer.pause();
        transferSpeed = 50;
        //.copy().add(new Vec3i(1,0,1));
    }

    public void work(){
        if(fluidContents[0] != null && fluidContents[0].amount > cost){
            fluidContents[0].amount -= cost;
            if(fluidContents[0].amount <= 0){
                fluidContents[0] = null;
            }

            current.x--;
            current.y = worldObj.findTopSolidBlock(current.x, current.z);
            System.out.println(current.y);
            if(current.y < 1){
                current.y = y+4;
                //workTimer.pause();
            }
            if(current.x < x-16){
                current.x = x-1;
                current.z++;
                current.y = worldObj.findTopSolidBlock(current.x, current.z);
                if(current.z > z+16){
                    current.z = z+1;
                    current.y = worldObj.findTopSolidBlock(current.x, current.z);
                }
            }

            //SignalIndustries.LOGGER.info(String.valueOf(current.y));
            if(worldObj.getBlockId(current.x,current.y-1,current.z) != Block.bedrock.id){
                Block block = Block.getBlock(worldObj.getBlockId(current.x,current.y-1,current.z));
                int meta = worldObj.getBlockMetadata(current.x, current.y-1, current.z);
                Direction dir = null;
                for (Direction direction : Direction.values()) {
                    if(itemConnections.get(direction) == Connection.OUTPUT || itemConnections.get(direction) == Connection.BOTH){
                        if(direction.getTileEntity(worldObj,this) instanceof TileEntityChest){
                            dir = direction;
                        }
                    }
                }
                if(dir != null){
                    TileEntity tile = dir.getTileEntity(worldObj,this);
                    ItemStack[] drops = block.getBreakResult(worldObj, EnumDropCause.PROPER_TOOL,x,y,z,meta,tile);
                    if(tile instanceof TileEntityChest){
                        if(drops == null){
                            block.dropBlockWithCause(worldObj, EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this);
                            worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                            return;
                        }
                        for (ItemStack drop : drops) {
                            int availableSlot = -1;
                            for (int i = 0; i < ((TileEntityChest) tile).getSizeInventory(); i++) {
                                ItemStack stack = ((TileEntityChest) tile).getStackInSlot(i);
                                if(stack == null || (stack.isItemEqual(drop)) && stack.stackSize < stack.getMaxStackSize()) {
                                    availableSlot = i;
                                    break;
                                }
                            }
                            if(availableSlot == -1){
                                block.dropBlockWithCause(worldObj, EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this);
                                worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                            } else {
                                ItemStack stack = ((TileEntityChest) tile).getStackInSlot(availableSlot);
                                if(stack == null){
                                    ((TileEntityChest) tile).setInventorySlotContents(availableSlot,drop);
                                } else if (stack.isItemEqual(drop)){
                                    stack.stackSize+=drop.stackSize;
                                }
                                worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                            }
                        }
                    } else {
                        block.dropBlockWithCause(worldObj, EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this);
                        worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                    }
                } else {
                    block.dropBlockWithCause(worldObj, EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this);
                    worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                }
            }
        }
    }

    @Override
    public void tick() {
        speedMultiplier = 1;
        cost = 0;
        for(Direction dir : Direction.values()){
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if(tile instanceof TileEntityBooster){
                if(((TileEntityBooster) tile).isBurning()){
                    int meta = tile.getMovedData();
                    if(Direction.getDirectionFromSide(meta).getOpposite() == dir){
                        speedMultiplier = 2;
                    }
                }
            }
        }
        extractFluids();
        if(!workTimer.isPaused()){
            workTimer.tick();
        }
        if(current.equals(new Vec3i())){
            current = new Vec3i(x-1,y+4,z+1);
        }
        workTimer.max = progressMaxTicks / speedMultiplier;
    }

    @Override
    public boolean isBurning() {
        return fluidContents[0] != null;
    }
}
