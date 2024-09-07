package sunsetsatellite.signalindustries.inventories.machines;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.util.SIMultiblock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TileEntityBuilder extends TileEntityTieredMachineBase implements IBoostable {

    public int cost = 10;

    public TickTimer workTimer = new TickTimer(this,this::work,5,true);
    public Vec3i offset = new Vec3i();
    public Direction rotation = Direction.Z_NEG;

    public SIMultiblock buildingMultiblock;
    public ArrayList<BlockInstance> buildingBlocks = new ArrayList<>();
    public Vec3i currentlyBuilding = new Vec3i();
    public int buildingBlockIndex = 0;
    public int builtBlocks = 0;

    public TileEntityBuilder() {
        itemContents = new ItemStack[3*9 + 1];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 2000;
        acceptedFluids.get(0).add(SIBlocks.energyFlowing);
        workTimer.pause();
    }

    public void work(){
        SIMultiblock multiblock = getMultiblock();
        if (multiblock != null) {
            setStructureToBuild();
            if(buildingBlocks.isEmpty()){
                workTimer.pause();
                return;
            }
            BlockInstance blockInstance = buildingBlocks.get(buildingBlockIndex);
            if(Arrays.stream(itemContents).anyMatch((S)-> S != null && S.stackSize > 0 && S.itemID == blockInstance.block.id)){
                if(fluidContents[0] != null && fluidContents[0].amount >= cost){
                    if(blockInstance.place(worldObj)){
                        fluidContents[0].amount -= cost;
                        List<ItemStack> stackList = Arrays.stream(itemContents).filter((S) -> S != null && S.stackSize > 0 && S.itemID == blockInstance.block.id).collect(Collectors.toList());
                        stackList.get(0).stackSize--;
                        buildingBlocks.remove(blockInstance);
                        builtBlocks++;
                    }
                }
            }
            buildingBlockIndex++;
            if(buildingBlockIndex >= buildingBlocks.size()){
                buildingBlockIndex = 0;
            }
            if(!buildingBlocks.isEmpty()){
                currentlyBuilding = buildingBlocks.get(buildingBlockIndex).pos;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
        workTimer.max = (int) (10 / speedMultiplier);
        workTimer.tick();
        if(fluidContents[0] == null || itemContents[0] == null || !(itemContents[0].getItem() instanceof ItemBlueprint)){
            reset();
        }
        fuelMaxBurnTicks = workTimer.max;
        progressMaxTicks = workTimer.max;
        fuelBurnTicks = workTimer.max-workTimer.value;
        progressTicks = workTimer.value;
        for (int i = 0; i < itemContents.length; i++) {
            if(itemContents[i] != null && itemContents[i].stackSize <= 0){
                itemContents[i] = null;
            }
        }
        for (int i = 0; i < fluidContents.length; i++) {
            if(fluidContents[i] != null && fluidContents[i].amount <= 0){
                fluidContents[i] = null;
            }
        }
    }

    public void reset(){
        workTimer.pause();
        buildingBlockIndex = 0;
        builtBlocks = 0;
        currentlyBuilding = new Vec3i();
        buildingMultiblock = null;
        buildingBlocks.clear();
    }

    public void setStructureToBuild(){
        SIMultiblock multiblock = getMultiblock();
        if (multiblock != null) {
            if (buildingMultiblock == null && buildingBlocks.isEmpty()) {
                buildingMultiblock = multiblock;
                ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(x, y, z).add(offset), rotation);
                blocks.add(multiblock.getOrigin(new Vec3i(x, y, z).add(offset), rotation.getOpposite().shiftAxis()));
                buildingBlocks = blocks;
            } else if (buildingMultiblock != multiblock) {
                buildingBlockIndex = 0;
                builtBlocks = 0;
                currentlyBuilding = new Vec3i();
                buildingMultiblock = multiblock;
                ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(x, y, z).add(offset), rotation);
                blocks.add(multiblock.getOrigin(new Vec3i(x, y, z).add(offset), rotation.getOpposite().shiftAxis()));
                buildingBlocks = blocks;
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        CompoundTag offsetTag = new CompoundTag();
        offset.writeToNBT(offsetTag);
        tag.put("Offset", offsetTag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        offset = new Vec3i(tag.getCompound("Offset"));
    }

    @Override
    public boolean isBurning() {
        return fluidContents[0] != null && !workTimer.isPaused();
    }

    public SIMultiblock getMultiblock(){
        if(itemContents[0] != null && itemContents[0].getItem() instanceof ItemBlueprint) {
            String key = itemContents[0].getData().getStringOrDefault("multiblock", "");
            if (Objects.equals(key, "")) {
                return null;
            }
            SIMultiblock multiblock = (SIMultiblock) Multiblock.multiblocks.get(key.replace("multiblock.signalindustries.", ""));
            if (multiblock == null) {
                return null;
            }
            if (multiblock.tier.ordinal() > tier.ordinal() ) {
                return null;
            }
            return multiblock;
        }
        return null;
    }

    @Override
    public String getInvName() {
        return "Builder";
    }
}
