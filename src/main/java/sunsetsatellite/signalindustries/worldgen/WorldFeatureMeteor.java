package sunsetsatellite.signalindustries.worldgen;


import net.minecraft.core.block.Blocks;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.ExplosionNoDrops;
import sunsetsatellite.signalindustries.util.MeteorLocation;

import java.util.Random;

public class WorldFeatureMeteor implements WorldFeatureInterface {

    public int oreId;
    public int oreMeta;
    public int oreChance;
    public int radius = 4;

    public WorldFeatureMeteor(int oreId, int oreMeta, int oreChance) {
        this.oreId = oreId;
        this.oreMeta = oreMeta;
        this.oreChance = oreChance;
    }

    public WorldFeatureMeteor(int oreId, int oreMeta, int oreChance, int radius) {
        this.oreId = oreId;
        this.oreMeta = oreMeta;
        this.oreChance = oreChance;
        this.radius = radius;
    }

    public boolean isPointInsideSphere(int x, int y, int z, double radius) {
        return x * x + y * y + z * z < radius * radius;
    }

	@Override
	public boolean place(@NotNull World world, @NotNull Random random, @NotNull TilePosc tilePos) {
		int i = tilePos.x();
		int j = tilePos.y();
		int k = tilePos.z();
		SignalIndustries.LOGGER.info("{} Meteor fell at X:{} Y:{} Z:{}", I18n.getInstance().translateKey(Blocks.blocksList[oreId].getLanguageKey(oreMeta)+".name"), i, j, k);
		ExplosionNoDrops ex = new ExplosionNoDrops(world, null, i, j, k, 50f);
		ex.explode();
		ex.addEffects(false);

		int oreBlocks = 0;

		for (int x = -radius; x <= radius; ++x) {
			for (int y = -radius; y <= radius; ++y) {
				for (int z = -radius; z <= radius; ++z) {
					if (isPointInsideSphere(x, y, z, radius)) {
						if (oreId != 0 && random.nextInt(100) < oreChance) {
							world.setBlockAndMetadataWithNotify(x + i, (y + j) - 8, z + k, oreId, oreMeta);
							//if(oreId == SIBlocks.signalumOre.id()){
							//    SignalIndustries.ORE_BLOCK_COUNT.compute(SIBlocks.signalumOre,(ignored,v) -> v == null ? 1 : v + 1);
							//}
							oreBlocks++;
						} else {
							world.setBlockAndMetadataWithNotify(x + i, (y + j) - 8, z + k, Blocks.BASALT.id(), 0);
						}
					}
				}
			}
		}

		SignalIndustries.addMeteorLocation(new MeteorLocation(MeteorLocation.Type.getFromBlock(Blocks.blocksList[oreId]), new Vec3i(i, j, k)));
		SignalIndustries.LOGGER.info("Meteor contains {} ore.", oreBlocks);
		return true;
	}
}
