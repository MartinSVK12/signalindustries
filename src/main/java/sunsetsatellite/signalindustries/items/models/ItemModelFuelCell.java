package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemModelFuelCell extends ItemModelStandard {

    public IconCoordinate cellEmpty = TextureRegistry.getTexture("signalindustries:item/fuelcellempty");
    public IconCoordinate cellFull = TextureRegistry.getTexture("signalindustries:item/fuelcellfilled");
    public IconCoordinate cellDepleted = TextureRegistry.getTexture("signalindustries:item/fuelcelldepleted");

    public ItemModelFuelCell(Item item, String namespace) {
        super(item, namespace);
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
        int fuel = itemStack.getData().getInteger("fuel");
        int depleted = itemStack.getData().getInteger("depleted");
        if(fuel <= 0 && depleted <= 0){
            return cellEmpty;
        } else if (fuel <= 0) {
            return cellDepleted;
        } else {
            return cellFull;
        }
    }
}
