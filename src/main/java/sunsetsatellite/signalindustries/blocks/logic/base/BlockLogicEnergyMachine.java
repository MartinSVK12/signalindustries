package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.catalyst.core.util.network.NetworkComponent;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicEnergyMachine extends BlockLogicMachine implements NetworkComponent {

    public BlockLogicEnergyMachine(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, material, tier, tileEntitySupplier, guiId);
    }

    @Override
    public NetworkType getType() {
        return NetworkType.CATALYST_ENERGY;
    }
}
