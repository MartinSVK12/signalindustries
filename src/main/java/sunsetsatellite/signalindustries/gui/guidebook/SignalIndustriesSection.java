package sunsetsatellite.signalindustries.gui.guidebook;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.SearchableGuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.guidebook.pages.CrusherPage;
import sunsetsatellite.signalindustries.gui.guidebook.pages.ExtractorPage;
import sunsetsatellite.signalindustries.gui.guidebook.pages.PumpPage;
import sunsetsatellite.signalindustries.gui.guidebook.sections.FluidMachineSection;
import sunsetsatellite.signalindustries.gui.guidebook.sections.MachineSection;
import sunsetsatellite.signalindustries.gui.guidebook.sections.MultiblockSection;
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
    private final SearchableGuidebookSubsection pump = new FluidMachineSection(this,SIRecipes.PUMP.getAllRecipes(), PumpPage.class);
    private final SearchableGuidebookSubsection multiblocks = new MultiblockSection(this);
    public SignalIndustriesSection() {
        super("guidebook.section.signalindustries", new ItemStack(SignalIndustries.signalumCrystal), 0xAA0000, 0xFF0000);
        reloadSection();
        indices.add(new Index("guidebook.section.signalindustries.extractor",extractor.getPages().get(0)));
        indices.add(new Index("guidebook.section.signalindustries.crusher",crusher.getPages().get(0)));
        indices.add(new Index("guidebook.section.signalindustries.pump",pump.getPages().get(0)));
        indices.add(new Index("guidebook.section.signalindustries.multiblocks",multiblocks.getPages().get(0)));
        subsections.add(extractor);
        subsections.add(crusher);
        subsections.add(pump);
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
