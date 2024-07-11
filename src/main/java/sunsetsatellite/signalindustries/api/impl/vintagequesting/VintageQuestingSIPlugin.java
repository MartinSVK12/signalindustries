package sunsetsatellite.signalindustries.api.impl.vintagequesting;

import net.minecraft.client.Minecraft;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.World;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.quest.template.ChapterTemplate;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.reward.ItemRewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.ClickTaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.RetrievalTaskTemplate;
import sunsetsatellite.vintagequesting.registry.ChapterRegistry;
import sunsetsatellite.vintagequesting.registry.QuestRegistry;
import sunsetsatellite.vintagequesting.registry.RewardRegistry;
import sunsetsatellite.vintagequesting.registry.TaskRegistry;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static sunsetsatellite.signalindustries.SIBlocks.*;
import static sunsetsatellite.signalindustries.SIItems.*;
import static sunsetsatellite.signalindustries.SignalIndustries.listOf;
import static sunsetsatellite.signalindustries.SignalIndustries.zip;

public class VintageQuestingSIPlugin {

    public void initializePlugin() {

        List<QuestTemplate> prototypeQuests = addPrototypeQuests();

        new ChapterTemplate("signalindustries:prototype",0, prototypeMachineCore,"chapter.signalindustries.prototype","chapter.signalindustries.prototype",
                prototypeQuests);
        new ChapterTemplate("signalindustries:basic",1, basicMachineCore,"chapter.signalindustries.basic","chapter.signalindustries.basic",
                listOf());
        new ChapterTemplate("signalindustries:reinforced",2, reinforcedMachineCore,"chapter.signalindustries.reinforced","chapter.signalindustries.reinforced",
                listOf());
        new ChapterTemplate("signalindustries:awakened",3, awakenedMachineCore,"chapter.signalindustries.awakened","chapter.signalindustries.awakened",
                listOf());

        for (QuestTemplate quest : VintageQuesting.QUESTS) {
            if(Objects.equals(quest.getTranslatedName(), quest.getName() + ".name") || Objects.equals(quest.getTranslatedDescription(), quest.getDescription() + ".desc")) {
                System.out.printf(quest.getName()+".name="+quest.getIcon().getDefaultStack().getDisplayName()+"\n");
                System.out.printf(quest.getDescription()+".desc="+"\n");
            }
        }

        VintageQuesting.LOGGER.info("Loaded quests from: signalindustries!");

    }

    public List<QuestTemplate> addPrototypeQuests(){
        QuestTemplate welcome = new QuestTemplate("signalindustries:welcome","quest.signalindustries.welcome", rawSignalumCrystal, Logic.AND, Logic.AND)
                .setTasks(listOf(new ClickTaskTemplate("signalindustries:welcome/click")));

        QuestTemplate genesis = simpleQuest("genesis",signalumOre,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(rawSignalumCrystal,32)
                        )),
                zip(listOf("reward"),
                        listOf(
                                new ItemStack(rawSignalumCrystal,32)
                        )),
                listOf(welcome),welcome,
                0,
                64);

        QuestTemplate hammer = simpleQuest("hammer",ironPlateHammer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(ironPlateHammer,1)
                        )),
                listOf(welcome),genesis,
                0,
                64);

        QuestTemplate cobblePlates = simpleQuest("cobblePlates",cobblestonePlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(cobblestonePlate,4)
                        )),
                listOf(hammer),hammer,
                -64,
                64);

        QuestTemplate stonePlates = simpleQuest("stonePlates",stonePlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(stonePlate,4)
                        )),
                listOf(hammer),hammer,
                64,
                64);

        QuestTemplate tablet = simpleQuest("tablet",configurationTablet,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(configurationTablet,1)
                        )),
                listOf(stonePlates,genesis),stonePlates,
                64,
                0);

        QuestTemplate ioConfig = simpleClickQuest("ioConfig",itemManipulationCircuit,
                listOf(tablet),tablet,
                64,
                0);

        QuestTemplate prototypeCore = simpleQuest("prototypeCore",prototypeMachineCore,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeMachineCore,1)
                        )),
                listOf(genesis,cobblePlates,stonePlates),hammer,
                0,
                128);

        QuestTemplate protoExtract = simpleQuest("prototypeExtractor", prototypeExtractor,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeExtractor,1)
                        )),
                listOf(prototypeCore),prototypeCore,
                0,
                64);

        QuestTemplate energy = simpleClickQuest("energy",energyFlowing,
                listOf(protoExtract),protoExtract,
                0,
                64);

        QuestTemplate protoPlate = simpleQuest("prototypePlateFormer", prototypePlateFormer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypePlateFormer,1)
                        )),
                listOf(prototypeCore),prototypeCore,
                64,
                64);

        QuestTemplate protoSmelter = simpleQuest("prototypeAlloySmelter", prototypeAlloySmelter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeAlloySmelter,1)
                        )),
                listOf(prototypeCore),prototypeCore,
                -64,
                64);

        QuestTemplate protoCrusher = simpleQuest("prototypeCrusher", prototypeCrusher,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeCrusher,1)
                        )),
                listOf(prototypeCore),prototypeCore,
                128,
                64);

        QuestTemplate protoCutter = simpleQuest("prototypeCrystalCutter", prototypeCrystalCutter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeCrystalCutter,1)
                        )),
                listOf(prototypeCore),prototypeCore,
                -128,
                64);

        QuestTemplate energyConduit = simpleQuest("energyConduit", prototypeConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeConduit,4)
                        )),
                listOf(prototypeCore),prototypeCore,
                64*4,
                64);

        QuestTemplate fluidConduit = simpleQuest("fluidConduit", prototypeFluidConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeFluidConduit,4)
                        )),
                listOf(prototypeCore),prototypeCore,
                64*5,
                64);

        QuestTemplate itemConduit = simpleQuest("itemConduit", prototypeItemConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeItemConduit,4)
                        )),
                listOf(prototypeCore),prototypeCore,
                64*6,
                64);

        QuestTemplate energyCell = simpleQuest("energyCell", prototypeEnergyCell,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeEnergyCell,1)
                        )),
                listOf(energyConduit),energyConduit,
                0,
                64);

        QuestTemplate fluidTank = simpleQuest("fluidTank", prototypeFluidTank,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeFluidTank,1)
                        )),
                listOf(fluidConduit),fluidConduit,
                0,
                64);

        QuestTemplate pump = simpleQuest("pump", prototypePump,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypePump,1)
                        )),
                listOf(fluidConduit),fluidConduit,
                0,
                128);

        QuestTemplate inserter = simpleQuest("inserter", prototypeInserter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeInserter,1)
                        )),
                listOf(itemConduit),itemConduit,
                0,
                64);

        QuestTemplate storageContainer = simpleQuest("storageContainer", prototypeStorageContainer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeStorageContainer,1)
                        )),
                listOf(prototypeCore),itemConduit,
                64,
                64);

        QuestTemplate crystal = simpleQuest("crystal", signalumCrystal,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(signalumCrystal,1)
                        )),
                listOf(protoCutter),energy,
                -16,
                64).setWidth(64).setHeight(64).setIconSize(2);

        QuestTemplate crystalBattery = simpleQuest("crystalBattery", signalumCrystalBattery,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(signalumCrystalBattery,1)
                        )),
                listOf(crystal),crystal,
                80,
                16);

        return listOf(
                welcome,genesis,hammer,cobblePlates,stonePlates,
                tablet,ioConfig,prototypeCore,protoPlate,protoCutter,protoCrusher,
                protoExtract,protoSmelter,energy,energyConduit,fluidConduit,
                itemConduit,energyCell,fluidTank,pump,inserter,storageContainer,
                crystal,crystalBattery
        );
    }

    public QuestTemplate simpleQuest(
            String id,
            IItemConvertible icon,
            List<Pair<String,ItemStack>> tasks,
            List<QuestTemplate> preRequisites,
            QuestTemplate offsetQuest,
            int xOffset,
            int yOffset
    ) {
        List<TaskTemplate> retrievalTasks = new ArrayList<>();
        for (Pair<String, ItemStack> task : tasks) {
            retrievalTasks.add(new RetrievalTaskTemplate("signalindustries:"+id+"/"+task.getLeft(),task.getRight()));
        }
        return new QuestTemplate("signalindustries:"+id,"quest.signalindustries."+id,icon,Logic.AND,Logic.AND)
                .setPreRequisites(preRequisites)
                .setTasks(retrievalTasks)
                .setX(offsetQuest,xOffset)
                .setY(offsetQuest,yOffset);
    }

    public QuestTemplate simpleQuest(
            String id,
            IItemConvertible icon,
            List<Pair<String,ItemStack>> tasks,
            List<Pair<String,ItemStack>> rewards,
            List<QuestTemplate> preRequisites,
            QuestTemplate offsetQuest,
            int xOffset,
            int yOffset
    ) {
        List<TaskTemplate> retrievalTasks = new ArrayList<>();
        for (Pair<String, ItemStack> task : tasks) {
            retrievalTasks.add(new RetrievalTaskTemplate("signalindustries:"+id+"/"+task.getLeft(),task.getRight()));
        }
        List<RewardTemplate> itemRewards = new ArrayList<>();
        for (Pair<String, ItemStack> task : tasks) {
            itemRewards.add(new ItemRewardTemplate("signalindustries:"+id+"/"+task.getLeft(),task.getRight()));
        }
        return new QuestTemplate("signalindustries:"+id,"quest.signalindustries."+id,icon,Logic.AND,Logic.AND)
                .setPreRequisites(preRequisites)
                .setTasks(retrievalTasks)
                .setRewards(itemRewards)
                .setX(offsetQuest,xOffset)
                .setY(offsetQuest,yOffset);
    }

    public QuestTemplate simpleClickQuest(
            String id,
            IItemConvertible icon,
            List<QuestTemplate> preRequisites,
            QuestTemplate offsetQuest,
            int xOffset,
            int yOffset
    ) {
        return new QuestTemplate("signalindustries:"+id,"quest.signalindustries."+id,icon,Logic.AND,Logic.AND)
                .setPreRequisites(preRequisites)
                .setTasks(listOf(new ClickTaskTemplate("signalindustries:"+id+"/click")))
                .setX(offsetQuest,xOffset)
                .setY(offsetQuest,yOffset);
    }

    public void reset() {
        Minecraft mc = Minecraft.getMinecraft(this);
        IHasQuests player = (IHasQuests) mc.thePlayer;
        World world = mc.theWorld;
        player.setCurrentChapter(null);
        player.getQuestGroup().chapters.clear();
        VintageQuesting.QUESTS = new QuestRegistry();
        VintageQuesting.TASKS = new TaskRegistry();
        VintageQuesting.REWARDS = new RewardRegistry();
        VintageQuesting.CHAPTERS = new ChapterRegistry();
        initializePlugin();
        for (ChapterTemplate chapter : VintageQuesting.CHAPTERS) {
            player.getQuestGroup().chapters.add(chapter.getInstance());
        }
        player.loadData(VintageQuesting.playerData);
    }
}
