package sunsetsatellite.signalindustries.tiles;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityFluidHatch extends TileEntityTieredContainer implements IMultiblockPart {

    public TileEntity connectedTo;

    public TileEntityFluidHatch() {
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 8000;
        acceptedFluids.get(0).addAll(Fluid.fluidMap.values());
    }

    @Override
    public void init(Block<?> block) {
        super.init(block);
        if (tier == Tier.PROTOTYPE) {
            fluidCapacity[0] = 8000;
        } else {
            fluidCapacity[0] = (int) Math.pow(2, tier.ordinal()) * 16000;
        }
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
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
        return "container.signalindustries.fluidHatch";
    }
}
