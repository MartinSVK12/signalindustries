package sunsetsatellite.signalindustries.tiles.conduit;


import com.mojang.nbt.tags.ByteTag;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketTileEntityData;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
//import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
//import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicItemConduit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.tiles.TileEntityFilter;
import sunsetsatellite.signalindustries.tiles.TileEntityStorageContainer;
import sunsetsatellite.signalindustries.tiles.base.TileEntityWithName;
import sunsetsatellite.signalindustries.util.PipeMode;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.*;
import java.util.stream.Collectors;

//TODO: more modes
//TODO: double chests don't work
//TODO: do a second try for items that failed to insert

public class TileEntityItemConduit extends TileEntityWithName implements IScreenActionListener/*, ISupportsMultiparts*/ {

    public static int TRANSFER_TICKS = 20 * 3;
    public static int EXTRACT_TICKS = 20 * 2;
    private final TickTimer extractTimer = new TickTimer(this, this::extractItem, EXTRACT_TICKS, true);
    private final List<PipeItem> contents = new ArrayList<>();
    public PipeMode mode = PipeMode.RANDOM;
    private final Random random = new Random();
    public Tier tier = Tier.PROTOTYPE;
    public PipeType type = PipeType.NORMAL;

    public Map<Direction, Boolean> restrictDirections = new HashMap<>();
    public Map<Direction, Boolean> noConnectDirections = new HashMap<>();
    public boolean sensorActive;
    public int sensorMode = 0;
    public int sensorAmount = 0;
    public boolean sensorUseMeta = true;
    public boolean sensorUseData = false;
    public ItemStack sensorStack = null;

    //public final HashMap<Direction, Multipart> parts = (HashMap<Direction, Multipart>) Catalyst.mapOf(Direction.values(), new Multipart[Direction.values().length]);

    public TileEntityItemConduit() {
        for (Direction dir : Direction.values()) {
            restrictDirections.put(dir, false);
            noConnectDirections.put(dir, false);
        }
    }

    public List<PipeItem> getContents() {
        return contents;
    }

    //returns true if successful, false otherwise
    public boolean addItem(ItemStack stack, Direction entry) {
        HashMap<Direction, TileEntity> surroundings = getSurroundings();
        List<Map.Entry<Direction, TileEntity>> exitList = surroundings.entrySet().stream().filter((E) -> E.getKey() != entry).collect(Collectors.toList());
        if (exitList.isEmpty()) {
            return false;
        }
        Direction exit = null;
        //select the exit direction based on mode
        if (mode == PipeMode.RANDOM) {
            exit = pickRandomExitDirection(exitList, stack);
        } else if (mode == PipeMode.SPLIT) {
            //split stack into multiple pipe items going into every possible direction
            List<Direction> exits = exitList.stream().filter((E) -> surroundings.get(E.getKey()) instanceof IItemIO || surroundings.get(E.getKey()) instanceof Container || surroundings.get(E.getKey()) instanceof TileEntityItemConduit).map(Map.Entry::getKey).collect(Collectors.toList());
            if (exits.isEmpty()) return false;
            //if stack size is divisible by the exit size
            if (stack.stackSize % exits.size() == 0) {
                int split = stack.stackSize / exits.size();
                for (Direction dir : exits) {
                    if (split > 0) {
                        contents.add(new PipeItem(new ItemStack(stack.itemID, split, stack.getMetadata()), entry, dir));
                    }
                }
            } else {
                //if not
                int split = stack.stackSize / exits.size();
                int remaider = stack.stackSize % exits.size();
                for (Direction dir : exits) {
                    if (split + remaider > 0) {
                        contents.add(new PipeItem(new ItemStack(stack.itemID, split + remaider, stack.getMetadata()), entry, dir));
                        remaider = 0;
                    }
                }
            }
            return true;
        }
        if (exit == null) {
            return false;
        }
        //check if exit tile exists and is correct
        TileEntity exitTile = surroundings.get(exit);
        if (!(exitTile instanceof IItemIO) && !(exitTile instanceof TileEntityStorageContainer) && !(exitTile instanceof Container) && !(exitTile instanceof TileEntityItemConduit)) {
            return false;
        }
        //add item to conduit
        PipeItem pipeItem = new PipeItem(stack, entry, exit);
        contents.add(pipeItem);
        return true;
    }

    public void dropItem(PipeItem item, Iterator<PipeItem> iter) {
        if (contents.contains(item)) {
            Vec3f dirVec = item.exit.getVecF().divide(2);
            Vec3f offset = new Vec3f(tilePos).add(dirVec).add(0.5);
            EntityItem entityitem = new EntityItem(worldObj, (float) offset.x, (float) offset.y, (float) offset.z, item.stack);
            float multiplier = 0.05F;
            entityitem.xd = dirVec.x * multiplier;
            entityitem.yd = dirVec.y * multiplier;
            entityitem.zd = dirVec.z * multiplier;
            worldObj.entityJoinedWorld(entityitem);
            if (iter != null) {
                iter.remove();
            } else {
                contents.remove(item);
            }
        }
    }

    public void extractItem() {
        //get surroundings blocks, filter out item conduits
        HashMap<Direction, TileEntity> surroundings = getSurroundings();
        List<Map.Entry<Direction, TileEntity>> entryList = surroundings.entrySet().stream().filter((E) -> !(E.getValue() instanceof TileEntityItemConduit)).collect(Collectors.toList());
        if (entryList.isEmpty()) {
            return;
        }
        Direction entry = null;
        //select the entry direction randomly
        entry = entryList.get(random.nextInt(entryList.size())).getKey();
        if (entry == null) {
            return;
        }
        //conduits can only extract from tiles that implement IItemIO for now
        TileEntity entryTile = surroundings.get(entry);
        if (entryTile instanceof IItemIO && entryTile instanceof Container) {
            IItemIO io = ((IItemIO) entryTile);
            Container inv = ((Container) entryTile);
            //connection check
            if (io.getItemIOForSide(entry.getOpposite()) == Connection.OUTPUT || io.getItemIOForSide(entry.getOpposite()) == Connection.BOTH) {
                int slot = io.getActiveItemSlotForSide(entry.getOpposite());
                if (inv.getItem(slot) != null) {
                    ItemStack stack = inv.getItem(slot);
                    if (stack.stackSize >= 8) {
                        stack = stack.splitStack(8);
                    } else {
                        stack = stack.splitStack(stack.stackSize);
                    }
                    Direction finalEntry = entry;
                    //filter out the entry direction
                    List<Map.Entry<Direction, TileEntity>> exitList = surroundings.entrySet().stream().filter((E) -> E.getKey() != finalEntry).collect(Collectors.toList());
                    if (exitList.isEmpty()) {
                        return;
                    }
                    Direction exit = null;
                    //select the exit direction based on mode
                    if (mode == PipeMode.RANDOM) {
                        exit = pickRandomExitDirection(exitList, stack);
                    } else if (mode == PipeMode.SPLIT) {
                        //split stack into multiple pipe items going into every possible direction
                        List<Direction> exits = exitList.stream().filter((E) -> surroundings.get(E.getKey()) instanceof IItemIO || surroundings.get(E.getKey()) instanceof TileEntityStorageContainer || surroundings.get(E.getKey()) instanceof Container || surroundings.get(E.getKey()) instanceof TileEntityItemConduit).map(Map.Entry::getKey).collect(Collectors.toList());
                        if (exits.isEmpty()) return;
                        //if stack size is divisible by the exit size
                        if (stack.stackSize % exits.size() == 0) {
                            int split = stack.stackSize / exits.size();
                            for (Direction dir : exits) {
                                if (split > 0) {
                                    contents.add(new PipeItem(new ItemStack(stack.itemID, split, stack.getMetadata()), entry, dir));
                                }
                            }
                        } else {
                            //if not
                            int split = stack.stackSize / exits.size();
                            int remaider = stack.stackSize % exits.size();
                            for (Direction dir : exits) {
                                if (split + remaider > 0) {
                                    contents.add(new PipeItem(new ItemStack(stack.itemID, split + remaider, stack.getMetadata()), entry, dir));
                                    remaider = 0;
                                }
                            }
                        }
                        if (stack.stackSize <= 0) {
                            inv.setItem(slot, null);
                        }
                        return;
                    }
                    if (exit == null) {
                        return;
                    }
                    //check if exit tile exists and is correct
                    TileEntity exitTile = surroundings.get(exit);
                    if (!(exitTile instanceof IItemIO) && !(exitTile instanceof Container) && !(exitTile instanceof TileEntityStorageContainer) && !(exitTile instanceof TileEntityItemConduit)) {
                        return;
                    }
                    //add item to conduit
                    PipeItem pipeItem = new PipeItem(stack, entry, exit);
                    contents.add(pipeItem);
                    if (inv.getItem(slot).stackSize <= 0) {
                        inv.setItem(slot, null);
                    }
                }
            }
        }
    }

    private void acceptItem(Direction entry, PipeItem item, TileEntityItemConduit conduit) {
        //get surroundings and keep only surrounding pipes
        HashMap<Direction, TileEntity> surroundings = getSurroundings();
        List<Map.Entry<Direction, TileEntity>> entryList = surroundings.entrySet().stream().filter((E) -> E.getValue() instanceof TileEntityItemConduit).collect(Collectors.toList());
        List<Direction> directions = entryList.stream().map(Map.Entry::getKey).toList();
        //validate entry direction
        if (directions.contains(entry)) {
            //filter out the entry direction and select exit direction based on mode
            List<Map.Entry<Direction, TileEntity>> exitList = surroundings.entrySet().stream().filter((E) -> E.getKey() != entry).collect(Collectors.toList());
            if (exitList.isEmpty()) {
                return;
            }
            Direction exit = null;
            if (mode == PipeMode.RANDOM) {
                exit = pickRandomExitDirection(exitList, item.stack);
            } else if (mode == PipeMode.SPLIT) {
                //split stack into multiple pipe items going into every possible direction
                List<Direction> exits = exitList.stream().filter((E) -> surroundings.get(E.getKey()) instanceof IItemIO || surroundings.get(E.getKey()) instanceof TileEntityStorageContainer || surroundings.get(E.getKey()) instanceof Container || surroundings.get(E.getKey()) instanceof TileEntityItemConduit).map(Map.Entry::getKey).collect(Collectors.toList());
                if (exits.isEmpty()) return;
                //if stack size is divisible by the exit size
                if (item.stack.stackSize % exits.size() == 0) {
                    int split = item.stack.stackSize / exits.size();
                    conduit.contents.remove(item);
                    for (Direction dir : exits) {
                        if (split > 0) {
                            contents.add(new PipeItem(new ItemStack(item.stack.itemID, split, item.stack.getMetadata()), entry, dir));
                        }
                    }
                } else {
                    //if not
                    int split = item.stack.stackSize / exits.size();
                    int remaider = item.stack.stackSize % exits.size();
                    conduit.contents.remove(item);
                    for (Direction dir : exits) {
                        if (split + remaider > 0) {
                            contents.add(new PipeItem(new ItemStack(item.stack.itemID, split + remaider, item.stack.getMetadata()), entry, dir));
                            remaider = 0;
                        }
                    }
                }
                return;
            }
            if (exit == null) {
                return;
            }
            //validate exit tile and transfer item
            TileEntity exitTile = surroundings.get(exit);
            if (!(exitTile instanceof IItemIO) && !(exitTile instanceof TileEntityStorageContainer) && !(exitTile instanceof Container) && !(exitTile instanceof TileEntityItemConduit)) {
                return;
            }
            conduit.contents.remove(item);
            contents.add(new PipeItem(item.stack, entry, exit));
        }
    }

    private Direction pickRandomExitDirection(List<Map.Entry<Direction, TileEntity>> exitList, ItemStack stack) {
        List<Direction> blockedDirs = new ArrayList<>();
        for (Map.Entry<Direction, TileEntity> exitEntry : exitList) {
            if (exitEntry.getValue() instanceof IItemIO || exitEntry.getValue() instanceof Container) {
                if (exitEntry.getValue() instanceof IItemIO) {
                    IItemIO io = (IItemIO) exitEntry.getValue();
                    if (io.getItemIOForSide(exitEntry.getKey().getOpposite()) == Connection.INPUT || io.getItemIOForSide(exitEntry.getKey().getOpposite()) == Connection.BOTH) {
                        return exitEntry.getKey();
                    } else {
                        blockedDirs.add(exitEntry.getKey());
                    }
                } else {
                    Container inv = (Container) exitEntry.getValue();
                    for (int i = 0; i < inv.getContainerSize(); i++) {
                        if (inv.getItem(i) == null) {
                            return exitEntry.getKey();
                        } else if (inv.getItem(i).isItemEqual(stack) && inv.getItem(i).stackSize + stack.stackSize <= inv.getMaxStackSize() && inv.getItem(i).stackSize + stack.stackSize <= inv.getItem(i).getMaxStackSize(inv)) {
                            return exitEntry.getKey();
                        }
                    }
                    blockedDirs.add(exitEntry.getKey());
                }
            } else if (exitEntry.getValue() instanceof TileEntityStorageContainer) {
                TileEntityStorageContainer container = (TileEntityStorageContainer) exitEntry.getValue();
                if (container.contents == null || (container.contents.isItemEqual(stack) && container.contents.getData().equals(stack.getData()) && container.contents.stackSize < container.capacity)) {
                    return exitEntry.getKey();
                }
                blockedDirs.add(exitEntry.getKey());
            }
        }
        restrictDirections.forEach((D, B) -> {
            if (B && !blockedDirs.contains(D)) {
                blockedDirs.add(D);
            }
        });
        noConnectDirections.forEach((D, B) -> {
            if (B && !blockedDirs.contains(D)) {
                blockedDirs.add(D);
            }
        });
        exitList = exitList.stream().filter((E) -> !(blockedDirs.contains(E.getKey()))).collect(Collectors.toList());
        if (exitList.isEmpty()) {
            return null;
        } else if (exitList.size() == 1) {
            return exitList.get(0).getKey();
        }
        return exitList.get(random.nextInt(exitList.size())).getKey();
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlockDirty(tilePos);
        worldObj.notifyBlocksOfNeighborChange(tilePos, getBlock());
        if (worldObj != null && getBlock() != Blocks.AIR) {
			BlockLogicItemConduit logic = Catalyst.blockLogic(getBlock(), BlockLogicItemConduit.class);
			if(logic != null){
				tier = logic.tier;
				type = logic.type;
			}
        }
        switch (tier) {
            case BASIC:
                TRANSFER_TICKS = 20 * 3;
                EXTRACT_TICKS = 20 * 2;
                extractTimer.max = EXTRACT_TICKS;
                break;
            default:
                TRANSFER_TICKS = 20 * 6;
                EXTRACT_TICKS = 20 * 4;
                extractTimer.max = EXTRACT_TICKS;
                break;
        }
        if (EnvironmentHelper.isClientWorld()) return;
        extractTimer.tick();
        contents.removeIf((P) -> P.stack == null || P.stack.stackSize <= 0 || P.stack.itemID == 0);
        final Iterator<PipeItem> iter = contents.iterator();
        while (iter.hasNext()) {
            PipeItem next = iter.next();

            if (next.insertTimer.isPaused()) {
                dropItem(next, iter);
            }
        }
        sensorActive = false;
        for (PipeItem pipeItem : contents.toArray(new PipeItem[0])) {
            pipeItem.insertTimer.tick();
            ItemStack stack = pipeItem.stack;
            if (stack != null && sensorStack != null && type == PipeType.SENSOR) {
                if (stack.itemID == sensorStack.itemID) {
                    sensorActive = checkIfValidForSensor(stack);
                }
            }
        }
    }

    private boolean checkIfValidForSensor(ItemStack stack) {
        boolean yes = false;
        switch (sensorMode) {
            case 0:
                yes = stack.stackSize == sensorAmount;
                break;
            case 1:
                yes = stack.stackSize != sensorAmount;
                break;
            case 2:
                yes = stack.stackSize > sensorAmount;
                break;
            case 3:
                yes = stack.stackSize < sensorAmount;
                break;
            case 4:
                yes = stack.stackSize >= sensorAmount;
                break;
            case 5:
                yes = stack.stackSize <= sensorAmount;
                break;
        }
        if (sensorUseMeta && stack.getMetadata() != sensorStack.getMetadata()) {
            yes = false;
        }
        if (sensorUseData && !stack.getData().equals(sensorStack.getData())) {
            yes = false;
        }
        return yes;
    }

    public HashMap<Direction, TileEntity> getSurroundings() {
        HashMap<Direction, TileEntity> surroundings = new HashMap<>();
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
            if (tile != null) {
                if (tile instanceof Container || tile instanceof TileEntityItemConduit || tile instanceof TileEntityStorageContainer) {
                    if (!noConnectDirections.get(dir)) {
                        surroundings.put(dir, tile);
                    }
                }
            }
        }
        return surroundings;
    }

	@Override
	public void readAdditionalData(@NotNull CompoundTag tag) {
		sensorActive = tag.getBoolean("IsActive");
		sensorMode = tag.getInteger("SensorMode");
		sensorAmount = tag.getInteger("CheckAmount");
		sensorUseMeta = tag.getBoolean("UseMeta");
		sensorUseData = tag.getBoolean("UseData");
		if (tag.containsKey("SensorStack")) {
			sensorStack = ItemStack.readItemStackFromNbt(tag.getCompound("SensorStack"));
		}
		CompoundTag items = tag.getCompound("Items");
		CompoundTag restrict = tag.getCompound("Restrictions");
		CompoundTag noConnect = tag.getCompound("NoConnect");
		contents.clear();
		for (Tag<?> value : items.getValues()) {
			if (value instanceof CompoundTag) {
				CompoundTag itemNbt = (CompoundTag) value;
				PipeItem item = new PipeItem(itemNbt);
				contents.add(item);
			}
		}
		for (Tag<?> value : restrict.getValues()) {
			if (value instanceof ByteTag) {
				restrictDirections.replace(Direction.getFromName(value.getTagName()), ((Byte) value.getValue()) == 1);
			}
		}
		for (Tag<?> value : noConnect.getValues()) {
			if (value instanceof ByteTag) {
				noConnectDirections.replace(Direction.getFromName(value.getTagName()), ((Byte) value.getValue()) == 1);
			}
		}

		/*CompoundTag coversNbt = tag.getCompound("Parts");

		for (Map.Entry<String, Tag<?>> entry : coversNbt.getValue().entrySet()) {
			Direction dir = Direction.values()[Integer.parseInt(entry.getKey())];
			CompoundTag partTag = (CompoundTag) entry.getValue();
			parts.put(dir, new Multipart(partTag));
		}*/

	}

	@Override
    public Packet getDescriptionPacket() {
        return new PacketTileEntityData(this);
    }

	@Override
	public void writeAdditionalData(@NotNull CompoundTag tag) {
		CompoundTag items = new CompoundTag();
		CompoundTag restrict = new CompoundTag();
		CompoundTag noConnect = new CompoundTag();
		for (Map.Entry<Direction, Boolean> entry : restrictDirections.entrySet()) {
			Direction D = entry.getKey();
			Boolean B = entry.getValue();
			restrict.putBoolean(D.getName(), B);
		}
		for (Map.Entry<Direction, Boolean> entry : noConnectDirections.entrySet()) {
			Direction D = entry.getKey();
			Boolean B = entry.getValue();
			noConnect.putBoolean(D.getName(), B);
		}
		for (int i = 0; i < contents.size(); i++) {
			CompoundTag itemNbt = new CompoundTag();
			PipeItem item = contents.get(i);
			item.writeToNBT(itemNbt);
			items.put(String.valueOf(i), itemNbt);
		}
		tag.put("NoConnect", noConnect);
		tag.put("Restrictions", restrict);
		tag.put("Items", items);

		tag.putBoolean("IsActive", sensorActive);
		tag.putInt("CheckAmount", sensorAmount);
		tag.putInt("SensorMode", sensorMode);
		tag.putBoolean("UseMeta", sensorUseMeta);
		tag.putBoolean("UseData", sensorUseData);
		if (sensorStack != null) {
			CompoundTag itemNbt = new CompoundTag();
			sensorStack.writeToNBT(itemNbt);
			tag.putCompound("SensorStack", itemNbt);
		}

		/*CompoundTag coversNbt = new CompoundTag();

		for (Map.Entry<Direction, Multipart> entry : parts.entrySet()) {
			if (entry.getValue() == null) continue;
			CompoundTag partNbt = new CompoundTag();
			entry.getValue().writeToNbt(partNbt);
			coversNbt.putCompound(String.valueOf(entry.getKey().ordinal()), partNbt);
		}

		tag.putCompound("Parts", coversNbt);*/
	}

	/*@Override
    public HashMap<Direction, Multipart> getParts() {
        return parts;
    }*/

    @Override
    public void dropContents(World world, int x, int y, int z) {
        super.dropContents(world, x, y, z);
        List<ItemStack> stacks = getContents().stream().map(PipeItem::getStack).collect(Collectors.toList());
        for (ItemStack itemstack : stacks) {
            if (itemstack != null) {
                Random random = new Random();
                float f = random.nextFloat() * 0.8F + 0.1F;
                float f1 = random.nextFloat() * 0.8F + 0.1F;
                float f2 = random.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0) {
                    int i1 = random.nextInt(21) + 10;
                    if (i1 > itemstack.stackSize) {
                        i1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= i1;
                    EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));
                    float f3 = 0.05F;
                    entityitem.xd = (float) random.nextGaussian() * f3;
                    entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
                    entityitem.zd = (float) random.nextGaussian() * f3;
                    world.entityJoinedWorld(entityitem);
                }
            }
        }
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        switch (type) {
            case RESTRICT: {
                if (id >= 0 && id < 6) {
                    if (restrictDirections.get(Direction.values()[id])) {
                        restrictDirections.replace(Direction.values()[id], false);
                    } else {
                        restrictDirections.replace(Direction.values()[id], true);
                    }
                }
                if (id == 6) {
                    switch (mode) {
                        case RANDOM:
                            mode = PipeMode.SPLIT;
                            break;
                        case SPLIT:
                            mode = PipeMode.RANDOM;
                            break;
                    }
                }
                break;
            }
            case SENSOR: {
                if (id == 2) {
                    if (sensorAmount > 0) sensorAmount--;
                }
                if (id == 1) {
                    sensorAmount++;
                }
                if (id == 3) {
                    sensorUseMeta = !sensorUseMeta;
                }
                if (id == 4) {
                    sensorUseData = !sensorUseData;
                }
                if (id == 0) {
                    sensorMode++;
                    if (sensorMode == 6) {
                        sensorMode = 0;
                    }
                }
                if (id == 5) {
                    switch (mode) {
                        case RANDOM:
                            mode = PipeMode.SPLIT;
                            break;
                        case SPLIT:
                            mode = PipeMode.RANDOM;
                            break;
                    }
                }
                break;
            }
        }

    }

	@Override
	public String getName() {
		return "ItemConduit";
	}

	/// ////


    public class PipeItem {
        private final ItemStack stack;
        private final Direction entry;
        private final Direction exit;
        private final TickTimer insertTimer = new TickTimer(this, this::insertItem, TRANSFER_TICKS, false);

        public ItemStack getStack() {
            return stack;
        }

        public Direction getEntry() {
            return entry;
        }

        public Direction getExit() {
            return exit;
        }

        public int getTicks() {
            return insertTimer.value;
        }

        public PipeItem(ItemStack stack, Direction entry, Direction exit) {
            this.stack = stack;
            this.entry = entry;
            this.exit = exit;
        }

        public PipeItem(CompoundTag tag) {
	        ItemStack preStack;
			preStack = ItemStack.readItemStackFromNbt(tag.getCompound("stack"));
			if(preStack == null){
				preStack = new ItemStack(Blocks.AIR,0,0);
			}
			this.stack = preStack;
	        this.entry = Direction.getDirectionFromSide(tag.getInteger("entry"));
            this.exit = Direction.getDirectionFromSide(tag.getInteger("exit"));
            insertTimer.value = tag.getInteger("ticks");
        }

        public void writeToNBT(CompoundTag compoundTag) {
            CompoundTag stackNbt = new CompoundTag();
            stack.writeToNBT(stackNbt);
            compoundTag.putInt("entry", entry.getSideNumber());
            compoundTag.putInt("exit", exit.getSideNumber());
            compoundTag.putInt("ticks", insertTimer.value);
            compoundTag.putCompound("stack", stackNbt);
        }

        public void insertItem() {
            TileEntity tileEntity = exit.getTileEntity(worldObj, TileEntityItemConduit.this);
            Direction entry = exit.getOpposite();
            //treat the filter as a special case
            if (tileEntity instanceof TileEntityFilter) {
                ((TileEntityFilter) tileEntity).sort(entry.getOpposite(), this, TileEntityItemConduit.this);
                return;
            }
            if (tileEntity instanceof TileEntityStorageContainer) {
                TileEntityStorageContainer container = (TileEntityStorageContainer) tileEntity;
                container.insertStack(this.stack);
            }
            if (tileEntity instanceof IItemIO && tileEntity instanceof Container) {
                IItemIO io = ((IItemIO) tileEntity);
                Container inv = ((Container) tileEntity);
                if (io.getItemIOForSide(entry) == Connection.INPUT || io.getItemIOForSide(entry) == Connection.BOTH) {
                    int slot = io.getActiveItemSlotForSide(entry, stack);
                    ItemStack tileStack = inv.getItem(slot);
                    if (tileStack == null || (tileStack.isItemEqual(stack) && tileStack.stackSize + stack.stackSize <= tileStack.getMaxStackSize())) {
                        if (tileStack == null) {
                            inv.setItem(slot, stack);
                        } else {
                            tileStack.stackSize += stack.stackSize;
                        }
                        contents.remove(this);
                    } else if (tileStack.isItemEqual(stack)) {
                        int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, stack.stackSize);
                        if (remainder <= 0) {
                            return;
                        }
                        stack.stackSize -= remainder;
                        tileStack.stackSize += remainder;
                    }
                }
            } else if (!(tileEntity instanceof IItemIO) && tileEntity instanceof Container) {
                Container inv = ((Container) tileEntity);
                int slot = 0;
                while (stack.stackSize > 0) {
                    if (slot >= inv.getContainerSize()) {
                        break;
                    }
                    ItemStack tileStack = inv.getItem(slot);
                    if (tileStack == null) {
                        inv.setItem(slot, stack);
                        contents.remove(this);
                        break;
                    } else if (tileStack.isItemEqual(stack)) {
                        int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, stack.stackSize);
                        if (remainder <= 0) {
                            slot++;
                            continue;
                        }
                        stack.stackSize -= remainder;
                        tileStack.stackSize += remainder;
                    }
                    slot++;
                }
                if (stack.stackSize <= 0) {
                    contents.remove(this);
                }
            } else if (tileEntity instanceof TileEntityItemConduit) {
                ((TileEntityItemConduit) tileEntity).acceptItem(entry, this, TileEntityItemConduit.this);
            }
        }
    }
}
