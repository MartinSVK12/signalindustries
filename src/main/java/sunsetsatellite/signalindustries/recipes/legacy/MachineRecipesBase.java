package sunsetsatellite.signalindustries.recipes.legacy;




import java.util.HashMap;

@Deprecated
public abstract class MachineRecipesBase<I,O> {

    protected final HashMap<I, O> recipeList = new HashMap<>();

    public abstract HashMap<I,O> getRecipeList();

    public abstract void addRecipe(I input, O output);

    public abstract O getResult(I input);

}
