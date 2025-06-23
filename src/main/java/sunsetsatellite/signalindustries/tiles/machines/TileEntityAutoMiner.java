package sunsetsatellite.signalindustries.tiles.machines;

import com.mojang.nbt.tags.CompoundTag;
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
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.TileEntityItemConduit;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityAutoMiner extends TileEntityTieredMachineBase implements IBoostable, IScreenActionListener {

    //TODO: conduit output is broken

    public Vec2i maxSize = new Vec2i(16,16);
    public Vec2i size = new Vec2i(0,0);
    public Vec3i current = new Vec3i();
    public TickTimer workTimer = new TickTimer(this,this::work,progressMaxTicks,true);
    public int cost;
    public int multiplier = 1;
    public TileEntityAutoMiner(){
        progressMaxTicks = 5;
        fuelMaxBurnTicks = 20;
        cost = 1;
        itemContents = new ItemStack[1];
        fluidCapacity[0] = Short.MAX_VALUE/2;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        workTimer.pause();
        transferSpeed = 50;
        //.copy().add(new Vec3i(1,0,1));
        itemConnections.replace(Direction.Y_POS,Connection.OUTPUT);
    }

    @Override
    public void init(Block<?> block) {
        super.init(block);
        maxSize = tier == Tier.BASIC ? new Vec2i(16,16) : new Vec2i(32,32);
        if(size.x == 0 || size.y == 0){
            size = maxSize.copy();
        }
        multiplier = tier == Tier.BASIC ? 1 : 2;
    }

    public void work(){
        if(fuelBurnTicks == 0 && fluidContents[0] != null && fluidContents[0].amount > cost) {
            fluidContents[0].amount -= cost;
            fuelBurnTicks = fuelMaxBurnTicks;
            if (fluidContents[0].amount <= 0) {
                fluidContents[0] = null;
            }
        }
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
            for (int m = 0; m < multiplier; m++) {
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
                            if(block.hasTag(SignalIndustries.ORE_BLOCK) && tier == Tier.REINFORCED){
                                for (ItemStack drop : drops) {
                                    drop.stackSize *= 2;
                                }
                            }
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
                if(current.x < x-(size.x-2)){
                    current.x = x-1;
                    current.z++;
                    current.y = findTopSolidNonLiquidBlockLimited(worldObj,current.x, current.z,y+4);
                    if(current.z > z+(size.y-2)){
                        current.z = z+1;
                        current.y = findTopSolidNonLiquidBlockLimited(worldObj,current.x, current.z,y+4);
                    }
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

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        CompoundTag sizeTag = new CompoundTag();
        size.writeToNBT(sizeTag);
        tag.put("Size",sizeTag);
        tag.putBoolean("Active", !workTimer.isPaused());
        CompoundTag currentTag = new CompoundTag();
        current.writeToNBT(currentTag);
        tag.put("Current",currentTag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        CompoundTag sizeTag = tag.getCompound("Size");
        size.readFromNBT(sizeTag);
        boolean active = tag.getBoolean("Active");
        if(active && workTimer.isPaused()){
            workTimer.unpause();
        } else {
            workTimer.pause();
        }
        CompoundTag currentTag = tag.getCompound("Current");
        current.readFromNBT(currentTag);
    }

    public int findTopSolidNonLiquidBlockLimited(World world, int x, int z, int yLimit) {
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

    @Override
    public void buttonClicked(int id, int button, int channel) {
        switch (id) {
            case 3:
                if (size.x < maxSize.x) {
                    size.x++;
                }
                break;
            case 4:
                if (size.x > 1) {
                    size.x--;
                }
                break;
            case 5:
                if (size.y < maxSize.y) {
                    size.y++;
                }
                break;
            case 6:
                if (size.y > 1) {
                    size.y--;
                }
                break;
        }
    }
}
