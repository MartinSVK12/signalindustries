package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.BlockRail;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.signalindustries.SignalIndustries;

public class BlockDilithiumRail extends BlockRail {

    public BlockDilithiumRail(String key, int id, boolean isPowered) {
        super(key, id, isPowered);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(Side side, int j) {
        if(SignalIndustries.dilithiumRail != null) {
            if (this.id == SignalIndustries.dilithiumRail.id) {
                if ((j & 0x8) == 0) {
                    return texCoordToIndex(SignalIndustries.railTex[0][0], SignalIndustries.railTex[0][1]);
                } else {
                    return texCoordToIndex(SignalIndustries.railTex[1][0], SignalIndustries.railTex[1][1]);
                }
            }
        }
        return texCoordToIndex(SignalIndustries.railTex[0][0], SignalIndustries.railTex[0][1]);
    }
}
