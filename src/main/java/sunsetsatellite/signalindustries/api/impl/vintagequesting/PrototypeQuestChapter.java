package sunsetsatellite.signalindustries.api.impl.vintagequesting;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.vintagequesting.gui.QuestChapterPage;
import sunsetsatellite.vintagequesting.gui.ScreenQuestbook;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;

import java.util.Random;

public class PrototypeQuestChapter extends QuestChapterPage {

    public PrototypeQuestChapter() {
        super("signalindustries:prototype", 0);
    }

    @Override
    public @NotNull String getName() {
        return Catalyst.translateNameKey("chapter.signalindustries.prototype");
    }

    @Override
    public @NotNull String getDescription() {
        return Catalyst.translateDescKey("chapter.signalindustries.prototype");
    }

    @Override
    public @Nullable IconCoordinate getBackgroundTile(ScreenQuestbook screen, int layer, Random random, int tileX, int tileY) {
        return TextureRegistry.getTexture(SIBlocks.blockTextures.get(SIBlocks.cobblestoneBricks).defaultTextures.get(Side.NORTH));
    }

    @Override
    public void postProcessBackground(ScreenQuestbook screen, Random random, ScreenQuestbook.BGLayer layerCache, int orgX, int orgY) {

    }

    @Override
    public @NotNull ItemStack getIcon() {
        return SIBlocks.prototypeMachineCore.getDefaultStack();
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
    public Quest getStartingQuest() {
        return getQuest(VintageQuestingSIPlugin.getQuest("welcome"));
    }

    @Override
    public IconCoordinate getQuestBackground(QuestTemplate quest) {
        return TextureRegistry.getTexture(quest.getType().texture());
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
