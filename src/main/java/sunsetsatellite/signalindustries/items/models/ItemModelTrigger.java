package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.items.applications.ItemTrigger;

public class ItemModelTrigger extends ItemModelStandard {

    public IconCoordinate triggerInactive = TextureRegistry.getTexture("signalindustries:item/trigger");
    public IconCoordinate triggerActive = TextureRegistry.getTexture("signalindustries:item/trigger_active");

    public ItemModelTrigger(Item item) {
        super(item);
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, @NonNull ItemStack itemStack) {
        if (entity instanceof Player player) {
			ItemTrigger trigger = (ItemTrigger) itemStack.getItem();
            if (player.inventory.armorItemInSlot(HumanArmorShape.CHEST) != null && player.inventory.armorItemInSlot(HumanArmorShape.CHEST).getItem() instanceof ItemSignalumPowerHarness) {
                ItemStack harness = player.inventory.armorItemInSlot(HumanArmorShape.CHEST);
                boolean active = harness.getData().getBoolean("active_" + trigger.getAbilityName(itemStack));
                return active ? triggerActive : triggerInactive;
            }
        }
        return triggerInactive;
    }
}
