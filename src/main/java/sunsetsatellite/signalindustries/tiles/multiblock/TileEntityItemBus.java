package sunsetsatellite.signalindustries.tiles.multiblock;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Arrays;

public class TileEntityItemBus extends TileEntityTieredContainer implements IMultiblockPart {

    public TileEntity connectedTo;
    private boolean init = false;

    public TileEntityItemBus() {
        itemContents = new ItemStack[18];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        acceptedFluids.clear();
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
        if (!init) {
            if (tier == Tier.BASIC) {
                itemContents = Arrays.copyOf(itemContents, 4);
                init = true;
            }
			if (tier == Tier.REINFORCED) {
				itemContents = Arrays.copyOf(itemContents, 9);
				init = true;
			}
        }
    }

    @Override
    public boolean isConnected() {
        return connectedTo != null;
    }

    @Override
    public TileEntity getConnectedTileEntity() {
        return connectedTo;
    }

    @Override
    public boolean connect(TileEntity tileEntity) {
        connectedTo = tileEntity;
        return true;
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.itemBus";
    }

	@Override
	public void sort() {

	}
}
