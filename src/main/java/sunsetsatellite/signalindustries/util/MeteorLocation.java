package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;

import java.util.HashMap;
import java.util.Map;

public record MeteorLocation(Type type, Vec3i location) {

	public record Type(String name, Block<?> block) {
		public static final Map<String, Type> TYPES = new HashMap<>();

		public static final Type IRON = new Type("IRON", Blocks.ORE_IRON_BASALT);
		public static final Type SIGNALUM = new Type("SIGNALUM", SIBlocks.signalumOre);
		public static final Type DILITHIUM = new Type("DILITHIUM", SIBlocks.dilithiumOre);
		public static final Type UNKNOWN = new Type("UNKNOWN", null);

		public Type(String name, Block<?> block) {
			this.name = name;
			this.block = block;
			TYPES.put(name, this);
		}

		public static Type fromName(String name) {
			return TYPES.getOrDefault(name, UNKNOWN);
		}

		public static Type getFromBlock(Block<?> block) {
			for (Map.Entry<String, Type> entry : TYPES.entrySet()) {
				String K = entry.getKey();
				Type V = entry.getValue();
				if (V.block == block) {
					return V;
				}
			}
			return UNKNOWN;
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
