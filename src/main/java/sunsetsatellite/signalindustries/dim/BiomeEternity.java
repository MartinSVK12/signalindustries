package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.entity.SpawnListEntry;
import net.minecraft.core.enums.MobCategory;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.SurfaceProperties;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.SIBlocks;

import java.util.List;

public class BiomeEternity extends Biome {
	public BiomeEternity() {
		super("eternity");
		SurfaceProperties props = new SurfaceProperties.Builder()
			.withFillerBlock(SIBlocks.realityFabric)
			.withTopBlock(SIBlocks.realityFabric)
			.build();
		this.withSurfaceProperties(props).withDebugColor(0xA0A0A0);
		spawnableAmbientCreatureList.clear();
		spawnableMonsterList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCreatureList.clear();
	}

	@Override
	public @NotNull List<@NotNull SpawnListEntry> getSpawnableList(@NotNull MobCategory category) {
		return List.of();
	}
}
