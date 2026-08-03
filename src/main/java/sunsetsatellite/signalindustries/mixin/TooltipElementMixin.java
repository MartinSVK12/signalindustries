package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.guidebook.SlotGuidebook;
import net.minecraft.client.option.GameSettings;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;

@Mixin(value = TooltipElement.class, remap = false)
public class TooltipElementMixin {

    @Shadow
    Minecraft mc;

    @Inject(method = "getTooltipText(Lnet/minecraft/core/item/ItemStack;ZLnet/minecraft/core/player/inventory/slot/Slot;)Ljava/lang/String;", at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;", ordinal = 0, shift = At.Shift.AFTER))
    public void getTooltipText(ItemStack itemStack, boolean showDescription, Slot slot, CallbackInfoReturnable<String> cir, @Local StringBuilder text) {
        if (slot instanceof SlotGuidebook) {
            RecipeEntryBase<?, ?, ?> r = ((SlotGuidebook) slot).recipe;
            if (r instanceof RecipeEntryMachineRandomOutput) {
                RecipeEntryMachineRandomOutput recipe = (RecipeEntryMachineRandomOutput) r;
                RecipeSymbol input = recipe.getInput()[0].asNormalSymbol();
                if (!input.matches(slot.getItemStack())) {
                    WeightedRandomBag<WeightedRandomLootObject> bag = recipe.getOutput();
                    double percent = bag.getAsPercentage(slot.getItemStack());
                    WeightedRandomLootObject lootObject = bag.getEntries().get(slot.index);
                    if (lootObject.isRandomYield()) {
                        text.append("\n").append(TextFormatting.LIGHT_GRAY).append(String.format("%d-%d", lootObject.getMinYield(), lootObject.getMaxYield()));
                    }
                    text.append('\n').append(TextFormatting.LIGHT_GRAY).append(String.format("%.2f", percent)).append("%");
                }
            }
        }
    }

    @Inject(method = "getTooltipText(Lnet/minecraft/core/item/ItemStack;ZLnet/minecraft/core/player/inventory/slot/Slot;)Ljava/lang/String;", at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;append(I)Ljava/lang/StringBuilder;", ordinal = 1, shift = At.Shift.AFTER))
    public void getTooltipText2(ItemStack itemStack, boolean showDescription, Slot slot, CallbackInfoReturnable<String> cir, @Local StringBuilder text) {
        boolean debug = GameSettings.SHOW_ITEM_DEBUG_INFO.value;
        if(debug && slot != null){
            text.append("\n").append(TextFormatting.LIGHT_GRAY).append("Slot ID: ").append(slot.index);
        }
    }

}
