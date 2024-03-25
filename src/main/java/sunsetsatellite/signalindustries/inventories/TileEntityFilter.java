package sunsetsatellite.signalindustries.inventories;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit.PipeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TileEntityFilter extends TileEntityFluidItemContainer {

    public boolean ignoreMeta = false;
    public FilterSide defaultSide = FilterSide.MAGENTA;

    public TileEntityFilter(){
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        itemContents = new ItemStack[9*6];
    }

    @Override
    public void tick() {
        worldObj.markBlockDirty(x,y,z);
        super.tick();
    }

    public void sort(Direction inputDir, PipeItem item, TileEntityItemConduit conduit){
        FilterSide outputSide = getFilterColor(item.getStack());
        if(outputSide == null){
            conduit.dropItem(item,null);
            return;
        }
        Set<Direction> directionSet = getSurroundings().keySet();
        if (!directionSet.contains(outputSide.getDirection())) {
            conduit.dropItem(item,null);
            return;
        }
        for (Map.Entry<Direction, TileEntityItemConduit> entry : getSurroundings().entrySet()) {
            if(entry.getKey() == outputSide.getDirection()){
                TileEntityItemConduit tile = entry.getValue();
                boolean success = tile.addItem(item.getStack(), outputSide.getDirection().getOpposite());
                if(!success){
                    conduit.dropItem(item,null);
                    return;
                } else {
                    conduit.getContents().remove(item);
                    return;
                }
            }
        }
    }

    public HashMap<Direction, TileEntityItemConduit> getSurroundings(){
        HashMap<Direction, TileEntityItemConduit> surroundings = new HashMap<>();
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if(tile != null){
                if(tile instanceof TileEntityItemConduit){
                    surroundings.put(dir, (TileEntityItemConduit) tile);
                }
            }
        }
        return surroundings;
    }

    public FilterSide getFilterColor(ItemStack stack) {
        for(int i2 = 0; i2 < this.itemContents.length; ++i2) {
            if(this.itemContents[i2] != null && this.itemContents[i2].itemID == stack.itemID && (this.itemContents[i2].getMetadata() == stack.getMetadata()) || ignoreMeta) {
                return FilterSide.values()[i2/9];
            }
        }

        return defaultSide;
    }


    @Override
    public String getInvName() {
        return "Filter";
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        tag.putBoolean("IgnoreMeta",ignoreMeta);
        tag.putInt("DefaultSide",defaultSide.ordinal());
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        ignoreMeta = tag.getBoolean("IgnoreMeta");
        defaultSide = FilterSide.values()[tag.getInteger("DefaultSide")];
    }

    public enum FilterSide {
        RED(Direction.Y_POS),
        GREEN(Direction.Z_NEG),
        BLUE(Direction.X_NEG),
        CYAN(Direction.Y_NEG),
        MAGENTA(Direction.Z_POS),
        YELLOW(Direction.X_POS);

        private final Direction direction;

        FilterSide(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        public static FilterSide getFromDirection(Direction dir){
            for (FilterSide side : values()) {
                if(side.getDirection() == dir){
                    return side;
                }
            }
            return null;
        }
    }
}
