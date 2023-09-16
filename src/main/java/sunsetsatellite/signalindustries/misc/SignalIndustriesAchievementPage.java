package sunsetsatellite.signalindustries.misc;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.AchievementList;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.util.achievements.AchievementPage;
import turniplabs.halplibe.util.achievements.GuiAchievements;

import java.util.Random;

public class SignalIndustriesAchievementPage extends AchievementPage {
    public SignalIndustriesAchievementPage() {
        super("Signal Industries", "achievements.page.signalindustries");
        achievementList.add((Achievement) INIT.registerStat());
        achievementList.add((Achievement) THE_PROTOTYPE.registerStat());
        achievementList.add((Achievement) FROM_WITHIN.registerStat());
    }

    public static final Achievement INIT = new Achievement(AchievementList.achievementList.size()+1,SignalIndustries.key("init"),0,0,SignalIndustries.rawSignalumCrystal,null);
    public static final Achievement THE_PROTOTYPE = new Achievement(AchievementList.achievementList.size()+2,SignalIndustries.key("thePrototype"),2,0,SignalIndustries.prototypeMachineCore,INIT);
    public static final Achievement FROM_WITHIN = new Achievement(AchievementList.achievementList.size()+3,SignalIndustries.key("fromWithin"),4,0,SignalIndustries.prototypeExtractor,THE_PROTOTYPE);

    @Override
    public void getBackground(GuiAchievements guiAchievements, Random random,  int iOffset, int jOffset, int blockX1, int blockY1, int blockX2, int blockY2) {
        for(int l7 = 0; l7 * 16 - blockY2 < 155; ++l7) {
            float f5 = 0.6F - (float)(blockY1 + l7) / 25.0F * 0.3F;
            GL11.glColor4f(f5, f5, f5, 1.0F);

            for(int i8 = 0; i8 * 16 - blockX2 < 224; ++i8) {
                random.setSeed((long)(1234 + blockX1 + i8));
                random.nextInt();
                int j8 = random.nextInt(1 + blockY1 + l7) + (blockY1 + l7) / 2;
                int k8 = Block.sand.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                if (j8 <= 37 && blockY1 + l7 != 35) {
                    if (j8 == 22) {
                        k8 = SignalIndustries.dilithiumOre.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 == 10) {
                        k8 = Block.oreIronBasalt.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 == 8) {
                        k8 = SignalIndustries.signalumOre.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 > 4) {
                        k8 = Block.basalt.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 > 0) {
                        k8 = Block.stone.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    }
                } else {
                    if (random.nextInt(2) == 0) {
                        k8 = SignalIndustries.dimensionalShardOre.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else {
                        k8 = SignalIndustries.realityFabric.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    }
                }

                guiAchievements.drawTexturedModalRect(iOffset + i8 * 16 - blockX2, jOffset + l7 * 16 - blockY2, k8 % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain, k8 / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain, 16, 16, TextureFX.tileWidthTerrain, 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain));
            }
        }
    }
}
