package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemModelMeteorTracker extends ItemModelStandard {

    public static final IconCoordinate calibrated = TextureRegistry.getTexture("signalindustries:item/meteor_tracker");

    public ItemModelMeteorTracker(Item item, String namespace) {
        super(item, namespace);
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
        if(itemStack.getMetadata() == 1){
            return calibrated;
        }
        return super.getIcon(entity, itemStack);
    }
}
