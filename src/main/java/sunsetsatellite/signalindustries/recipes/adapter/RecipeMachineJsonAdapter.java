package sunsetsatellite.signalindustries.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.lang.reflect.Type;
public class RecipeMachineJsonAdapter implements RecipeJsonAdapter<RecipeEntryMachine> {
    @Override
    public RecipeEntryMachine deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        ItemStack result = context.deserialize(obj.get("result").getAsJsonObject(), ItemStack.class);
        RecipeProperties properties = context.deserialize(obj.get("properties").getAsJsonObject(), RecipeProperties.class);
        RecipeExtendedSymbol[] symbols = obj.get("symbols").getAsJsonArray().asList().stream().map((E) -> (RecipeExtendedSymbol) context.deserialize(E, RecipeExtendedSymbol.class)).toArray(RecipeExtendedSymbol[]::new);
        return new RecipeEntryMachine(symbols,result,properties);
    }

    @Override
    public JsonElement serialize(RecipeEntryMachine src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.toString());
        obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
        JsonArray symbolsArray = new JsonArray();
        for (RecipeExtendedSymbol symbol : src.getInput()) {
            symbolsArray.add(context.serialize(symbol));
        }
        obj.add("symbols", symbolsArray);
        obj.add("result", context.serialize(src.getOutput(), ItemStack.class));
        obj.add("properties", context.serialize(src.getData(), RecipeProperties.class));

        return obj;
    }
}
