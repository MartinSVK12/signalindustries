package sunsetsatellite.signalindustries.recipes.adapter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.lang.reflect.Type;

public class RecipeMachineRandomOutputJsonAdapter implements RecipeJsonAdapter<RecipeEntryMachineRandomOutput> {
    @Override
    public RecipeEntryMachineRandomOutput deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Type type = (new TypeToken<WeightedRandomBag<WeightedRandomLootObject>>() {
        }).getType();
        WeightedRandomBag<WeightedRandomLootObject> outputs = context.deserialize(obj.get("outputs").getAsJsonArray(), type);
        RecipeProperties properties = context.deserialize(obj.get("properties").getAsJsonObject(), RecipeProperties.class);
        RecipeExtendedSymbol[] symbols = obj.get("symbols").getAsJsonArray().asList().stream().map((E) -> (RecipeExtendedSymbol) context.deserialize(E, RecipeExtendedSymbol.class)).toArray(RecipeExtendedSymbol[]::new);
        return new RecipeEntryMachineRandomOutput(symbols, outputs, properties);
    }

    @Override
    public JsonElement serialize(RecipeEntryMachineRandomOutput src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.toString());
        obj.addProperty("type", Registries.RECIPE_TYPES.getKey(src.getClass()));
        JsonArray symbolsArray = new JsonArray();
        for (RecipeExtendedSymbol symbol : src.getInput()) {
            symbolsArray.add(context.serialize(symbol));
        }
        obj.add("symbols", symbolsArray);
        Type type = (new TypeToken<WeightedRandomBag<WeightedRandomLootObject>>() {
        }).getType();
        obj.add("outputs", context.serialize(src.getOutput(), type));
        obj.add("properties", context.serialize(src.getData(), RecipeProperties.class));

        return obj;
    }
}
