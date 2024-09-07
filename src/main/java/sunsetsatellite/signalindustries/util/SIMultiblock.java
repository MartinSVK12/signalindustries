package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.CompoundTag;
import sunsetsatellite.catalyst.multiblocks.Multiblock;

public class SIMultiblock extends Multiblock {

    public final Tier tier;

    public SIMultiblock(String modId, Class<?>[] modClasses, String translateKey, CompoundTag data, boolean includeAir, Tier tier) {
        super(modId, modClasses, translateKey, data, includeAir);
        this.tier = tier;
    }

    public SIMultiblock(String modId, Class<?>[] modClasses, String translateKey, String filePath, boolean includeAir, Tier tier) {
        super(modId, modClasses, translateKey, filePath, includeAir);
        this.tier = tier;
    }


}
