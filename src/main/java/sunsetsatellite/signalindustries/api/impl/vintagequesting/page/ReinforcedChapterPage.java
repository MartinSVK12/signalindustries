package sunsetsatellite.signalindustries.api.impl.vintagequesting.page;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.vintagequesting.client.gui.ChapterPage;
import sunsetsatellite.vintagequesting.client.gui.ScreenQuestbook;
import sunsetsatellite.vintagequesting.core.Chapter;

import java.util.Random;

public class ReinforcedChapterPage extends ChapterPage {
	public ReinforcedChapterPage(Chapter chapter) {
		super(chapter);
	}

	@Override
	public @Nullable IconCoordinate getBackgroundTile(ScreenQuestbook screen, int layer, Random random, int tileX, int tileY) {
		return TextureRegistry.getTexture(SIBlocks.blockTextures.get(SIBlocks.reinforcedCasing).defaultTextures.get(Side.NORTH));
	}

	@Override
	public void postProcessBackground(ScreenQuestbook screen, Random random, ScreenQuestbook.BGLayer layerCache, int orgX, int orgY) {

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
	public int lineColorLocked(boolean isHovered) {
		return 0xFFFFFF;
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
