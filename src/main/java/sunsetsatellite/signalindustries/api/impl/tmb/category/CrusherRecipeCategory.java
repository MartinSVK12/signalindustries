package sunsetsatellite.signalindustries.api.impl.tmb.category;

import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.fluids.api.impl.tmb.ExtendedTypedIngredient;
import sunsetsatellite.catalyst.fluids.api.impl.tmb.TMBFluidPlugin;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.tmb.translator.MachineRecipeTranslator;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import turing.tmb.RecipeLayoutBuilder;
import turing.tmb.TypedIngredient;
import turing.tmb.api.ItemStackIngredientRenderer;
import turing.tmb.api.VanillaTypes;
import turing.tmb.api.drawable.IDrawable;
import turing.tmb.api.drawable.IDrawableAnimated;
import turing.tmb.api.drawable.IIngredientList;
import turing.tmb.api.recipe.ILookupContext;
import turing.tmb.api.recipe.IRecipeCategory;
import turing.tmb.api.recipe.IRecipeLayout;
import turing.tmb.api.recipe.RecipeIngredientRole;
import turing.tmb.api.runtime.ITMBRuntime;
import turing.tmb.client.DrawableAnimated;
import turing.tmb.client.DrawableBlank;
import turing.tmb.client.DrawableIngredient;
import turing.tmb.client.DrawableTexture;
import turing.tmb.util.IngredientList;

import java.util.List;

public class CrusherRecipeCategory implements IRecipeCategory<MachineRecipeTranslator> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable arrowBack;
    private final int x = 44;

    public CrusherRecipeCategory() {
        this.background = new DrawableBlank(120, 60);
        this.icon = new DrawableIngredient<>(SIBlocks.prototypeCrusher.getDefaultStack(), ItemStackIngredientRenderer.INSTANCE);
        this.arrow = new DrawableAnimated(new DrawableTexture("/assets/tmb/textures/gui/gui_vanilla.png", 82, 128, 24, 16, 0, 0, 0, 0, 24, 16), 1, IDrawableAnimated.StartDirection.LEFT, false);
        this.arrowBack = new DrawableTexture("/assets/tmb/textures/gui/gui_vanilla.png", 24, 133, 24, 16, 0, 0, 0, 0, 24, 16);
    }

    @Override
    public String getName() {
        return "Crusher";
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
    public void drawRecipe(ITMBRuntime runtime, MachineRecipeTranslator recipe, IRecipeLayout layout, List<IIngredientList> ingredients, ILookupContext context) {

        RecipeProperties data = recipe.getOriginal().getData();
        getIngredients(recipe, layout, context, ingredients);

        if (data.thisTierOnly) {
            runtime.getGuiHelper().getMinecraft().font.drawStringWithShadow("Only at: "+data.tier.getTextColor() + data.tier.getRank() + TextFormatting.WHITE,24,(background.getHeight() - 10),0xFFF0F0F0);
        } else {
            runtime.getGuiHelper().getMinecraft().font.drawStringWithShadow("Minimum tier: "+data.tier.getTextColor()+data.tier.getRank() + TextFormatting.WHITE,24,(background.getHeight() - 10),0xFFF0F0F0);
        }

        arrowBack.draw(runtime.getGuiHelper(), x + 26, (background.getHeight() / 2) - 5);
        arrow.draw(runtime.getGuiHelper(), x + 26, (background.getHeight() / 2) - 5);

        runtime.getGuiHelper().getMinecraft().font.drawCenteredString(data.ticks+"t",x + 39, (background.getHeight() / 2) - 14,0xFFFFFFFF);
    }

    @Override
    public void getIngredients(MachineRecipeTranslator recipe, IRecipeLayout layout, ILookupContext context, List<IIngredientList> ingredients) {
        RecipeProperties data = recipe.getOriginal().getData();

        ingredients.add(0, IngredientList.fromRecipeSymbol(recipe.getOriginal().getInput()[0].asNormalSymbol()));
        ingredients.add(1, new IngredientList(TypedIngredient.itemStackIngredient(recipe.getOriginal().getOutput())));
        ingredients.add(2, new IngredientList(ExtendedTypedIngredient.fluidStackIngredient(new FluidStack(SIFluids.ENERGY, (int) (data.cost * (data.ticks/200.0f))))));
    }

    @Override
    public IRecipeLayout getRecipeLayout() {
        return new RecipeLayoutBuilder()
                .addInputSlot(0, VanillaTypes.ITEM_STACK).setPosition(x, (background.getHeight() / 2) - 6).build()
                .addOutputSlot(1, VanillaTypes.ITEM_STACK).setPosition(x + 56, (background.getHeight() / 2) - 6).build()
                .addSlot(2, TMBFluidPlugin.FLUID_STACK, RecipeIngredientRole.RENDER_ONLY).setPosition(10, (background.getHeight() / 2) - 6).build()
                .build();
    }
}
