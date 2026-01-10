package sunsetsatellite.signalindustries.items.models;


import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemModelSaber extends ItemModelStandard {

    public IconCoordinate saberDisabled = TextureRegistry.getTexture("signalindustries:item/signalum_saber_unpowered");
    public IconCoordinate saberEnabled = TextureRegistry.getTexture("signalindustries:item/signalum_saber");

    public ItemModelSaber(Item item, String namespace) {
        super(item, namespace);
        setFull3D();
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
        if (itemStack.getData().getBoolean("active")) {
            return saberEnabled;
        }
        return saberDisabled;
    }
}
