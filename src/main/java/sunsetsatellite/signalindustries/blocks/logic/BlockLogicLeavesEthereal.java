package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.WorldSource;

public class BlockLogicLeavesEthereal extends BlockLogic {

    public BlockLogicLeavesEthereal(Block<?> block, Material material) {
        super(block, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public float getAmbientOcclusionStrength(WorldSource blockAccess, int x, int y, int z) {
        return BlockLogicLeavesBase.enableTreeShadowing ? 0.8f : 0.0f;
    }
}
