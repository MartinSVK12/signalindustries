package sunsetsatellite.signalindustries.util;

import net.minecraft.client.gui.achievements.ScreenAchievements;
import net.minecraft.client.gui.achievements.data.AchievementPage;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;

import java.util.Objects;
import java.util.Random;

public class AchievementPageSI extends AchievementPage {

	@Override
	public void addAchievement(@NotNull Achievement achievement, int x, int y) {
		super.addAchievement(achievement, x, y);
		achievement.registerAchievement();
	}

	@Override
    public @NotNull String getName() {
        return Catalyst.translateNameKey("gui.achievements.page.signalindustries");
    }

    @Override
    public @NotNull String getDescription() {
        return Catalyst.translateDescKey("gui.achievements.page.signalindustries");
    }

    @Override
    public @NotNull AchievementEntry onOpenAchievement() {
        return Objects.requireNonNull(getEntry(SIAchievements.INIT));
    }

    @Override
    public @Nullable IconCoordinate getBackgroundTile(ScreenAchievements screenAchievements, int i, Random random, int tileX, int tileY) {
		int offsetX = tileX + random.nextInt(6) - random.nextInt(6);
		int offsetY = tileY + random.nextInt(6) - random.nextInt(6);

		if(offsetY >= 50){
			if(random.nextInt(100) == 0){
				return TextureRegistry.getTexture("signalindustries:block/white");
			}
			return TextureRegistry.getTexture("signalindustries:block/gray");
		}
		if (offsetY >= 25) {
			if(random.nextInt(25) == 0){
				return getTextureFromBlock(SIBlocks.rootedFabric);
			}
			if(random.nextInt(100) == 0){
				return getTextureFromBlock(SIBlocks.dimensionalShardOre);
			}
			return getTextureFromBlock(SIBlocks.realityFabric);
		}

		if(random.nextInt(100) == 0){
			return getTextureFromBlock(SIBlocks.signalumOre);
		}
		if(random.nextInt(150) == 0){
			return getTextureFromBlock(SIBlocks.dilithiumOre);
		}
        return getTextureFromBlock(Blocks.BASALT);
    }

    @Override
    public void postProcessBackground(ScreenAchievements screenAchievements, Random random, ScreenAchievements.BGLayer bgLayer, int tileX, int tileY) {

    }

    @Override
    public @NotNull ItemStack getIcon() {
        return SIItems.signalumCrystal.getDefaultStack();
    }

    @Override
    public int backgroundLayers() {
        return 1;
    }

    @Override
    public int backgroundColor() {
        return 0;
    }

    @Override
    public IconCoordinate getAchievementIcon(Achievement achievement) {
        return TextureRegistry.getTexture(achievement.getType().texture());
    }

    @Override
    public int lineColorLocked(boolean isHovered) {
        return 0x000000;
    }

    @Override
    public int lineColorUnlocked(boolean isHovered) {
        return 0x707070;
    }

    @Override
    public int lineColorCanUnlock(boolean isHovered) {
        return 0x00ff00;
    }
}
