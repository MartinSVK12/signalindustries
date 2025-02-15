package sunsetsatellite.signalindustries.util;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;

import java.util.List;

public abstract class SearchableGuidebookSubsection {

    public GuidebookSection parent;

    public SearchableGuidebookSubsection(GuidebookSection parent){
        this.parent = parent;
    }

    public abstract void reloadSection();

    public abstract List<GuidebookPage> searchPages(SearchQuery query);

    public abstract List<GuidebookPage> getPages();

    public abstract List<GuidebookSection.Index> getIndices();
}
