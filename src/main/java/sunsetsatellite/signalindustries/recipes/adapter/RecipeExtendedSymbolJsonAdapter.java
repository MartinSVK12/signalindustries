package sunsetsatellite.signalindustries.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;

import java.lang.reflect.Type;

public class RecipeExtendedSymbolJsonAdapter implements JsonDeserializer<RecipeExtendedSymbol>, JsonSerializer<RecipeExtendedSymbol> {
    @Override
    public RecipeExtendedSymbol deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        char symbol = 0;
        if (obj.has("symbol")) {
            symbol = obj.get("symbol").getAsString().charAt(0);
        }

        ItemStack stack = null;
        FluidStack fluidStack = null;
        String group = null;
        int amount = 1;
        if (obj.has("stack")) {
            stack = context.deserialize(obj.getAsJsonObject("stack"), ItemStack.class);
        }

        if (obj.has("fluid")) {
            fluidStack = context.deserialize(obj.getAsJsonObject("fluid"), FluidStack.class);
        }


        if (obj.has("group")) {
            group = obj.get("group").getAsString();
        }

        if (obj.has("amount")) {
            amount = obj.get("amount").getAsInt();
        }

        RecipeExtendedSymbol exSymbol = null;

        if(stack != null){
            exSymbol = new RecipeExtendedSymbol(symbol, stack, group);
            if(amount != 1){
                exSymbol.setAmount(amount);
            }
        } else if (fluidStack != null) {
            exSymbol = new RecipeExtendedSymbol(symbol, fluidStack);
            if(amount != 1){
                exSymbol.setAmount(amount);
            }
        }

        return exSymbol;
    }

    @Override
    public JsonElement serialize(RecipeExtendedSymbol src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        if (src.getSymbol() != 0) {
            obj.addProperty("symbol", src.getSymbol());
        }

        if (src.getItemGroup() != null) {
            obj.addProperty("group", src.getItemGroup());
        }

        if (src.getAmount() != 1) {
            obj.addProperty("amount", src.getAmount());
        }

        if (src.getStack() != null) {
            obj.add("stack", context.serialize(src.getStack()));
        }

        if (src.getFluidStack() != null) {
            obj.add("fluid", context.serialize(src.getFluidStack(), FluidStack.class));
        }

        return obj;
    }
}
