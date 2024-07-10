package sunsetsatellite.signalindustries.api.impl.vintagequesting;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.animal.EntityPig;
import net.minecraft.core.entity.monster.EntitySlime;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import sunsetsatellite.vintagequesting.quest.template.ChapterTemplate;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;
import sunsetsatellite.vintagequesting.quest.template.reward.ItemRewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.ClickTaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.CraftingTaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.KillTaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.RetrievalTaskTemplate;
import sunsetsatellite.vintagequesting.util.Logic;

import static sunsetsatellite.signalindustries.SignalIndustries.listOf;

public class VintageQuestingSIPlugin {

    public void initializePlugin(Logger logger) {
        ItemRewardTemplate reward = new ItemRewardTemplate("vintagequesting:reward1",new ItemStack(Item.basket));
        ClickTaskTemplate clickTask = new ClickTaskTemplate("vintagequesting:click1");
        RetrievalTaskTemplate retrievalTask = new RetrievalTaskTemplate("vintagequesting:retrieval1",new ItemStack(Block.netherrackIgneous,32));
        RetrievalTaskTemplate retrievalTask2 = new RetrievalTaskTemplate("vintagequesting:retrieval2",new ItemStack(Block.blockDiamond,16)).setConsume();
        CraftingTaskTemplate craftingTask = new CraftingTaskTemplate("vintagequesting:crafting1",new ItemStack(Block.workbench,8));
        KillTaskTemplate killTask = new KillTaskTemplate("vintagequesting:kill1", EntityPig.class,4);
        QuestTemplate quest = new QuestTemplate("vintagequesting:test","quest.vq.test","quest.vq.test",Block.netherrackIgneous,Logic.AND, Logic.AND).setTasks(listOf(clickTask,retrievalTask,retrievalTask2,craftingTask,killTask)).setRewards(listOf(reward));
        ChapterTemplate chapter = new ChapterTemplate("vintagequesting:test",Block.dirt,"chapter.vq.test","chapter.vq.test",listOf(quest));

        ItemRewardTemplate reward2 = new ItemRewardTemplate("vintagequesting:reward2",new ItemStack(Block.torchRedstoneIdle));
        ClickTaskTemplate clickTask2 = new ClickTaskTemplate("vintagequesting:click2");
        RetrievalTaskTemplate retrievalTask3 = new RetrievalTaskTemplate("vintagequesting:retrieval3",new ItemStack(Block.slabBrickStonePolished,32));
        RetrievalTaskTemplate retrievalTask4 = new RetrievalTaskTemplate("vintagequesting:retrieval4",new ItemStack(Block.trommelIdle,16)).setConsume();
        CraftingTaskTemplate craftingTask2 = new CraftingTaskTemplate("vintagequesting:crafting2",new ItemStack(Block.furnaceBlastIdle,8));
        KillTaskTemplate killTask2 = new KillTaskTemplate("vintagequesting:kill2", EntitySlime.class,4);
        QuestTemplate quest2 = new QuestTemplate("vintagequesting:test2","quest.vq.test2","quest.vq.test2",Block.bedrock,Logic.AND,Logic.AND)
                .setTasks(listOf(clickTask2,retrievalTask3,retrievalTask4,craftingTask2,killTask2))
                .setRewards(listOf(reward2))
                .setPreRequisites(listOf(quest))
                .setX(64);
        ChapterTemplate chapter2 = new ChapterTemplate("vintagequesting:test2",Block.blockDiamond,"chapter.vq.test2","chapter.vq.test2",listOf(quest,quest2));
    }
}
