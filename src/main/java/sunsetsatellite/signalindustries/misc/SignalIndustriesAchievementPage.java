package sunsetsatellite.signalindustries.misc;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.stat.Stat;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIBlocks;
import turniplabs.halplibe.util.achievements.AchievementPage;
import turniplabs.halplibe.util.achievements.GuiAchievements;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

public class SignalIndustriesAchievementPage extends AchievementPage {

    public static int nextAchievementID = 69;
    public static int offsetY = 5;

    public SignalIndustriesAchievementPage() {
        super("Signal Industries", "achievements.page.signalindustries");
    }

    @Override
    public void getBackground(GuiAchievements guiAchievements, Random random, int iOffset, int jOffset, int blockX1, int blockY1, int blockX2, int blockY2) {
        for(int row = 0; row * 16 - blockY2 < 155; ++row) {
            float brightness = 0.6F - (float)(blockY1 + row) / 25.0F * 0.3F;
            GL11.glColor4f(brightness, brightness, brightness, 1.0F);

            for(int column = 0; column * 16 - blockX2 < 224; ++column) {
                random.setSeed(1234 + blockX1 + column);
                random.nextInt();
                int randomY = random.nextInt(1 + blockY1 + row) + (blockY1 + row) / 2;
                IconCoordinate texture = this.getTextureFromBlock(Block.stone);
                Block[] oreArray = stoneOres;
                if (randomY >= 28 || blockY1 + row > 24) {
                    oreArray = basaltOres;
                }

                if (randomY <= 37 && blockY1 + row != 35) {
                    if (randomY == 22) {
                        if (random.nextInt(2) == 0) {
                            texture = this.getTextureFromBlock(oreArray[3]);
                        } else {
                            texture = this.getTextureFromBlock(oreArray[4]);
                        }
                    } else if (randomY == 10) {
                        texture = this.getTextureFromBlock(oreArray[1]);
                    } else if (randomY == 8) {
                        texture = this.getTextureFromBlock(oreArray[0]);
                    } else if (randomY > 4) {
                        texture = this.getTextureFromBlock(Block.stone);
                        if (randomY >= 28 || blockY1 + row > 24) {
                            texture = this.getTextureFromBlock(Block.basalt);
                        }
                    } else if (randomY > 0) {
                        texture = this.getTextureFromBlock(Block.stone);
                    }
                } else {
                    texture = this.getTextureFromBlock(SIBlocks.realityFabric);
                }


            }
        }

    }

    private final Block[] stoneOres;
    private final Block[] basaltOres;

    {
        stoneOres = new Block[]{Block.oreIronStone};
        basaltOres = new Block[]{Block.oreIronBasalt, SIBlocks.signalumOre, SIBlocks.dilithiumOre};
    }

    protected IconCoordinate getTextureFromBlock(Block block) {
        return ((BlockModel<?>) BlockModelDispatcher.getInstance().getDispatch(block)).getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
    }
}
