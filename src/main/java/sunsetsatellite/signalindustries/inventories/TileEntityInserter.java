package sunsetsatellite.signalindustries.inventories;

import net.minecraft.client.entity.fx.EntityDiggingFX;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.phys.AABB;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;
import java.util.stream.Collectors;

public class TileEntityInserter extends TileEntity implements IBoostable {

    public static final int MAX_WORK_TICKS = 60;
    public TickTimer workTimer = new TickTimer(this,this::work,MAX_WORK_TICKS,true);
    public Direction input = Direction.Z_NEG;
    public Direction output = Direction.Z_POS;
    public float speedMultiplier = 1;
    private Tier tier = Tier.PROTOTYPE;

    @Override
    public void tick() {
        super.tick();
        workTimer.tick();
        workTimer.max = (int) (MAX_WORK_TICKS / speedMultiplier + (tier.ordinal()+1));
        input = Direction.getDirectionFromSide(worldObj.getBlockMetadata(x,y,z));
        output = input.getOpposite();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            tier = block.getTier();
            applyModifiers();
        }
    }

    public void applyModifiers(){
        speedMultiplier = 1;
        for(Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
            if (tile instanceof TileEntityBooster) {
                if (((TileEntityBooster) tile).isBurning()) {
                    int meta = tile.getMovedData();
                    if (Direction.getDirectionFromSide(meta).getOpposite() == dir) {
                        if(((TileEntityBooster) tile).tier == Tier.BASIC){
                            speedMultiplier = 1.5f;
                        } else if(((TileEntityBooster) tile).tier == Tier.REINFORCED) {
                            speedMultiplier = 2;
                        } else if (((TileEntityBooster) tile).tier == Tier.AWAKENED) {
                            speedMultiplier = 3;
                        }
                    }
                }
            }
        }
    }

    public void work(){
        TileEntity inv = input.getTileEntity(worldObj,this);
        TileEntity pipe = output.getTileEntity(worldObj,this);
        AABB aabb = getBlockType().getSelectedBoundingBoxFromPool(worldObj,x,y,z).copy().offset(input.getVecF().x,input.getVecF().y,input.getVecF().z);
        List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class,aabb).stream().map((E)->((EntityItem)E)).collect(Collectors.toList());
        if(pipe instanceof TileEntityItemConduit && (inv instanceof IInventory || inv instanceof TileEntityStorageContainer)){
            if(inv instanceof IInventory){
                int slot = -1;
                for (int i = 0; i < ((IInventory) inv).getSizeInventory(); i++) {
                    ItemStack stack = ((IInventory) inv).getStackInSlot(i);
                    if(stack == null){
                        continue;
                    }
                    slot = i;
                }
                if(slot == -1){
                    return;
                }
                ItemStack stack = ((IInventory) inv).getStackInSlot(slot);
                ItemStack split;
                int maxSplit = (int) Math.min(64,(4 * speedMultiplier) * (tier.ordinal()+1));
                if(stack.stackSize >= maxSplit){
                    split = stack.splitStack(maxSplit);
                } else {
                    split = stack.splitStack(stack.stackSize);
                }
                boolean success = ((TileEntityItemConduit) pipe).addItem(split,output.getOpposite());
                if(!success){
                    stack.stackSize += split.stackSize;
                }
                if(stack.stackSize <= 0){
                    ((IInventory) inv).setInventorySlotContents(slot,null);
                }
            } else {
                TileEntityStorageContainer container = (TileEntityStorageContainer) inv;
                int maxSplit = (int) Math.min(64,(4 * speedMultiplier) * (tier.ordinal()+1));
                ItemStack stack = container.extractStack(maxSplit);
                if(stack != null){
                    boolean success = ((TileEntityItemConduit) pipe).addItem(stack, output.getOpposite());
                    if(!success){
                        Vec3f vec = new Vec3f(x,y,z).add(Direction.getDirectionFromSide(worldObj.getBlockMetadata(x,y,z)).getVecF()).add(0.5f);
                        EntityItem entityitem = new EntityItem(worldObj, vec.x, vec.y, vec.z, stack);
                        worldObj.entityJoinedWorld(entityitem);
                    }
                }
            }
        } else if (pipe instanceof TileEntityItemConduit && !items.isEmpty()) {
            EntityItem item = items.get(0);
            ItemStack split;
            int maxSplit = (int) Math.min(64,(4 * speedMultiplier) * (tier.ordinal()+1));
            if(item.item.stackSize >= maxSplit){
                split = item.item.splitStack(maxSplit);
            } else {
                split = item.item.splitStack(item.item.stackSize);
            }
            boolean success = ((TileEntityItemConduit) pipe).addItem(split,output.getOpposite());
            if(!success){
                item.item.stackSize += split.stackSize;
            } else {
                if(item.item.itemID < 16384){
                    for (int i = 0; i < 4; i++) {
                        SignalIndustries.spawnParticle(new EntityDiggingFX(worldObj, item.x, item.y, item.z, 0,0,0, Block.getBlock(item.item.itemID),0, item.item.getMetadata()));
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        SignalIndustries.spawnParticle(new EntityDiggingFX(worldObj, item.x, item.y, item.z, 0,0,0, getBlockType(),0, item.item.getMetadata()));
                    }
                }
            }
        }
    }


}
