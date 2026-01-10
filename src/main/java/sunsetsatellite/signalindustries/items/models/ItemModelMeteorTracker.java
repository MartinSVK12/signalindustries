package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemModelMeteorTracker extends ItemModelStandard {

    public static final IconCoordinate calibrated = TextureRegistry.getTexture("signalindustries:item/meteor_tracker");
    public static final IconCoordinate calibrated_reinforced = TextureRegistry.getTexture("signalindustries:item/reinforced_meteor_tracker");

    public static final IconCoordinate uncalibrated = TextureRegistry.getTexture("signalindustries:item/meteor_tracker_uncalibrated");
    public static final IconCoordinate uncalibrated_reinforced = TextureRegistry.getTexture("signalindustries:item/reinforced_meteor_tracker_uncalibrated");

    public final Tier tier;

    public ItemModelMeteorTracker(Item item, String namespace, Tier tier) {
        super(item, namespace);
        this.tier = tier;
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
        if (itemStack.getMetadata() == 1) {
            return tier == Tier.REINFORCED ? calibrated_reinforced : calibrated;
        }
        return tier == Tier.REINFORCED ? uncalibrated_reinforced : uncalibrated;
    }
}
