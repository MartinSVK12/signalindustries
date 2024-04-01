package sunsetsatellite.signalindustries.inventories;

import com.mojang.nbt.ByteTag;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.signalindustries.blocks.BlockItemConduit;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.blocks.base.BlockTiered;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWithName;
import sunsetsatellite.signalindustries.items.ItemTiered;
import sunsetsatellite.signalindustries.util.PipeMode;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.*;
import java.util.stream.Collectors;

//TODO: more modes

public class TileEntityItemConduit extends TileEntityWithName {

    public static int TRANSFER_TICKS = 20*3;
    public static int EXTRACT_TICKS = 20*2;
    private final TickTimer extractTimer = new TickTimer(this, this::extractItem, EXTRACT_TICKS, true);
    private final List<PipeItem> contents = new ArrayList<>();
    public PipeMode mode = PipeMode.RANDOM;
    private final Random random = new Random();
    public Tier tier = Tier.PROTOTYPE;
    public PipeType type = PipeType.NORMAL;

    public Map<Direction, Boolean> restrictDirections = new HashMap<>();

    public TileEntityItemConduit(){
        for (Direction dir : Direction.values()) {
            restrictDirections.put(dir,false);
        }
    }

    public List<PipeItem> getContents() {
        return contents;
    }

    //returns true if successful, false otherwise
    public boolean addItem(ItemStack stack, Direction entry){
        HashMap<Direction, TileEntity> surroundings = getSurroundings();
        List<Map.Entry<Direction, TileEntity>> exitList = surroundings.entrySet().stream().filter((E) -> E.getKey() != entry).collect(Collectors.toList());
        if(exitList.isEmpty()){
            return false;
        }
        Direction exit = null;
        //select the exit direction based on mode
        if(mode == PipeMode.RANDOM){
            exit = pickRandomExitDirection(exitList,stack);
        } else if (mode == PipeMode.SPLIT) {
            //split stack into multiple pipe items going into every possible direction
            List<Direction> exits = exitList.stream().filter((E)->surroundings.get(E.getKey()) instanceof IItemIO || surroundings.get(E.getKey()) instanceof IInventory || surroundings.get(E.getKey()) instanceof TileEntityItemConduit).map(Map.Entry::getKey).collect(Collectors.toList());
            if(exits.isEmpty()) return false;
            //if stack size is divisible by the exit size
            if(stack.stackSize % exits.size() == 0){
                int split = stack.stackSize / exits.size();
                for (Direction dir : exits) {
                    if(split > 0){
                        contents.add(new PipeItem(new ItemStack(stack.itemID,split,stack.getMetadata()),entry,dir));
                    }
                }
            } else {
                //if not
                int split = stack.stackSize / exits.size();
                int remaider = stack.stackSize % exits.size();
                for (Direction dir : exits) {
                    if(split+remaider > 0){
                        contents.add(new PipeItem(new ItemStack(stack.itemID,split+remaider,stack.getMetadata()),entry,dir));
                        remaider = 0;
                    }
                }
            }
            return true;
        }
        if(exit == null){
            return false;
        }
        //check if exit tile exists and is correct
        TileEntity exitTile = surroundings.get(exit);
        if (!(exitTile instanceof IItemIO) && !(exitTile instanceof IInventory) && !(exitTile instanceof TileEntityItemConduit)) {
            return false;
        }
        //add item to conduit
        PipeItem pipeItem = new PipeItem(stack, entry, exit);
        contents.add(pipeItem);
        return true;
    }

    public void dropItem(PipeItem item, Iterator<PipeItem> iter){
        if(contents.contains(item)){
            Vec3f dirVec = item.exit.getVecF().divide(2);
            Vec3f offset = new Vec3f(x,y,z).add(dirVec).add(0.5);
            EntityItem entityitem = new EntityItem(worldObj, (float) offset.x, (float) offset.y, (float) offset.z, item.stack);
            float multiplier = 0.05F;
            entityitem.xd = dirVec.x * multiplier;
            entityitem.yd = dirVec.y * multiplier;
            entityitem.zd = dirVec.z * multiplier;
            worldObj.entityJoinedWorld(entityitem);
            if(iter != null){
                iter.remove();
            } else {
                contents.remove(item);
            }
        }
    }

    public void extractItem(){
        //get surroundings blocks, filter out item conduits
        HashMap<Direction, TileEntity> surroundings = getSurroundings();
        List<Map.Entry<Direction, TileEntity>> entryList = surroundings.entrySet().stream().filter((E) -> !(E.getValue() instanceof TileEntityItemConduit)).collect(Collectors.toList());
        if(entryList.isEmpty()){
            return;
        }
        Direction entry = null;
        //select the entry direction randomly
        entry = entryList.get(random.nextInt(entryList.size())).getKey();
        if(entry == null){
            return;
        }
        //conduits can only extract from tiles that implement IItemIO for now
        TileEntity entryTile = surroundings.get(entry);
        if(entryTile instanceof IItemIO && entryTile instanceof IInventory){
            IItemIO io = ((IItemIO) entryTile);
            IInventory inv = ((IInventory) entryTile);
            //connection check
            if(io.getItemIOForSide(entry.getOpposite()) == Connection.OUTPUT || io.getItemIOForSide(entry.getOpposite()) == Connection.BOTH){
                int slot = io.getActiveItemSlotForSide(entry.getOpposite());
                if(inv.getStackInSlot(slot) != null){
                    ItemStack stack = inv.getStackInSlot(slot);
                    if(stack.stackSize >= 8){
                        stack = stack.splitStack(8);
                    } else {
                        stack = stack.splitStack(stack.stackSize);
                    }
                    Direction finalEntry = entry;
                    //filter out the entry direction
                    List<Map.Entry<Direction, TileEntity>> exitList = surroundings.entrySet().stream().filter((E) -> E.getKey() != finalEntry).collect(Collectors.toList());
                    if(exitList.isEmpty()){
                        return;
                    }
                    Direction exit = null;
                    //select the exit direction based on mode
                    if(mode == PipeMode.RANDOM){
                        exit = pickRandomExitDirection(exitList,stack);
                    } else if (mode == PipeMode.SPLIT) {
                        //split stack into multiple pipe items going into every possible direction
                        List<Direction> exits = exitList.stream().filter((E)->surroundings.get(E.getKey()) instanceof IItemIO || surroundings.get(E.getKey()) instanceof IInventory || surroundings.get(E.getKey()) instanceof TileEntityItemConduit).map(Map.Entry::getKey).collect(Collectors.toList());
                        if(exits.isEmpty()) return;
                        //if stack size is divisible by the exit size
                        if(stack.stackSize % exits.size() == 0){
                            int split = stack.stackSize / exits.size();
                            for (Direction dir : exits) {
                                if(split > 0){
                                    contents.add(new PipeItem(new ItemStack(stack.itemID,split,stack.getMetadata()),entry,dir));
                                }
                            }
                        } else {
                            //if not
                            int split = stack.stackSize / exits.size();
                            int remaider = stack.stackSize % exits.size();
                            for (Direction dir : exits) {
                                if(split+remaider > 0){
                                    contents.add(new PipeItem(new ItemStack(stack.itemID,split+remaider,stack.getMetadata()),entry,dir));
                                    remaider = 0;
                                }
                            }
                        }
                        if(stack.stackSize <= 0){
                            inv.setInventorySlotContents(slot,null);
                        }
                        return;
                    }
                    if(exit == null){
                        return;
                    }
                    //check if exit tile exists and is correct
                    TileEntity exitTile = surroundings.get(exit);
                    if (!(exitTile instanceof IItemIO) && !(exitTile instanceof IInventory) && !(exitTile instanceof TileEntityItemConduit)) {
                        return;
                    }
                    //add item to conduit
                    PipeItem pipeItem = new PipeItem(stack, entry, exit);
                    contents.add(pipeItem);
                    if(inv.getStackInSlot(slot).stackSize <= 0){
                        inv.setInventorySlotContents(slot,null);
                    }
                }
            }
        }
    }

    private void acceptItem(Direction entry, PipeItem item, TileEntityItemConduit conduit){
        //get surroundings and keep only surrounding pipes
        HashMap<Direction, TileEntity> surroundings = getSurroundings();
        List<Map.Entry<Direction, TileEntity>> entryList = surroundings.entrySet().stream().filter((E) -> E.getValue() instanceof TileEntityItemConduit).collect(Collectors.toList());
        List<Direction> directions = entryList.stream().map(Map.Entry::getKey).collect(Collectors.toList());
        //validate entry direction
        if(directions.contains(entry)){
            //filter out the entry direction and select exit direction based on mode
            List<Map.Entry<Direction, TileEntity>> exitList = surroundings.entrySet().stream().filter((E) -> E.getKey() != entry).collect(Collectors.toList());
            if(exitList.isEmpty()){
                return;
            }
            Direction exit = null;
            if(mode == PipeMode.RANDOM){
                exit = pickRandomExitDirection(exitList, item.stack);
            } else if (mode == PipeMode.SPLIT) {
                //split stack into multiple pipe items going into every possible direction
                List<Direction> exits = exitList.stream().filter((E)->surroundings.get(E.getKey()) instanceof IItemIO || surroundings.get(E.getKey()) instanceof IInventory || surroundings.get(E.getKey()) instanceof TileEntityItemConduit).map(Map.Entry::getKey).collect(Collectors.toList());
                if(exits.isEmpty()) return;
                //if stack size is divisible by the exit size
                if(item.stack.stackSize % exits.size() == 0){
                    int split = item.stack.stackSize / exits.size();
                    conduit.contents.remove(item);
                    for (Direction dir : exits) {
                        if(split > 0){
                            contents.add(new PipeItem(new ItemStack(item.stack.itemID,split,item.stack.getMetadata()),entry,dir));
                        }
                    }
                } else {
                    //if not
                    int split = item.stack.stackSize / exits.size();
                    int remaider = item.stack.stackSize % exits.size();
                    conduit.contents.remove(item);
                    for (Direction dir : exits) {
                        if(split+remaider > 0){
                            contents.add(new PipeItem(new ItemStack(item.stack.itemID,split+remaider,item.stack.getMetadata()),entry,dir));
                            remaider = 0;
                        }
                    }
                }
                return;
            }
            if(exit == null){
                return;
            }
            //validate exit tile and transfer item
            TileEntity exitTile = surroundings.get(exit);
            if (!(exitTile instanceof IItemIO) && !(exitTile instanceof IInventory) && !(exitTile instanceof TileEntityItemConduit)) {
                return;
            }
            conduit.contents.remove(item);
            contents.add(new PipeItem(item.stack,entry,exit));
        }
    }

    private Direction pickRandomExitDirection(List<Map.Entry<Direction, TileEntity>> exitList, ItemStack stack){
        List<Direction> blockedDirs = new ArrayList<>();
        for (Map.Entry<Direction, TileEntity> exitEntry : exitList) {
            if(exitEntry.getValue() instanceof IItemIO || exitEntry.getValue() instanceof IInventory){
                if(exitEntry.getValue() instanceof IItemIO){
                    IItemIO io = (IItemIO) exitEntry.getValue();
                    if(io.getItemIOForSide(exitEntry.getKey().getOpposite()) == Connection.INPUT || io.getItemIOForSide(exitEntry.getKey().getOpposite()) == Connection.BOTH){
                        return exitEntry.getKey();
                    } else {
                        blockedDirs.add(exitEntry.getKey());
                    }
                } else {
                    IInventory inv = (IInventory) exitEntry.getValue();
                    for (int i = 0; i < inv.getSizeInventory(); i++) {
                        if(inv.getStackInSlot(i) == null){
                            return exitEntry.getKey();
                        } else if (inv.getStackInSlot(i).isItemEqual(stack) && inv.getStackInSlot(i).stackSize+stack.stackSize <= inv.getInventoryStackLimit() && inv.getStackInSlot(i).stackSize+stack.stackSize <= inv.getStackInSlot(i).getMaxStackSize(inv)) {
                            return exitEntry.getKey();
                        }
                    }
                    blockedDirs.add(exitEntry.getKey());
                }
            }
        }
        restrictDirections.forEach((D,B)->{
            if(B && !blockedDirs.contains(D)){
                blockedDirs.add(D);
            }
        });
        exitList = exitList.stream().filter((E)->!(blockedDirs.contains(E.getKey()))).collect(Collectors.toList());
        if(exitList.isEmpty()){
            return null;
        } else if (exitList.size() == 1) {
            return exitList.get(0).getKey();
        }
        return exitList.get(random.nextInt(exitList.size())).getKey();
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlockDirty(x,y,z);
        if(worldObj != null && getBlockType() != null){
            tier = ((ITiered)getBlockType()).getTier();
            type = ((BlockItemConduit)getBlockType()).type;
        }
        switch (tier){
            case BASIC:
                TRANSFER_TICKS = 20*3;
                EXTRACT_TICKS = 20*2;
                extractTimer.max = EXTRACT_TICKS;
                break;
            default:
                TRANSFER_TICKS = 20*6;
                EXTRACT_TICKS = 20*4;
                extractTimer.max = EXTRACT_TICKS;
                break;
        }
        extractTimer.tick();
        contents.removeIf((P)->P.stack == null);
        final Iterator<PipeItem> iter = contents.iterator();
        while(iter.hasNext()){
            PipeItem next = iter.next();
            if(next.insertTimer.isPaused()){
                dropItem(next,iter);
            }
        }
        for (PipeItem pipeItem : contents.toArray(new PipeItem[0])) {
            pipeItem.insertTimer.tick();
        }
    }

    public HashMap<Direction, TileEntity> getSurroundings(){
        HashMap<Direction, TileEntity> surroundings = new HashMap<>();
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if(tile != null){
                if(tile instanceof IInventory || tile instanceof TileEntityItemConduit){
                    surroundings.put(dir,tile);
                }
            }
        }
        return surroundings;
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        CompoundTag items = nbttagcompound.getCompound("Items");
        CompoundTag restrict = nbttagcompound.getCompound("Restrictions");
        for (Tag<?> value : items.getValues()) {
            if(value instanceof CompoundTag){
                CompoundTag itemNbt = (CompoundTag) value;
                PipeItem item = new PipeItem(itemNbt);
                contents.add(item);
            }
        }
        for (Tag<?> value : restrict.getValues()) {
            if(value instanceof ByteTag){
                restrictDirections.replace(Direction.getFromName(value.getTagName()), ((Byte) value.getValue()) == 1);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        CompoundTag items = new CompoundTag();
        CompoundTag restrict = new CompoundTag();
        for (Map.Entry<Direction, Boolean> entry : restrictDirections.entrySet()) {
            Direction D = entry.getKey();
            Boolean B = entry.getValue();
            restrict.putBoolean(D.getName(), B);
        }
        for (int i = 0; i < contents.size(); i++) {
            CompoundTag itemNbt = new CompoundTag();
            PipeItem item = contents.get(i);
            item.writeToNBT(itemNbt);
            items.put(String.valueOf(i),itemNbt);
        }
        nbttagcompound.put("Restrictions",restrict);
        nbttagcompound.put("Items",items);
    }


    ///////


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

        public PipeItem(CompoundTag tag){
            this.stack = ItemStack.readItemStackFromNbt(tag.getCompound("stack"));
            this.entry = Direction.getDirectionFromSide(tag.getInteger("entry"));
            this.exit = Direction.getDirectionFromSide(tag.getInteger("exit"));
            insertTimer.value = tag.getInteger("ticks");
        }

        public void writeToNBT(CompoundTag compoundTag) {
            CompoundTag stackNbt = new CompoundTag();
            stack.writeToNBT(stackNbt);
            compoundTag.putInt("entry",entry.getSide());
            compoundTag.putInt("exit",exit.getSide());
            compoundTag.putInt("ticks", insertTimer.value);
        }

        public void insertItem(){
            TileEntity tileEntity = exit.getTileEntity(worldObj, TileEntityItemConduit.this);
            Direction entry = exit.getOpposite();
            //treat the filter as a special case
            if(tileEntity instanceof TileEntityFilter){
                ((TileEntityFilter)tileEntity).sort(entry.getOpposite(),this,TileEntityItemConduit.this);
                return;
            }
            if (tileEntity instanceof IItemIO && tileEntity instanceof IInventory) {
                IItemIO io = ((IItemIO) tileEntity);
                IInventory inv = ((IInventory) tileEntity);
                if(io.getItemIOForSide(entry) == Connection.INPUT || io.getItemIOForSide(entry) == Connection.BOTH){
                    int slot = io.getActiveItemSlotForSide(entry);
                    ItemStack tileStack = inv.getStackInSlot(slot);
                    if(tileStack == null || (tileStack.isItemEqual(stack) && tileStack.stackSize+stack.stackSize <= tileStack.getMaxStackSize())){
                        if(tileStack == null){
                            inv.setInventorySlotContents(slot,stack);
                        } else {
                            tileStack.stackSize += stack.stackSize;
                        }
                        contents.remove(this);
                    } else if (tileStack.isItemEqual(stack)) {
                        int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, stack.stackSize);
                        if(remainder <= 0){
                            return;
                        }
                        stack.stackSize -= remainder;
                        tileStack.stackSize += remainder;
                    }
                }
            } else if (!(tileEntity instanceof IItemIO) && tileEntity instanceof IInventory){
                IInventory inv = ((IInventory) tileEntity);
                int slot = 0;
                while (stack.stackSize > 0){
                    if(slot >= inv.getSizeInventory()){
                        break;
                    }
                    ItemStack tileStack = inv.getStackInSlot(slot);
                    if(tileStack == null) {
                        inv.setInventorySlotContents(slot,stack);
                        contents.remove(this);
                        break;
                    } else if(tileStack.isItemEqual(stack)){
                        int remainder = Math.min(tileStack.getMaxStackSize() - tileStack.stackSize, stack.stackSize);
                        if(remainder <= 0){
                            slot++;
                            continue;
                        }
                        stack.stackSize -= remainder;
                        tileStack.stackSize += remainder;
                    }
                    slot++;
                }
                if(stack.stackSize <= 0){
                    contents.remove(this);
                }
            } else if(tileEntity instanceof TileEntityItemConduit) {
                ((TileEntityItemConduit) tileEntity).acceptItem(entry,this,TileEntityItemConduit.this);
            }
        }
    }
}
