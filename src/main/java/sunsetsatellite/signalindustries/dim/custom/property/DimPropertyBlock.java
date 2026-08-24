package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;

public class DimPropertyBlock extends DimPropertyBase {
    public Block<?> block;

    public DimPropertyBlock(Block<?> block) {
        this.block = block;
    }

    public DimPropertyBlock(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        try {
            block = Blocks.blockMap.get(NamespaceID.getPermanent(nbt.getString("Block")));
        } catch (HardIllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putString("Block", block.namespaceId().toString());
    }

    @Override
    public Block<?> get() {
        return block;
    }
}
