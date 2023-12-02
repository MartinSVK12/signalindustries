package sunsetsatellite.signalindustries.gui.guidebook.sections;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.util.SearchableGuidebookSubsection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FluidMachineSection extends SearchableGuidebookSubsection {

    protected final List<RecipeEntryMachineFluid> recipes;
    protected final Class<? extends GuidebookPage> pageClass;
    private final List<GuidebookPage> pages = new ArrayList<>();
    private Pair<String,List<GuidebookPage>> filteredPages = null;
    private final int recipesPerPage;

    public FluidMachineSection(GuidebookSection parent, List<RecipeEntryMachineFluid> recipes, Class<? extends GuidebookPage> pageClass) {
        super(parent);
        this.recipes = recipes;
        this.pageClass = pageClass;
        try {
            this.recipesPerPage = (int) pageClass.getField("RECIPES_PER_PAGE").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        reloadSection();
    }

    @Override
    public void reloadSection() {
        pages.clear();
        int totalRecipes = recipes.size();
        int totalPages = totalRecipes / recipesPerPage;
        if(totalPages == 0) totalPages = 1;
        for (int i = 0; i < totalPages; i++) {
            int j = i*recipesPerPage;
            ArrayList<RecipeEntryMachineFluid> list = new ArrayList<>(recipes.subList(j,Math.min(j+recipesPerPage,totalRecipes)));
            try {
                pages.add(pageClass.getConstructor(GuidebookSection.class, ArrayList.class).newInstance(parent,list));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<GuidebookPage> searchPages(SearchQuery query) {
        if(filteredPages == null || filteredPages.getRight() == null || filteredPages.getRight().isEmpty() || !Objects.equals(filteredPages.getLeft(), query.rawQuery)) {
            ArrayList<RecipeEntryMachineFluid> filteredRecipes = new ArrayList<>();
            for (RecipeEntryMachineFluid recipe : recipes) {
                if(recipe.matchesQuery(query)){
                    filteredRecipes.add(recipe);
                }
            }
            ArrayList<GuidebookPage> guidebookPages = new ArrayList<>();
            int filteredRecipeSize = filteredRecipes.size();
            int filteredPageCount = filteredRecipeSize / recipesPerPage;
            if (filteredPageCount == 0) filteredPageCount = 1;
            for (int i = 0; i < filteredPageCount; i++) {
                int j = i * recipesPerPage;
                ArrayList<RecipeEntryMachineFluid> list = new ArrayList<>(filteredRecipes.subList(j, Math.min(j + recipesPerPage, filteredRecipeSize)));
                if (!list.isEmpty()) {
                    try {
                        guidebookPages.add(pageClass.getConstructor(GuidebookSection.class, ArrayList.class).newInstance(parent,list));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            this.filteredPages = Pair.of(query.rawQuery, guidebookPages);
            return guidebookPages;
        } else {
            return filteredPages.getRight();
        }
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
