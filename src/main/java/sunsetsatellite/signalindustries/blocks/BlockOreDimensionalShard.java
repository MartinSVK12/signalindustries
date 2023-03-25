package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class BlockOreDimensionalShard extends Block {
    public BlockOreDimensionalShard(int i) {
        super(i, Material.rock);
    }

    @Override
    public int idDropped(int i, Random random) {
        return SignalIndustries.dimensionalShard.itemID;
    }

    @Override
    public int quantityDropped(int metadata, Random random) {
        return random.nextInt(1) + 1;
    }
}
