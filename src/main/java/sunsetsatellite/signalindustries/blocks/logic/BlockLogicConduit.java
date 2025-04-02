package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicConduitBase;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicConduit extends BlockLogicConduitBase {
    public BlockLogicConduit(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier) {
        super(block, material, tier, tileEntitySupplier, ConduitCapability.SIGNALUM);
    }
}
