package sunsetsatellite.signalindustries.gui.guidebook.sections;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.signalindustries.SIMultiblocks;
import sunsetsatellite.signalindustries.gui.guidebook.pages.MultiblockMaterialsPage;
import sunsetsatellite.signalindustries.gui.guidebook.pages.MultiblockPage;
import sunsetsatellite.signalindustries.util.SearchableGuidebookSubsection;

import java.util.ArrayList;
import java.util.List;

public class MultiblockSection extends SearchableGuidebookSubsection {

    private final List<GuidebookPage> pages = new ArrayList<>();
    private final Pair<String,List<GuidebookPage>> filteredPages = null;

    public MultiblockSection(GuidebookSection parent) {
        super(parent);
        reloadSection();
    }

    @Override
    public void reloadSection() {
        pages.clear();
        //TODO: auto-add new multiblocks here
        pages.add(new MultiblockPage(parent, SIMultiblocks.dimAnchorMultiblock));
        pages.add(new MultiblockMaterialsPage(parent, SIMultiblocks.dimAnchorMultiblock));
        pages.add(new MultiblockPage(parent, SIMultiblocks.signalumReactor));
        pages.add(new MultiblockMaterialsPage(parent, SIMultiblocks.signalumReactor));
        pages.add(new MultiblockPage(parent, SIMultiblocks.wrathTree));
        pages.add(new MultiblockMaterialsPage(parent, SIMultiblocks.wrathTree));
        pages.add(new MultiblockPage(parent, SIMultiblocks.extractionManifold));
        pages.add(new MultiblockMaterialsPage(parent, SIMultiblocks.extractionManifold));
        pages.add(new MultiblockPage(parent, SIMultiblocks.inductionSmelterBasic));
        pages.add(new MultiblockMaterialsPage(parent, SIMultiblocks.inductionSmelterBasic));
    }

    @Override
    public List<GuidebookPage> searchPages(SearchQuery query) {
        List<GuidebookPage> filtered = new ArrayList<>();
        for (GuidebookPage page : pages) {
            if(page instanceof MultiblockPage){
                MultiblockPage multiblockPage = (MultiblockPage) page;
                if(multiblockPage.multiblock.getTranslatedName().contains(query.query.getRight())){
                    filtered.add(page);
                }
            } else if (page instanceof MultiblockMaterialsPage) {
                MultiblockMaterialsPage multiblockPage = (MultiblockMaterialsPage) page;
                if(multiblockPage.multiblock.getTranslatedName().contains(query.query.getRight())){
                    filtered.add(page);
                }
            }

        }
        return filtered;
    }

    @Override
    public List<GuidebookPage> getPages() {
        return pages;
    }

    @Override
    public List<GuidebookSection.Index> getIndices() {
        return null;
    }
}
