package sunsetsatellite.signalindustries.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.world.Dimension;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipePropertiesJsonAdapter implements JsonDeserializer<RecipeProperties>, JsonSerializer<RecipeProperties> {
    @Override
    public RecipeProperties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Tier tier = Tier.valueOf(obj.get("tier").getAsString());
        int cost = obj.get("cost").getAsInt();
        int ticks = obj.get("ticks").getAsInt();
        boolean thisTierOnly = obj.get("thisTierOnly").getAsBoolean();
        Integer id = null;
        Float chance = null;
        Boolean consumeContainers = null;
        if (obj.has("id")) {
            id = obj.get("id").getAsInt();
        }
        if (obj.has("chance")) {
            chance = obj.get("chance").getAsFloat();
        }
        if (obj.has("consumeContainers")) {
            consumeContainers = obj.get("consumeContainers").getAsBoolean();
        }
        List<Dimension> allowedDimensions = new ArrayList<>();

        if (obj.has("allowedDimensions")) {
            obj.getAsJsonArray("allowedDimensions").forEach(dim -> {
                String dimName = dim.getAsString();
                Dimension.getDimensionList().entrySet().stream().filter(E -> Objects.equals(E.getValue().languageKey, dimName)).findFirst().ifPresent(E -> allowedDimensions.add(E.getValue()));
            });
        }

        RecipeProperties properties = new RecipeProperties(ticks, cost, tier, thisTierOnly);
        if (id != null) properties.id = id;
        if (chance != null) properties.chance = chance;
        if (consumeContainers != null) properties.consumeContainers = consumeContainers;
        if (!allowedDimensions.isEmpty()) properties.allowedDimensions = allowedDimensions;
        return properties;
    }

    @Override
    public JsonElement serialize(RecipeProperties src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("tier", src.tier.name());
        obj.addProperty("cost", src.cost);
        obj.addProperty("ticks", src.ticks);
        if (src.id != 0) {
            obj.addProperty("id", src.id);
        }
        if (src.chance != 1.0f) {
            obj.addProperty("chance", src.chance);
        }
        obj.addProperty("thisTierOnly", src.thisTierOnly);
        obj.addProperty("consumeContainers", src.consumeContainers);
        if (src.allowedDimensions != null && !src.allowedDimensions.isEmpty()) {
            JsonArray array = new JsonArray();
            src.allowedDimensions.forEach(dim -> array.add(dim.languageKey));
            obj.add("allowedDimensions", array);
        }
        return obj;

    }
}
