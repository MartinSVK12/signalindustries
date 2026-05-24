package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class BlockLogicLeavesEthereal extends BlockLogic {

    public BlockLogicLeavesEthereal(Block<?> block, Material material) {
        super(block, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

	@Override
	public float getAmbientOcclusionStrength(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return BlockLogicLeavesBase.enableTreeShadowing ? 0.8f : 0.0f;
	}
}
