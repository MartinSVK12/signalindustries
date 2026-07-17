package sunsetsatellite.signalindustries.util;

import net.minecraft.client.gui.achievements.ScreenAchievements;
import net.minecraft.client.gui.achievements.data.AchievementPage;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIItems;

import java.util.Objects;
import java.util.Random;

public class AchievementPageSI extends AchievementPage {
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
    public @Nullable IconCoordinate getBackgroundTile(ScreenAchievements screenAchievements, int i, Random random, int j, int k) {
        return TextureRegistry.getTexture("signalindustries:block/reality_fabric");
    }

    @Override
    public void postProcessBackground(ScreenAchievements screenAchievements, Random random, ScreenAchievements.BGLayer bGLayer, int i, int j) {

    }

    @Override
    public @NotNull ItemStack getIcon() {
        return SIItems.signalumCrystal.getDefaultStack();
    }

    @Override
    public int backgroundLayers() {
        return 0;
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
