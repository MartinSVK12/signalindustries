package sunsetsatellite.signalindustries.recipes.adapter;

import com.google.gson.*;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.items.tools.blocks.ItemBlockSIFluidTank;

import java.lang.reflect.Type;

public class RecipeOutputStackJsonAdapter implements JsonDeserializer<RecipeOutputStack>, JsonSerializer<RecipeOutputStack> {

    @Override
    public RecipeOutputStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        float chance = obj.get("chance").getAsFloat();
        boolean randomAmount = obj.get("randomAmount").getAsBoolean();
        RecipeOutputStack outputStack = null;

        if(obj.has("stack")){
            ItemStack stack = context.deserialize(obj.getAsJsonObject("stack"), ItemStack.class);
            outputStack = new RecipeOutputStack(stack, chance);
        } else if (obj.has("fluid")) {
            FluidStack fluid = context.deserialize(obj.getAsJsonObject("fluid"), FluidStack.class);
            outputStack = new RecipeOutputStack(fluid, chance);
        }
        if(randomAmount && outputStack != null){
            int amountMin = obj.get("amountMin").getAsInt();
            int amountMax = obj.get("amountMax").getAsInt();
            outputStack.randomYield(amountMin,amountMax);
        }
        return outputStack;
    }

    @Override
    public JsonElement serialize(RecipeOutputStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("chance",src.chance);
        obj.addProperty("randomAmount",src.randomAmount);
        if(src.randomAmount){
            obj.addProperty("amountMin",src.amountMin);
            obj.addProperty("amountMax",src.amountMax);
        }
        if(src.isItem()){
            obj.add("stack",context.serialize(src.stack, ItemStack.class));
        } else if (src.isFluid()) {
            obj.add("fluid",context.serialize(src.fluid, FluidStack.class));
        }
        return obj;
    }
}
