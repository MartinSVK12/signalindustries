package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.signalindustries.inventories.TileEntityVoidContainer;

public class BlockVoidContainer extends BlockTileEntity {
    public BlockVoidContainer(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityVoidContainer();
    }
}
