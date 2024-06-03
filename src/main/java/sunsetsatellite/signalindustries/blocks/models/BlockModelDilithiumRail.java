package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelRail;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import sunsetsatellite.signalindustries.blocks.BlockDilithiumRail;

public class BlockModelDilithiumRail extends BlockModelRail<BlockDilithiumRail> {
    public BlockModelDilithiumRail(Block block) {
        super(block);
        powerActive = TextureRegistry.getTexture("signalindustries:block/dilithium_rail");
        powerActiveOverlay = TextureRegistry.getTexture("signalindustries:block/dilithium_rail_overlay");
    }
}
