package sunsetsatellite.signalindustries.gui.guidebook.sections;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.guidebook.pages.MultiblockPage;
import sunsetsatellite.signalindustries.util.SearchableGuidebookSubsection;

import java.util.ArrayList;
import java.util.List;

public class MultiblockSection extends SearchableGuidebookSubsection {

    private final List<GuidebookPage> pages = new ArrayList<>();
    private Pair<String,List<GuidebookPage>> filteredPages = null;

    public MultiblockSection(GuidebookSection parent) {
        super(parent);
        reloadSection();
    }

    @Override
    public void reloadSection() {
        pages.clear();
        pages.add(new MultiblockPage(parent, SignalIndustries.dimAnchorMultiblock));
        pages.add(new MultiblockPage(parent, SignalIndustries.signalumReactor));
    }

    @Override
    public List<GuidebookPage> searchPages(SearchQuery query) {
        List<GuidebookPage> filtered = new ArrayList<>();
        for (GuidebookPage page : pages) {
            MultiblockPage multiblockPage = (MultiblockPage) page;
            if(multiblockPage.multiblock.getTranslatedName().contains(query.query.getRight())){
                filtered.add(page);
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
