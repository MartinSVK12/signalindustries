package sunsetsatellite.signalindustries.screens.guidebook.sections;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SIMultiblocks;
import sunsetsatellite.signalindustries.screens.guidebook.pages.MultiblockMaterialsPage;
import sunsetsatellite.signalindustries.screens.guidebook.pages.MultiblockPage;
import sunsetsatellite.signalindustries.util.SIMultiblock;
import sunsetsatellite.signalindustries.screens.guidebook.SearchableGuidebookSubsection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Field> multiblockFields = Arrays.stream(SIMultiblocks.class.getDeclaredFields()).filter((F) -> SIMultiblock.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        try {
            for (Field field : multiblockFields) {
                field.setAccessible(true);
                pages.add(new MultiblockPage(parent, (Multiblock) field.get(null)));
                pages.add(new MultiblockMaterialsPage(parent, (Multiblock) field.get(null)));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to add multiblock data to guidebook!",e);
        }
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
