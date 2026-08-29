package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;

import java.util.HashMap;
import java.util.Map;

public record MeteorLocation(Type type, Vec3i location) {

	public record Type(String name) {
		public static final Map<String, Type> TYPES = new HashMap<>();

		public static final Type IRON = new Type("IRON");
		public static final Type SIGNALUM = new Type("SIGNALUM");
		public static final Type DILITHIUM = new Type("DILITHIUM");
		public static final Type UNKNOWN = new Type("UNKNOWN");

		public Type(String name) {
			this.name = name;
			TYPES.put(name, this);
		}

		public static Type fromName(String name) {
			return TYPES.getOrDefault(name, UNKNOWN);
		}

		public static Type getFromBlock(Block<?> block) {
			if (block == SIBlocks.signalumOre) {
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
		Type type = Type.fromName(tag.getString("type"));
		return new MeteorLocation(type, location);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MeteorLocation that)) return false;

		return type == that.type && location.equals(that.location);
	}

}
