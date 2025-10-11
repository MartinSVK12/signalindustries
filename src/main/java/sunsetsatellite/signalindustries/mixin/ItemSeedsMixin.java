package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemSeeds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sunsetsatellite.signalindustries.interfaces.mixins.ICrop;

@Mixin(value = ItemSeeds.class,remap = false)
public class ItemSeedsMixin implements ICrop {

    @Shadow
    @Final
    private Block<?> cropsBlock;

    @Override
    public Block<?> getCropBlock() {
        return cropsBlock;
    }
}
