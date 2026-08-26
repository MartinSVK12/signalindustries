package sunsetsatellite.signalindustries.tiles;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.List;

public class TileEntityInserter extends TileEntity implements IBoostable {

    public static final int MAX_WORK_TICKS = 60;
    public TickTimer workTimer = new TickTimer(this, this::work, MAX_WORK_TICKS, true);
    public Direction input = Direction.Z_NEG;
    public Direction output = Direction.Z_POS;
    public float speedMultiplier = 1;
    private Tier tier = Tier.PROTOTYPE;

    @Override
    public void tick() {
        super.tick();
		if(worldObj == null) return;
        workTimer.tick();
        workTimer.max = (int) (MAX_WORK_TICKS / speedMultiplier + (tier.ordinal() + 1));
        input = Direction.getDirectionFromSide(worldObj.getBlockData(tilePos));
        output = input.getOpposite();
        Block<?> block = getBlock();
        if (block != Blocks.AIR) {
			ITiered tiered = Catalyst.blockLogic(block, ITiered.class);
			if(tiered != null){
				tier = tiered.getTier();
			}
            applyModifiers();
        }
    }

	@Override
	public void readAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void writeAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	public void applyModifiers() {
        speedMultiplier = 1;
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
            if (tile instanceof TileEntityBooster) {
                if (((TileEntityBooster) tile).isBurning()) {
                    int meta = tile.getBlockMeta();
                    if (Direction.getDirectionFromSide(meta).getOpposite() == dir) {
                        if (((TileEntityBooster) tile).tier == Tier.BASIC) {
                            speedMultiplier = 1.5f;
                        } else if (((TileEntityBooster) tile).tier == Tier.REINFORCED) {
                            speedMultiplier = 2;
                        } else if (((TileEntityBooster) tile).tier == Tier.AWAKENED) {
                            speedMultiplier = 3;
                        }
                    }
                }
            }
        }
    }

    public void work() {
        TileEntity inv = input.getTileEntity(worldObj, this);
        TileEntity pipe = output.getTileEntity(worldObj, this);
        if (getBlock() == null) return;
        AABBdc aabb = getBlock().getSelectionAABB(worldObj, tilePos).translate(input.getVecF().x, input.getVecF().y, input.getVecF().z, new AABBd());
        List<EntityItem> items = new ArrayList<>(worldObj.getEntitiesWithinAABB(EntityItem.class, aabb));
        if (pipe instanceof TileEntityItemConduit && (inv instanceof Container || inv instanceof TileEntityStorageContainer)) {
            if (inv instanceof Container) {
                int slot = -1;
                for (int i = 0; i < ((Container) inv).getContainerSize(); i++) {
                    ItemStack stack = ((Container) inv).getItem(i);
                    if (stack == null) {
                        continue;
                    }
                    slot = i;
                }
                if (slot == -1) {
                    return;
                }
                int maxSplit = (int) Math.min(64, (4 * speedMultiplier) * (tier.ordinal() + 1));
                ItemStack stack = ((Container) inv).getItem(slot);
                if (stack == null) return;
                InventoryWrapper wrapper = new InventoryWrapper((Container) inv);

                ItemStack toInsert = wrapper.removeUntil(stack.itemID, stack.getMetadata(), maxSplit, stack.getData(), false, false);

                boolean success = ((TileEntityItemConduit) pipe).addItem(toInsert, output.getOpposite());
                if (!success) {
                    ItemStack leftovers = wrapper.add(toInsert);
                    if (leftovers != null) {
                        Vec3f vec = new Vec3f(tilePos).add(Direction.getDirectionFromSide(worldObj.getBlockData(tilePos)).getVecF()).add(0.5f);
                        EntityItem entityitem = new EntityItem(worldObj, vec.x, vec.y, vec.z, leftovers);
                        worldObj.entityJoinedWorld(entityitem);
                    }
                }
            } else {
                TileEntityStorageContainer container = (TileEntityStorageContainer) inv;
                int maxSplit = (int) Math.min(64, (4 * speedMultiplier) * (tier.ordinal() + 1));
                ItemStack stack = container.extractStack(maxSplit);
                if (stack != null) {
                    boolean success = ((TileEntityItemConduit) pipe).addItem(stack, output.getOpposite());
                    if (!success) {
                        Vec3f vec = new Vec3f(tilePos).add(Direction.getDirectionFromSide(worldObj.getBlockData(tilePos)).getVecF()).add(0.5f);
                        EntityItem entityitem = new EntityItem(worldObj, vec.x, vec.y, vec.z, stack);
                        worldObj.entityJoinedWorld(entityitem);
                    }
                }
            }
        } else if (pipe instanceof TileEntityItemConduit && !items.isEmpty()) {
            EntityItem item = items.get(0);
            ItemStack split;
            int maxSplit = (int) Math.min(64, (4 * speedMultiplier) * (tier.ordinal() + 1));
            if (item.item.stackSize >= maxSplit) {
                split = item.item.splitStack(maxSplit);
            } else {
                split = item.item.splitStack(item.item.stackSize);
            }
            boolean success = ((TileEntityItemConduit) pipe).addItem(split, output.getOpposite());
            if (!success) {
                item.item.stackSize += split.stackSize;
            } else {
                if (item.item.itemID < 16384) {
                    for (int i = 0; i < 4; i++) {
                        //SignalIndustries.spawnParticle(new EntityDiggingFX(worldObj, item.x, item.y, item.z, 0,0,0, Block.getBlock(item.item.itemID),0, item.item.getMetadata()));
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        //SignalIndustries.spawnParticle(new EntityDiggingFX(worldObj, item.x, item.y, item.z, 0,0,0, getBlockType(),0, item.item.getMetadata()));
                    }
                }
            }
        }
    }


}
