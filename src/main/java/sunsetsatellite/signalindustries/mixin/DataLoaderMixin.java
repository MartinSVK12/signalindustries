package sunsetsatellite.signalindustries.mixin;

import com.google.gson.GsonBuilder;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.net.packet.PacketRecipeSync;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.recipes.adapter.FluidStackJsonAdapter;
import sunsetsatellite.signalindustries.recipes.adapter.RecipeExtendedSymbolJsonAdapter;
import sunsetsatellite.signalindustries.recipes.adapter.RecipeOutputStackJsonAdapter;
import sunsetsatellite.signalindustries.recipes.adapter.RecipePropertiesJsonAdapter;
import sunsetsatellite.signalindustries.util.RecipeProperties;

@Mixin(value = DataLoader.class,remap = false)
public class DataLoaderMixin {

    @Inject(method = "loadRecipesFromFile", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;", shift = At.Shift.BEFORE))
    private static void loadRecipesFromFile(String path, CallbackInfo ci, @Local GsonBuilder builder) {
        builder.registerTypeAdapter(FluidStack.class,new FluidStackJsonAdapter());
        builder.registerTypeAdapter(RecipeOutputStack.class,new RecipeOutputStackJsonAdapter());
        builder.registerTypeAdapter(RecipeProperties.class,new RecipePropertiesJsonAdapter());
        builder.registerTypeAdapter(RecipeExtendedSymbol.class,new RecipeExtendedSymbolJsonAdapter());
    }

    @Inject(method = "loadRecipesFromString", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;", shift = At.Shift.BEFORE))
    private static void loadRecipesFromString(String json, CallbackInfo ci, @Local GsonBuilder builder) {
        builder.registerTypeAdapter(FluidStack.class,new FluidStackJsonAdapter());
        builder.registerTypeAdapter(RecipeOutputStack.class,new RecipeOutputStackJsonAdapter());
        builder.registerTypeAdapter(RecipeProperties.class,new RecipePropertiesJsonAdapter());
        builder.registerTypeAdapter(RecipeExtendedSymbol.class,new RecipeExtendedSymbolJsonAdapter());
    }

    @Inject(method = "loadRecipeFromServer", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;", shift = At.Shift.BEFORE))
    private static void loadRecipeFromServer(PacketRecipeSync packet, CallbackInfo ci, @Local GsonBuilder builder) {
        builder.registerTypeAdapter(FluidStack.class,new FluidStackJsonAdapter());
        builder.registerTypeAdapter(RecipeOutputStack.class,new RecipeOutputStackJsonAdapter());
        builder.registerTypeAdapter(RecipeProperties.class,new RecipePropertiesJsonAdapter());
        builder.registerTypeAdapter(RecipeExtendedSymbol.class,new RecipeExtendedSymbolJsonAdapter());
    }

    @Inject(method = "serializeRecipes", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;", shift = At.Shift.BEFORE))
    private static void serializeRecipes(CallbackInfoReturnable<String> cir, @Local GsonBuilder builder) {
        builder.registerTypeAdapter(FluidStack.class,new FluidStackJsonAdapter());
        builder.registerTypeAdapter(RecipeOutputStack.class,new RecipeOutputStackJsonAdapter());
        builder.registerTypeAdapter(RecipeProperties.class,new RecipePropertiesJsonAdapter());
        builder.registerTypeAdapter(RecipeExtendedSymbol.class,new RecipeExtendedSymbolJsonAdapter());
    }

    @Inject(method = "serializeRecipe", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;", shift = At.Shift.BEFORE))
    private static void serializeRecipe(RecipeEntryBase<?, ?, ?> recipe, CallbackInfoReturnable<String> cir, @Local GsonBuilder builder) {
        builder.registerTypeAdapter(FluidStack.class,new FluidStackJsonAdapter());
        builder.registerTypeAdapter(RecipeOutputStack.class,new RecipeOutputStackJsonAdapter());
        builder.registerTypeAdapter(RecipeProperties.class,new RecipePropertiesJsonAdapter());
        builder.registerTypeAdapter(RecipeExtendedSymbol.class,new RecipeExtendedSymbolJsonAdapter());
    }

    @Inject(method = "exportRecipes", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;", shift = At.Shift.BEFORE))
    private static void exportRecipes(String path, CallbackInfo ci, @Local GsonBuilder builder) {
        builder.registerTypeAdapter(FluidStack.class,new FluidStackJsonAdapter());
        builder.registerTypeAdapter(RecipeOutputStack.class,new RecipeOutputStackJsonAdapter());
        builder.registerTypeAdapter(RecipeProperties.class,new RecipePropertiesJsonAdapter());
        builder.registerTypeAdapter(RecipeExtendedSymbol.class,new RecipeExtendedSymbolJsonAdapter());
    }
}
