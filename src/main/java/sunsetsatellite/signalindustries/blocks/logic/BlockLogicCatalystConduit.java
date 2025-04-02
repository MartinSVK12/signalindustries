package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.network.NetworkComponent;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicConduitBase;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicCatalystConduit extends BlockLogicConduitBase implements NetworkComponent {
    public BlockLogicCatalystConduit(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier) {
        super(block, material, tier, tileEntitySupplier, ConduitCapability.CATALYST_ENERGY);
    }

    @Override
    public NetworkType getType() {
        return NetworkType.CATALYST_ENERGY;
    }
}
