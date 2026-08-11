package sunsetsatellite.signalindustries.api.impl.vintagequesting.chapter;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.api.impl.vintagequesting.VintageQuestingSIPlugin;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;

public class ReinforcedQuestChapter extends Chapter {

    public ReinforcedQuestChapter() {
        super("signalindustries:reinforced", 2);
    }

    @Override
    public @NotNull String getName() {
        return Catalyst.translateNameKey("chapter.signalindustries.reinforced");
    }

    @Override
    public @NotNull String getDescription() {
        return Catalyst.translateDescKey("chapter.signalindustries.reinforced");
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return SIBlocks.reinforcedMachineCore.getDefaultStack();
    }


    @Override
    public Quest getStartingQuest() {
        return getQuest(VintageQuestingSIPlugin.getQuest("reinforcedAlloy"));
    }

}
