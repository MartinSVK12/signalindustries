package sunsetsatellite.signalindustries.gui.guidebook;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.SearchableGuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.guidebook.pages.*;
import sunsetsatellite.signalindustries.gui.guidebook.sections.FluidMachineSection;
import sunsetsatellite.signalindustries.gui.guidebook.sections.MachineSection;
import sunsetsatellite.signalindustries.gui.guidebook.sections.MultiblockSection;
import sunsetsatellite.signalindustries.recipes.container.CrystalCutterRecipes;
import sunsetsatellite.signalindustries.recipes.container.SIRecipes;
import sunsetsatellite.signalindustries.util.SearchableGuidebookSubsection;

import java.util.ArrayList;
import java.util.List;

public class SignalIndustriesSection extends SearchableGuidebookSection {

    private final List<GuidebookPage> pages = new ArrayList<>();
    private final List<Index> indices = new ArrayList<>();
    private final List<SearchableGuidebookSubsection> subsections = new ArrayList<>();

    private final SearchableGuidebookSubsection extractor = new FluidMachineSection(this, SIRecipes.EXTRACTOR.getAllRecipes(), ExtractorPage.class);
    private final SearchableGuidebookSubsection crusher = new MachineSection(this,SIRecipes.CRUSHER.getAllRecipes(), CrusherPage.class);
    private final SearchableGuidebookSubsection alloySmelter = new MachineSection(this,SIRecipes.ALLOY_SMELTER.getAllRecipes(), AlloySmelterPage.class);
    private final SearchableGuidebookSubsection plateFormer = new MachineSection(this,SIRecipes.PLATE_FORMER.getAllRecipes(), PlateFormerPage.class);
    private final SearchableGuidebookSubsection pump = new FluidMachineSection(this,SIRecipes.PUMP.getAllRecipes(), PumpPage.class);
    private final SearchableGuidebookSubsection crystalCutter = new MachineSection(this,SIRecipes.CRYSTAL_CUTTER.getAllRecipes(), CrystalCutterPage.class);
    private final SearchableGuidebookSubsection crystalChamber = new MachineSection(this,SIRecipes.CRYSTAL_CHAMBER.getAllRecipes(), CrystalChamberPage.class);
    private final SearchableGuidebookSubsection infuser = new MachineSection(this,SIRecipes.INFUSER.getAllRecipes(), InfuserPage.class);
    private final SearchableGuidebookSubsection centrifuge = new MachineSection(this,SIRecipes.CENTRIFUGE.getAllRecipes(), CentrifugePage.class);


    private final SearchableGuidebookSubsection multiblocks = new MultiblockSection(this);
    public SignalIndustriesSection() {
        super("guidebook.section.signalindustries", new ItemStack(SignalIndustries.signalumCrystal), 0xAA0000, 0xFF0000);
        reloadSection();
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.extractor"),extractor.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.crusher"),crusher.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.alloySmelter"),alloySmelter.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.plateFormer"),plateFormer.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.pump"),pump.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.crystalCutter"),crystalCutter.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.crystalChamber"),crystalChamber.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.infuser"),infuser.getPages().get(0)));
        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.centrifuge"),centrifuge.getPages().get(0)));

        indices.add(new Index(I18n.getInstance().translateKey( "guidebook.section.signalindustries.multiblocks"),multiblocks.getPages().get(0)));

        subsections.add(extractor);
        subsections.add(crusher);
        subsections.add(alloySmelter);
        subsections.add(plateFormer);
        subsections.add(pump);
        subsections.add(crystalCutter);
        subsections.add(crystalChamber);
        subsections.add(infuser);
        subsections.add(centrifuge);
        subsections.add(multiblocks);
    }

    public void reloadSection(){
        pages.clear();
        pages.add(new GuidebookPage(this) {
            @Override
            protected void renderForeground(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {

            }
        });
        for (SearchableGuidebookSubsection subsection : subsections) {
            subsection.reloadSection();
        }
    }

    @Override
    public List<GuidebookPage> getPages() {
        ArrayList<GuidebookPage> list = new ArrayList<>(pages);
        for (SearchableGuidebookSubsection subsection : subsections) {
            if(subsection.getPages() != null){
                list.addAll(subsection.getPages());
            }
        }
        return list;
    }

    @Override
    public List<Index> getIndices() {
        ArrayList<Index> list = new ArrayList<>(indices);
        for (SearchableGuidebookSubsection subsection : subsections) {
            if(subsection.getIndices() != null){
                list.addAll(subsection.getIndices());
            }
        }
        return list;
    }

    @Override
    public List<GuidebookPage> searchPages(SearchQuery query) {
        ArrayList<GuidebookPage> list = new ArrayList<>(pages);
        for (SearchableGuidebookSubsection subsection : subsections) {
            List<GuidebookPage> searchList = subsection.searchPages(query);
             if(searchList != null && !searchList.isEmpty()){
                list.addAll(searchList);
            }
        }
        return list;
    }
}
