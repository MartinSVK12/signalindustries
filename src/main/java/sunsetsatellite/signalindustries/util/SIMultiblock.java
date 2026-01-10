package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystMultiblocks;
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
        SignalIndustries.LOGGER.info(String.format("Multiblock '%s' contains %d blocks.",translateKey,this.data.getCompound("Blocks").getValues().size()));
        this.tier = tier;
        CompoundTag subsTag = this.data.getCompound("Substitutions");
        int i = 0;
        for (Map.Entry<String, Tag<?>> blockTag : this.data.getCompound("Blocks").getValue().entrySet()) {
            for (String extraBlock : extraBlocks) {
                CompoundTag newSubTag = new CompoundTag();
                Block<?> block = Blocks.getBlock(getBlockId((CompoundTag) blockTag.getValue()));
                if (block != null && block.hasTag(SignalIndustries.REPLACEABLE_CASING)) {
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

    public SIMultiblock(String modId, Class<?>[] modClasses, String translateKey, String filePath, boolean includeAir, Tier tier) {
        super(modId, modClasses, translateKey, filePath, includeAir);
        SignalIndustries.LOGGER.info(String.format("Multiblock '%s' contains %d blocks.",translateKey,this.data.getCompound("Blocks").getValues().size()));
        this.tier = tier;
        CompoundTag subsTag = data.getCompound("Substitutions");
        int i = 0;
        for (Map.Entry<String, Tag<?>> blockTag : data.getCompound("Blocks").getValue().entrySet()) {
            for (String extraBlock : extraBlocks) {
                CompoundTag newSubTag = new CompoundTag();
                Block<?> block = Blocks.getBlock(getBlockId((CompoundTag) blockTag.getValue()));
                if (block != null && block.hasTag(SignalIndustries.REPLACEABLE_CASING)) {
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
