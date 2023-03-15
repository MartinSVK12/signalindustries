package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class BlockOreSignalum extends Block {
    public BlockOreSignalum(int i) {
        super(i, Material.rock);
    }

    @Override
    public int idDropped(int i, Random random) {
        return SignalIndustries.rawSignalumCrystal.itemID;
    }

    @Override
    public int quantityDropped(int metadata, Random random) {
        return random.nextInt(2) + 1;
    }
}
