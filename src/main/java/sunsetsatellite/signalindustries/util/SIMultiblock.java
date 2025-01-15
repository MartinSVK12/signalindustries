package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.block.Block;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.List;
import java.util.Map;

public class SIMultiblock extends Multiblock {

    public List<String> extraBlocks = Catalyst.listOf("reinforcedParallelProcessor","awakenedParallelProcessor","awakenedParallelProcessor8x");

    public final Tier tier;

    public SIMultiblock(String modId, Class<?>[] modClasses, String translateKey, CompoundTag data, boolean includeAir, Tier tier) {
        super(modId, modClasses, translateKey, data, includeAir);
        this.tier = tier;
    }

    public SIMultiblock(String modId, Class<?>[] modClasses, String translateKey, String filePath, boolean includeAir, Tier tier) {
        super(modId, modClasses, translateKey, filePath, includeAir);
        this.tier = tier;
        CompoundTag subsTag = data.getCompound("Substitutions");
        int i = 0;
        for (Map.Entry<String, Tag<?>> blockTag : data.getCompound("Blocks").getValue().entrySet()) {
            for (String extraBlock : extraBlocks) {
                CompoundTag newSubTag = new CompoundTag();
                Block block = Block.getBlock(getBlockId((CompoundTag) blockTag.getValue()));
                if (block.hasTag(SignalIndustries.REPLACEABLE_CASING)) {
                    newSubTag.putCompound("pos", ((CompoundTag) blockTag.getValue()).getCompound("pos"));
                    newSubTag.putInt("meta", -1);
                    newSubTag.putBoolean("tile", false);
                    newSubTag.putString("id", SIBlocks.class.getName() + ":" + extraBlock);
                    subsTag.putCompound(String.valueOf(i), newSubTag);
                    i++;
                }
            }
        }
    }


}
