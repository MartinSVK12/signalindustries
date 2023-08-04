package sunsetsatellite.signalindustries.blocks;




import sunsetsatellite.signalindustries.SignalIndustries;

public class BlockDilithiumRail extends BlockRail {
    public BlockDilithiumRail(int i, boolean flag) {
        super(i, flag);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int j) {
        if(SignalIndustries.dilithiumRail != null) {
            if (this.blockID == SignalIndustries.dilithiumRail.blockID) {
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
