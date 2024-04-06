package sunsetsatellite.signalindustries.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.AchievementList;
import net.minecraft.core.achievement.stat.Stat;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.util.achievements.AchievementPage;
import turniplabs.halplibe.util.achievements.GuiAchievements;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SignalIndustriesAchievementPage extends AchievementPage {

    public static int nextAchievementID = 69;
    public static int offsetY = 5;

    public SignalIndustriesAchievementPage() {
        super("Signal Industries", "achievements.page.signalindustries");
        Field[] achievements = SignalIndustriesAchievementPage.class.getDeclaredFields();
        Arrays.stream(achievements).filter((F)->F.getType().equals(Achievement.class)).forEach((F)->{
            try {
                achievementList.add((Achievement) ((Stat) F.get(null)).registerStat());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    public static final Achievement INIT = new Achievement(nextAchievementID++, key("init"),0,0-offsetY, rawSignalumCrystal,null);
    public static final Achievement THE_PROTOTYPE = new Achievement(nextAchievementID++, key("thePrototype"),2,0-offsetY, prototypeMachineCore,INIT);
    public static final Achievement FROM_WITHIN = new Achievement(nextAchievementID++, key("fromWithin"),3,-1-offsetY, prototypeExtractor,THE_PROTOTYPE);
    public static final Achievement TRANSFER = new Achievement(nextAchievementID++, key("transfer"),4,1-offsetY, prototypeConduit,THE_PROTOTYPE);
    public static final Achievement BUFFER = new Achievement(nextAchievementID++, key("buffer"),5,-1-offsetY, prototypeEnergyCell,THE_PROTOTYPE);
    public static final Achievement CRUSHER = new Achievement(nextAchievementID++, key("crusher"),6,1-offsetY, prototypeCrusher,THE_PROTOTYPE);
    public static final Achievement ALLOY_SMELTER = new Achievement(nextAchievementID++, key("alloySmelter"),7,-1-offsetY, prototypeAlloySmelter,THE_PROTOTYPE);
    public static final Achievement PLATE_FORMER = new Achievement(nextAchievementID++, key("plateFormer"),8,1-offsetY, prototypePlateFormer,THE_PROTOTYPE);
    public static final Achievement SHINING = new Achievement(nextAchievementID++, key("shining"),9,0-offsetY, signalumCrystal,THE_PROTOTYPE);
    public static final Achievement BASIC = new Achievement(nextAchievementID++, key("basic"),2,2-offsetY, basicMachineCore,SHINING);
    public static final Achievement ROM_CHIP = new Achievement(nextAchievementID++, key("romChip"),-2,6-offsetY, romChipBoost,null);
    public static final Achievement COMBINED = new Achievement(nextAchievementID++, key("combined"),4,3-offsetY, basicCrystalChamber,BASIC);
    public static final Achievement MINER = new Achievement(nextAchievementID++, key("miner"),6,3-offsetY, basicAutomaticMiner,BASIC);
    public static final Achievement PUMP = new Achievement(nextAchievementID++, key("pump"),8,3-offsetY, basicPump,BASIC);
    public static final Achievement HARNESS = new Achievement(nextAchievementID++, key("harness"),3,5-offsetY, signalumPrototypeHarness,BASIC);
    public static final Achievement PROGRAMMER = new Achievement(nextAchievementID++, key("programmer"),5,5-offsetY, basicProgrammer,ROM_CHIP);
    public static final Achievement TRIGGER = new Achievement(nextAchievementID++, key("trigger"),7,5-offsetY, nullTrigger,PROGRAMMER);
    public static final Achievement CHALLENGE = new Achievement(nextAchievementID++, key("challenge"),11,5-offsetY, basicWrathBeacon,BASIC);
    public static final Achievement VICTORY = new Achievement(nextAchievementID++, key("victory"),13,5-offsetY, energyCatalyst,BASIC);
    public static final Achievement RELIC = new Achievement(nextAchievementID++, key("relic"),9,4-offsetY, glowingObsidian,BASIC);
    public static final Achievement KNIGHTS_ALLOY = new Achievement(nextAchievementID++, key("knightAlloy"),9,6-offsetY, reinforcedCrystalAlloyIngot,RELIC);
    public static final Achievement REINFORCED = new Achievement(nextAchievementID++, key("reinforced"),9,8-offsetY, reinforcedMachineCore,KNIGHTS_ALLOY);
    public static final Achievement VICTORY_REINFORCED = new Achievement(nextAchievementID++, key("victory.reinforced"),11,7-offsetY, reinforcedWrathBeacon,REINFORCED);
    public static final Achievement BLADE = new Achievement(nextAchievementID++, key("blade"),7,7-offsetY, signalumSaber,REINFORCED);
    public static final Achievement PULSE = new Achievement(nextAchievementID++, key("pulse"),5,7-offsetY, pulsar,REINFORCED);
    public static final Achievement POWER_SUIT = new Achievement(nextAchievementID++, key("powerSuit"),3,7-offsetY, signalumPowerSuitChestplate,REINFORCED);
    public static final Achievement DILITHIUM = new Achievement(nextAchievementID++, key("dilithium"),7,9-offsetY, dilithiumShard,REINFORCED);
    public static final Achievement DIMENSIONAl = new Achievement(nextAchievementID++, key("dimensional"),11,9-offsetY, dimensionalShard,REINFORCED);
    public static final Achievement WARP_ORB = new Achievement(nextAchievementID++, key("warpOrb"),13,9-offsetY, warpOrb,DIMENSIONAl);
    public static final Achievement ANCHOR = new Achievement(nextAchievementID++, key("anchor"),15,9-offsetY, dimensionalAnchor,WARP_ORB);
    public static final Achievement TELEPORT_SUCCESS = new Achievement(nextAchievementID++, key("teleport.success"),17,8-offsetY, Block.grassRetro,ANCHOR);
    public static final Achievement TELEPORT_FAIL = new Achievement(nextAchievementID++, key("teleport.fail"),17,10-offsetY, realityFabric,ANCHOR);
    public static final Achievement ETERNITY = new Achievement(nextAchievementID++, key("eternity"),19,10-offsetY, rootedFabric,TELEPORT_FAIL);
    public static final Achievement FALSE_ETERNITY = new Achievement(nextAchievementID++, key("falseEternity"),17,12-offsetY, dimensionalShardOre,TELEPORT_FAIL);
    public static final Achievement BOOST = new Achievement(nextAchievementID++, key("boost"),5,9-offsetY, dilithiumBooster,DILITHIUM);
    public static final Achievement WINGS = new Achievement(nextAchievementID++, key("wings"),1,7-offsetY, crystalWings,POWER_SUIT);
    public static final Achievement HORIZONS = new Achievement(nextAchievementID++, key("horizons"),9,10-offsetY, reinforcedEnergyConnector,REINFORCED);
    public static final Achievement REACTOR = new Achievement(nextAchievementID++, key("reactor"),9,12-offsetY, signalumReactorCore,HORIZONS);
    public static final Achievement RISING_ABOVE = new Achievement(nextAchievementID++, key("comingSoon"),9,14-offsetY, awakenedSignalumFragment,REACTOR);


    public static final Achievement BLOOD_MOON = new Achievement(nextAchievementID++, key("bloodMoon"),-2,2-offsetY, monsterShard,null);
    public static final Achievement ECLIPSE = new Achievement(nextAchievementID++, key("eclipse"),-2,4-offsetY, infernalFragment,null);
    public static final Achievement STARFALL = new Achievement(nextAchievementID++, key("starfall"),-2,8-offsetY, Block.lampActive,null);

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
                        k8 = dilithiumOre.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 == 10) {
                        k8 = Block.oreIronBasalt.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 == 8) {
                        k8 = signalumOre.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 > 4) {
                        k8 = Block.basalt.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else if (j8 > 0) {
                        k8 = Block.stone.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    }
                } else {
                    if (random.nextInt(2) == 0) {
                        k8 = dimensionalShardOre.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    } else {
                        k8 = realityFabric.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0);
                    }
                }

                guiAchievements.drawTexturedModalRect(iOffset + i8 * 16 - blockX2, jOffset + l7 * 16 - blockY2, k8 % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain, k8 / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain, 16, 16, TextureFX.tileWidthTerrain, 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain));
            }
        }
    }
}
