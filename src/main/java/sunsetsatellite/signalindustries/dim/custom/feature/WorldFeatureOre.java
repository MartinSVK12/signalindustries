package sunsetsatellite.signalindustries.dim.custom.feature;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;
import sunsetsatellite.signalindustries.dim.custom.DecorationContext;
import sunsetsatellite.signalindustries.dim.custom.property.DimPropertyBlock;
import sunsetsatellite.signalindustries.dim.custom.property.DimPropertyFloat;
import sunsetsatellite.signalindustries.dim.custom.property.DimPropertyInt;

import java.util.Random;

public class WorldFeatureOre extends WorldFeatureBase {
    public Block<?> oreBlock;
    public int oreAmount;
    public int baseRepeatAmount;
    public float heightModifier;

    public WorldFeatureOre(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public boolean place(World world, Random random, int baseX, int baseY, int baseZ, DecorationContext context) {
        for (int i = 0; i < baseRepeatAmount * context.oreHeightModifier; ++i) {
            int x = baseX + random.nextInt(16);
            int y = context.minY + random.nextInt((int) (context.rangeY / heightModifier));
            int z = baseZ + random.nextInt(16);
            actuallyPlace(world, random, x, y, z, context);
        }
        return true;
    }

    private void actuallyPlace(World world, Random random, int xStart, int yStart, int zStart, DecorationContext context) {
        float f = random.nextFloat() * (float)Math.PI;
        double xMax = (float)(xStart + 8) + MathHelper.sin(f) * (float)this.oreAmount / 8.0F;
        double xMin = (float)(xStart + 8) - MathHelper.sin(f) * (float)this.oreAmount / 8.0F;
        double zMax = (float)(zStart + 8) + MathHelper.cos(f) * (float)this.oreAmount / 8.0F;
        double zMin = (float)(zStart + 8) - MathHelper.cos(f) * (float)this.oreAmount / 8.0F;
        double yMax = yStart + random.nextInt(3) + 2;
        double yMin = yStart - random.nextInt(3) + 2;

        for(int l = 0; l <= this.oreAmount; ++l) {
            double d6 = xMax + (xMin - xMax) * (double)l / (double)this.oreAmount;
            double d7 = yMax + (yMin - yMax) * (double)l / (double)this.oreAmount;
            double d8 = zMax + (zMin - zMax) * (double)l / (double)this.oreAmount;
            double d9 = random.nextDouble() * (double)this.oreAmount / (double)16.0F;
            double d10 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.oreAmount) + 1.0F) * d9 + (double)1.0F;
            double d11 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.oreAmount) + 1.0F) * d9 + (double)1.0F;
            int xVeinStart = MathHelper.floor(d6 - d10 / (double)2.0F);
            int yVeinStart = MathHelper.floor(d7 - d11 / (double)2.0F);
            int zVeinStart = MathHelper.floor(d8 - d10 / (double)2.0F);
            int xVeinEnd = MathHelper.floor(d6 + d10 / (double)2.0F);
            int yVeinEnd = MathHelper.floor(d7 + d11 / (double)2.0F);
            int zVeinEnd = MathHelper.floor(d8 + d10 / (double)2.0F);

            for(int x = xVeinStart; x <= xVeinEnd; ++x) {
                double d12 = ((double)x + (double)0.5F - d6) / (d10 / (double)2.0F);
                if (!(d12 * d12 >= (double)1.0F)) {
                    for(int y = yVeinStart; y <= yVeinEnd; ++y) {
                        double d13 = ((double)y + (double)0.5F - d7) / (d11 / (double)2.0F);
                        if (!(d12 * d12 + d13 * d13 >= (double)1.0F)) {
                            for(int z = zVeinStart; z <= zVeinEnd; ++z) {
                                double d14 = ((double)z + (double)0.5F - d8) / (d10 / (double)2.0F);
                                if (d12 * d12 + d13 * d13 + d14 * d14 < (double)1.0F) {
                                    int id = world.getBlockId(x, y, z);
                                    if (id == Blocks.STONE.id() || id == Blocks.COBBLE_NETHERRACK.id() || id == Blocks.BASALT.id() || id == Blocks.LIMESTONE.id() || id == Blocks.GRANITE.id()) {
                                        world.setBlock(x, y, z, this.oreBlock.id());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        oreBlock = this.data.getProperty("OreBlock", tag, DimPropertyBlock.class, Blocks.STONE);
        oreAmount = this.data.getProperty("OreAmount", tag, DimPropertyInt.class, 0);
        baseRepeatAmount = this.data.getProperty("BaseRepeatAmount", tag, DimPropertyInt.class, 0);
        heightModifier = this.data.getProperty("HeightModifier", tag, DimPropertyFloat.class, 1);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        this.data.saveProperty("OreBlock", new DimPropertyBlock(oreBlock), tag);
        this.data.saveProperty("OreAmount", new DimPropertyInt(oreAmount), tag);
        this.data.saveProperty("BaseRepeatAmount", new DimPropertyInt(baseRepeatAmount), tag);
        this.data.saveProperty("HeightModifier", new DimPropertyFloat(heightModifier), tag);
    }
}
