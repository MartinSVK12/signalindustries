package sunsetsatellite.signalindustries.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.lang.reflect.Type;

public class RecipeMachineMultiOutputJsonAdapter implements RecipeJsonAdapter<RecipeEntryMachineMultiOutput> {
    @Override
    public RecipeEntryMachineMultiOutput deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        RecipeOutputStack[] outputs = obj.get("outputs").getAsJsonArray().asList().stream().map((E) -> (RecipeOutputStack) context.deserialize(E, RecipeOutputStack.class)).toArray(RecipeOutputStack[]::new);
        RecipeProperties properties = context.deserialize(obj.get("properties").getAsJsonObject(), RecipeProperties.class);
        RecipeExtendedSymbol[] symbols = obj.get("symbols").getAsJsonArray().asList().stream().map((E) -> (RecipeExtendedSymbol) context.deserialize(E, RecipeExtendedSymbol.class)).toArray(RecipeExtendedSymbol[]::new);
        return new RecipeEntryMachineMultiOutput(symbols,outputs,properties);
    }

    @Override
    public JsonElement serialize(RecipeEntryMachineMultiOutput src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.toString());
        obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
        JsonArray symbolsArray = new JsonArray();
        for (RecipeExtendedSymbol symbol : src.getInput()) {
            symbolsArray.add(context.serialize(symbol));
        }
        obj.add("symbols", symbolsArray);
        obj.add("outputs", context.serialize(src.getOutput(), RecipeOutputStack[].class));
        obj.add("properties", context.serialize(src.getData(), RecipeProperties.class));

        return obj;
    }
}
