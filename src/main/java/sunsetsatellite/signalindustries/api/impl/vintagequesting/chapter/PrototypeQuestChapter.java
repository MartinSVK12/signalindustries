package sunsetsatellite.signalindustries.api.impl.vintagequesting.chapter;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.api.impl.vintagequesting.VintageQuestingSIPlugin;
import sunsetsatellite.vintagequesting.core.data.ChapterData;
import sunsetsatellite.vintagequesting.core.data.QuestData;


public class PrototypeQuestChapter extends ChapterData {

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
    public @NotNull ItemStack getIcon() {
        return SIBlocks.prototypeMachineCore.getDefaultStack();
    }

	@Override
	public QuestData getStartingQuest() {
		return VintageQuestingSIPlugin.getQuest("welcome");
	}
}
