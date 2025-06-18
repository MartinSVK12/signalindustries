package sunsetsatellite.signalindustries.blocks.color;

import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class BlockColorUnraveledFabric extends BlockColor {
    @Override
    public int getFallbackColor(int i) {
        return 0xFFFFFF;
    }

    @Override
    public int getWorldColor(WorldSource worldSource, int i, int j, int k) {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.intToIntARGB(255,r,g,b);
    }
}
