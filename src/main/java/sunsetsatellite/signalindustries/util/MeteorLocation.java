package sunsetsatellite.signalindustries.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.SIBlocks;

@NotNull
public class MeteorLocation {
    public final Type type;
    public final ChunkCoordinates location;

    public MeteorLocation(Type type, ChunkCoordinates location) {
        this.type = type;
        this.location = location;
    }

    public enum Type {
        IRON,
        SIGNALUM,
        DILITHIUM,
        UNKNOWN;

        public static Type getFromBlock(Block block){
            if(block == SIBlocks.signalumOre){
                return SIGNALUM;
            } else if (block == SIBlocks.dilithiumOre) {
                return DILITHIUM;
            } else if (block == Block.oreIronBasalt) {
                return IRON;
            } else {
                return UNKNOWN;
            }
        }

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
