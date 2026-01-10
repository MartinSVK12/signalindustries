package sunsetsatellite.signalindustries.api.impl.tmb.category;

import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.fluids.api.impl.tmb.ExtendedTypedIngredient;
import sunsetsatellite.catalyst.fluids.api.impl.tmb.TMBFluidPlugin;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.tmb.translator.FluidMachineRecipeTranslator;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import turing.tmb.RecipeLayoutBuilder;
import turing.tmb.api.ItemStackIngredientRenderer;
import turing.tmb.api.VanillaTypes;
import turing.tmb.api.drawable.IDrawable;
import turing.tmb.api.drawable.IDrawableAnimated;
import turing.tmb.api.drawable.IIngredientList;
import turing.tmb.api.recipe.ILookupContext;
import turing.tmb.api.recipe.IRecipeCategory;
import turing.tmb.api.recipe.IRecipeLayout;
import turing.tmb.api.runtime.ITMBRuntime;
import turing.tmb.client.DrawableAnimated;
import turing.tmb.client.DrawableBlank;
import turing.tmb.client.DrawableIngredient;
import turing.tmb.client.DrawableTexture;
import turing.tmb.util.IngredientList;

import java.util.List;

public class ExtractorRecipeCategory implements IRecipeCategory<FluidMachineRecipeTranslator> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable arrowBack;
    private final IDrawable flame;
    private final IDrawable flameBack;
    private final int x = 44;

    public ExtractorRecipeCategory() {
        this.background = new DrawableBlank(120, 60);
        this.icon = new DrawableIngredient<>(SIBlocks.prototypeExtractor.getDefaultStack(), ItemStackIngredientRenderer.INSTANCE);
        this.arrow = new DrawableAnimated(new DrawableTexture("/assets/tmb/textures/gui/gui_vanilla.png", 82, 128, 24, 16, 0, 0, 0, 0, 24, 16), 1, IDrawableAnimated.StartDirection.LEFT, false);
        this.arrowBack = new DrawableTexture("/assets/tmb/textures/gui/gui_vanilla.png", 24, 133, 24, 16, 0, 0, 0, 0, 24, 16);
        this.flame = new DrawableAnimated(new DrawableTexture("/assets/tmb/textures/gui/gui_vanilla.png", 82, 114, 14, 14, 0, 0, 0, 0, 14, 14), 1, IDrawableAnimated.StartDirection.TOP, true);
        this.flameBack = new DrawableTexture("/assets/tmb/textures/gui/gui_vanilla.png", 1, 134, 14, 14, 0, 0, 0, 0, 14, 14);
    }

    @Override
    public String getName() {
        return "Extractor";
    }

    @Override
    public String getNamespace() {
        return SignalIndustries.MOD_ID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void drawRecipe(ITMBRuntime runtime, FluidMachineRecipeTranslator recipe, IRecipeLayout layout, List<IIngredientList> ingredients, ILookupContext context) {

        RecipeProperties data = recipe.getOriginal().getData();
        getIngredients(recipe, layout, context, ingredients);

        if (data.thisTierOnly) {
            runtime.getGuiHelper().getMinecraft().font.drawStringWithShadow("Only at: " + data.tier.getTextColor() + data.tier.getRank() + TextFormatting.WHITE, 24, (background.getHeight() - 10), 0xFFF0F0F0);
        } else {
            runtime.getGuiHelper().getMinecraft().font.drawStringWithShadow("Minimum tier: " + data.tier.getTextColor() + data.tier.getRank() + TextFormatting.WHITE, 24, (background.getHeight() - 10), 0xFFF0F0F0);
        }

        arrowBack.draw(runtime.getGuiHelper(), x + 26, (background.getHeight() / 2) - 5);
        arrow.draw(runtime.getGuiHelper(), x + 26, (background.getHeight() / 2) - 5);
        flameBack.draw(runtime.getGuiHelper(), 10, (background.getHeight() / 2) - 5);
        flame.draw(runtime.getGuiHelper(), 10, (background.getHeight() / 2) - 5);

        runtime.getGuiHelper().getMinecraft().font.drawCenteredString(data.ticks + "t", x + 39, (background.getHeight() / 2) - 14, 0xFFFFFFFF);
    }

    @Override
    public void getIngredients(FluidMachineRecipeTranslator recipe, IRecipeLayout layout, ILookupContext context, List<IIngredientList> ingredients) {
        RecipeProperties data = recipe.getOriginal().getData();

        ingredients.add(0, IngredientList.fromRecipeSymbol(recipe.getOriginal().getInput()[0].asNormalSymbol()));
        ingredients.add(1, new IngredientList(ExtendedTypedIngredient.fluidStackIngredient(recipe.getOriginal().getOutput())));
    }

    @Override
    public IRecipeLayout getRecipeLayout() {
        return new RecipeLayoutBuilder()
                .addInputSlot(0, VanillaTypes.ITEM_STACK).setPosition(x, (background.getHeight() / 2) - 6).build()
                .addOutputSlot(1, TMBFluidPlugin.FLUID_STACK).setPosition(x + 56, (background.getHeight() / 2) - 6).build()
                .build();
    }
}
