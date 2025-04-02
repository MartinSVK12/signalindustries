package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;

public class MeteorLocation {
    public final Type type;
    public final Vec3i location;

    public MeteorLocation(Type type, Vec3i location) {
        this.type = type;
        this.location = location;
    }

    public enum Type {
        IRON,
        SIGNALUM,
        DILITHIUM,
        UNKNOWN;

        public static Type getFromBlock(Block<?> block){
            if(block == SIBlocks.signalumOre){
                return SIGNALUM;
            } else if (block == SIBlocks.dilithiumOre) {
                return DILITHIUM;
            } else if (block == Blocks.ORE_IRON_BASALT) {
                return IRON;
            } else {
                return UNKNOWN;
            }
        }

    }

    public void write(CompoundTag tag) {
        tag.putInt("x", location.x);
        tag.putInt("y", location.y);
        tag.putInt("z", location.z);
        tag.putString("type", type.name());
    }

    public static MeteorLocation read(CompoundTag tag) {
        Vec3i location = new Vec3i(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
        Type type = Type.valueOf(tag.getString("type"));
        return new MeteorLocation(type, location);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeteorLocation)) return false;

        MeteorLocation that = (MeteorLocation) o;
        return type == that.type && location.equals(that.location);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }
}
