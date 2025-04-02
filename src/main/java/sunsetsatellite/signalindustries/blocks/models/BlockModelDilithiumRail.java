package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelRail;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicDilithiumRail;

public class BlockModelDilithiumRail extends BlockModelRail<BlockLogicDilithiumRail> {
    public BlockModelDilithiumRail(Block<?> block) {
        super((Block<BlockLogicDilithiumRail>) block);
        setAllTextures(0, "signalindustries:block/dilithium_rail_unpowered");
        powerActive = TextureRegistry.getTexture("signalindustries:block/dilithium_rail");
        powerActiveOverlay = TextureRegistry.getTexture("signalindustries:block/dilithium_rail_overlay");
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side)
    {
        if (!block.getLogic().isPowered || (blockAccess.getBlockMetadata(x, y, z) & 8) == 0) return null;

        return powerActiveOverlay;
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data)
    {
        if(block == SIBlocks.dilithiumRail && (data & 8) != 0)
        {
            return powerActive;
        }
        return super.getBlockTextureFromSideAndMetadata(side, data);
    }
}
