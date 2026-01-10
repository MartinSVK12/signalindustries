package sunsetsatellite.signalindustries.screens.guidebook;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.SearchableGuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.screens.guidebook.pages.lore.IntroPage;
import sunsetsatellite.signalindustries.screens.guidebook.pages.recipe.*;
import sunsetsatellite.signalindustries.screens.guidebook.sections.*;

import java.util.ArrayList;
import java.util.List;

public class SignalIndustriesSection extends SearchableGuidebookSection {

    private final List<GuidebookPage> pages = new ArrayList<>();
    private final List<Index> indices = new ArrayList<>();
    private final List<SearchableGuidebookSubsection> subsections = new ArrayList<>();

    private final SearchableGuidebookSubsection extractor = new FluidMachineSection(this, SIRecipes.EXTRACTOR.getAllRecipes(), ExtractorPage.class);
    private final SearchableGuidebookSubsection crusher = new MachineSection(this, SIRecipes.CRUSHER.getAllRecipes(), CrusherPage.class);
    private final SearchableGuidebookSubsection wakingCrusher = new MachineSection(this, SIRecipes.WAKING_CRUSHER.getAllRecipes(), WakingCrusherPage.class);
    private final SearchableGuidebookSubsection alloySmelter = new MachineSection(this, SIRecipes.ALLOY_SMELTER.getAllRecipes(), AlloySmelterPage.class);
    private final SearchableGuidebookSubsection wakingAlloySmelter = new MachineSection(this, SIRecipes.WAKING_ALLOY_SMELTER.getAllRecipes(), WakingAlloySmelterPage.class);
    private final SearchableGuidebookSubsection plateFormer = new MachineSection(this, SIRecipes.PLATE_FORMER.getAllRecipes(), PlateFormerPage.class);
    private final SearchableGuidebookSubsection wakingPlateFormer = new MachineSection(this, SIRecipes.WAKING_PLATE_FORMER.getAllRecipes(), WakingPlateFormerPage.class);
    private final SearchableGuidebookSubsection pump = new FluidMachineSection(this, SIRecipes.PUMP.getAllRecipes(), PumpPage.class);
    private final SearchableGuidebookSubsection stoneworks = new MachineSection(this, SIRecipes.STONEWORKS.getAllRecipes(), StoneworksPage.class);
    private final SearchableGuidebookSubsection crystalCutter = new MachineSection(this, SIRecipes.CRYSTAL_CUTTER.getAllRecipes(), CrystalCutterPage.class);
    private final SearchableGuidebookSubsection crystalChamber = new MachineSection(this, SIRecipes.CRYSTAL_CHAMBER.getAllRecipes(), CrystalChamberPage.class);
    private final SearchableGuidebookSubsection infuser = new MachineSection(this, SIRecipes.INFUSER.getAllRecipes(), InfuserPage.class);
    private final SearchableGuidebookSubsection centrifuge = new MachineSection(this, SIRecipes.CENTRIFUGE.getAllRecipes(), CentrifugePage.class);
    private final SearchableGuidebookSubsection collector = new FluidMachineSection(this, SIRecipes.COLLECTOR.getAllRecipes(), CollectorPage.class);
    private final SearchableGuidebookSubsection wakingInfuser = new MachineSection(this, SIRecipes.WAKING_INFUSER.getAllRecipes(), WakingInfuserPage.class);
    private final SearchableGuidebookSubsection laserDrill = new MachineRandomOutputSection(this, SIRecipes.LASER_DRILL.getAllRecipes(), LaserDrillPage.class);
    private final SearchableGuidebookSubsection bonsaiPot = new MachineMultiOutputSection(this, SIRecipes.BONSAI_POT.getAllRecipes(), BonsaiPotPage.class);
    private final SearchableGuidebookSubsection greenhouse = new MachineMultiOutputSection(this, SIRecipes.GREENHOUSE.getAllRecipes(), GreenhousePage.class);


    private final SearchableGuidebookSubsection multiblocks = new MultiblockSection(this);

    public SignalIndustriesSection() {
        super("guidebook.section.signalindustries", new ItemStack(SIItems.signalumCrystal), 0xAA0000, 0xFF0000);
        reloadSection();
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.extractor"), extractor.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.collector"), collector.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.crusher"), crusher.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.wakingCrusher"), crusher.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.alloySmelter"), alloySmelter.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.wakingAlloySmelter"), alloySmelter.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.plateFormer"), plateFormer.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.wakingPlateFormer"), plateFormer.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.pump"), pump.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.stoneworks"), stoneworks.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.crystalCutter"), crystalCutter.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.crystalChamber"), crystalChamber.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.infuser"), infuser.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.wakingInfuser"), infuser.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.centrifuge"), centrifuge.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.laserDrill"), laserDrill.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.bonsaiPot"), bonsaiPot.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.greenhouse"), bonsaiPot.getPages().get(0)));

        indices.add(new Index(I18n.getInstance().translateKey("guidebook.section.signalindustries.multiblocks"), multiblocks.getPages().get(0)));

        subsections.add(extractor);
        subsections.add(collector);
        subsections.add(crusher);
        subsections.add(wakingCrusher);
        subsections.add(alloySmelter);
        subsections.add(wakingAlloySmelter);
        subsections.add(plateFormer);
        subsections.add(wakingPlateFormer);
        subsections.add(pump);
        subsections.add(stoneworks);
        subsections.add(crystalCutter);
        subsections.add(crystalChamber);
        subsections.add(infuser);
        subsections.add(wakingInfuser);
        subsections.add(centrifuge);
        subsections.add(laserDrill);
        subsections.add(bonsaiPot);
        subsections.add(greenhouse);
        subsections.add(multiblocks);
    }

    boolean filtered = false;

    public void reloadSection() {
        pages.clear();
        if (!filtered) pages.add(new IntroPage(this, "guidebook.section.signalindustries.intro"));
        filtered = false;
    }

    @Override
    public List<GuidebookPage> getPages() {
        ArrayList<GuidebookPage> list = new ArrayList<>(pages);
        for (SearchableGuidebookSubsection subsection : subsections) {
            if (subsection.getPages() != null) {
                list.addAll(subsection.getPages());
            }
        }
        return list;
    }

    @Override
    public List<Index> getIndices() {
        ArrayList<Index> list = new ArrayList<>(indices);
        for (SearchableGuidebookSubsection subsection : subsections) {
            if (subsection.getIndices() != null) {
                list.addAll(subsection.getIndices());
            }
        }
        return list;
    }

    @Override
    public List<GuidebookPage> searchPages(SearchQuery query) {
        filtered = true;
        reloadSection();
        ArrayList<GuidebookPage> list = new ArrayList<>(pages);
        for (SearchableGuidebookSubsection subsection : subsections) {
            List<GuidebookPage> searchList = subsection.searchPages(query);
            if (searchList != null && !searchList.isEmpty()) {
                list.addAll(searchList);
            }
        }
        return list;
    }
}
