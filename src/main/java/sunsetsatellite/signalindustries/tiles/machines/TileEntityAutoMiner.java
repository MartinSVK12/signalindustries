package sunsetsatellite.signalindustries.tiles.machines;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.TileEntityItemConduit;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class TileEntityAutoMiner extends TileEntityTieredMachineBase implements IBoostable {

    //TODO: conduit output is broken

    public Vec3i from = new Vec3i();
    public Vec3i to = new Vec3i();
    public Vec3i current = new Vec3i();
    public TickTimer workTimer = new TickTimer(this,this::work,progressMaxTicks,true);
    public int cost;
    public TileEntityAutoMiner(){
        progressMaxTicks = 20;
        cost = 1;
        itemContents = new ItemStack[1];
        fluidCapacity[0] = Short.MAX_VALUE/2;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        from = new Vec3i(0,0,0);
        to = new Vec3i(16,16,16);
        workTimer.pause();
        transferSpeed = 50;
        //.copy().add(new Vec3i(1,0,1));
        itemConnections.replace(Direction.Y_POS,Connection.OUTPUT);
    }

    public void work(){
        if(fluidContents[0] != null && fluidContents[0].amount > cost){
            fluidContents[0].amount -= cost;
            if(fluidContents[0].amount <= 0){
                fluidContents[0] = null;
            }

            current.y = findTopSolidNonLiquidBlockLimited(worldObj,current.x, current.z,y+4);

            if(worldObj.getBlockId(current.x,current.y-1,current.z) != Blocks.BEDROCK.id()){
                Block<?> block = Blocks.getBlock(worldObj.getBlockId(current.x,current.y-1,current.z));
                boolean silk = hasSilkTouch();
                if(block != null){
                    int meta = worldObj.getBlockMetadata(current.x, current.y-1, current.z);
                    Direction dir = null;
                    for (Direction direction : Direction.values()) {
                        if(itemConnections.get(direction) == Connection.OUTPUT || itemConnections.get(direction) == Connection.BOTH){
                            if(direction.getTileEntity(worldObj,this) instanceof TileEntityChest || direction.getTileEntity(worldObj,this) instanceof TileEntityItemConduit){
                                dir = direction;
                            }
                        }
                    }
                    if(dir != null){
                        TileEntity tile = dir.getTileEntity(worldObj,this);
                        ItemStack[] drops = block.getBreakResult(worldObj, silk ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL,x,y,z,meta,tile);
                        if(tile instanceof TileEntityChest){
                            if(drops == null){
                                block.dropBlockWithCause(worldObj, silk ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this,null);
                                worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                                return;
                            }
                            for (ItemStack drop : drops) {
                                int availableSlot = -1;
                                for (int i = 0; i < ((TileEntityChest) tile).getContainerSize(); i++) {
                                    ItemStack stack = ((TileEntityChest) tile).getItem(i);
                                    if(stack == null || (stack.isItemEqual(drop)) && stack.stackSize < stack.getMaxStackSize()) {
                                        availableSlot = i;
                                        break;
                                    }
                                }
                                if(availableSlot == -1){
                                    block.dropBlockWithCause(worldObj, silk ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this,null);
                                    worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                                } else {
                                    ItemStack stack = ((TileEntityChest) tile).getItem(availableSlot);
                                    if(stack == null){
                                        ((TileEntityChest) tile).setItem(availableSlot,drop);
                                    } else if (stack.isItemEqual(drop)){
                                        stack.stackSize+=drop.stackSize;
                                    }
                                    worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                                }
                            }
                        } else if(tile instanceof TileEntityItemConduit) {
                            if(drops != null){
                                for (ItemStack drop : drops) {
                                    if(drop != null){
                                        boolean success = ((TileEntityItemConduit) tile).addItem(drop,dir.getOpposite());
                                        if(!success){
                                            block.dropBlockWithCause(worldObj, silk ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this, null);
                                        }
                                        worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                                    }
                                }
                            }
                        } else {
                            block.dropBlockWithCause(worldObj, silk ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this, null);
                            worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                        }
                    } else {
                        block.dropBlockWithCause(worldObj, silk ? EnumDropCause.SILK_TOUCH : EnumDropCause.PROPER_TOOL,x,y+1,z,meta,this, null);
                        worldObj.setBlockWithNotify(current.x,current.y-1,current.z,0);
                    }
                }
            }

            current.x--;
            if(current.y < 1){
                current.y = y+4;
                //workTimer.pause();
            }
            if(current.x < x-14){
                current.x = x-1;
                current.z++;
                current.y = findTopSolidNonLiquidBlockLimited(worldObj,current.x, current.z,y+4);
                if(current.z > z+14){
                    current.z = z+1;
                    current.y = findTopSolidNonLiquidBlockLimited(worldObj,current.x, current.z,y+4);
                }
            }

        }
    }

    @Override
    public void tick() {
        if(worldObj != null){
            applyModifiers();
            extractFluids();
            if(!workTimer.isPaused()){
                workTimer.tick();
            }
            if(current.equals(new Vec3i())){
                current = new Vec3i(x-1,y+4,z+1);
            }
            boolean silk = hasSilkTouch();
            if(silk){
                cost = 2;
                workTimer.max = ((int) (progressMaxTicks / speedMultiplier) * 2);
            } else {
                cost = 1;
                workTimer.max = (int) (progressMaxTicks / speedMultiplier);
            }
        }
    }

    public int findTopSolidNonLiquidBlockLimited(World world,  int x, int z, int yLimit) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        int k = Math.min(yLimit,world.getHeightBlocks() - 1);
        x &= 15;

        for(z &= 15; k > 0; --k) {
            int l = chunk.getBlockID(x, k, z);
            Material material = l != 0 ? Blocks.blocksList[l].getMaterial() : Material.air;
            if (material.blocksMotion()) {
                return k + 1;
            }
        }

        return -1;
    }

    @Override
    public boolean isBurning() {
        return fluidContents[0] != null;
    }

    public boolean hasSilkTouch(){
        return getItem(0) != null && getItem(0).getItem().equals(SIItems.precisionControlChip);
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.autoMiner";
    }
}
