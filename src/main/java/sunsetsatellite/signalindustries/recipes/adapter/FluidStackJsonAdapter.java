package sunsetsatellite.signalindustries.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

import java.lang.reflect.Type;

public class FluidStackJsonAdapter implements JsonDeserializer<FluidStack>, JsonSerializer<FluidStack> {
    @Override
    public FluidStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String fluidName = obj.get("id").getAsString();
        int amount = obj.get("amount").getAsInt();
        try {
            Fluid fluid = Fluid.fluidMap.get(NamespaceID.getPermanent(fluidName));
            return new FluidStack(fluid,amount);
        } catch (HardIllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonElement serialize(FluidStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id",src.fluid.id.toString());
        obj.addProperty("amount",src.amount);
        return obj;
    }
}
