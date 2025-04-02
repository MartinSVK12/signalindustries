package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPartBlock;
import sunsetsatellite.signalindustries.util.MultiblockPart;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicMultiblockPart extends BlockLogicMachine implements IMultiblockPartBlock {

    public final MultiblockPart.Type type;
    public final MultiblockPart.IO io;

    public BlockLogicMultiblockPart(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, String guiId, MultiblockPart.Type type, MultiblockPart.IO io) {
        super(block, material, tier, tileEntitySupplier, guiId);
        this.type = type;
        this.io = io;
    }

    @Override
    public MultiblockPart.Type getType() {
        return type;
    }

    @Override
    public MultiblockPart.IO getIO() {
        return io;
    }
}
