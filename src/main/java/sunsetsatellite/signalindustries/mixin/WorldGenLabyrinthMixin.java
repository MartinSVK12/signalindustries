package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIItems;

import java.util.Random;

@Mixin(
        value = WorldFeatureLabyrinth.class,
        remap = false
)
public class WorldGenLabyrinthMixin {
    @Inject(
            method = "pickCheckLootItem",
            at = @At("TAIL"),
            cancellable = true
    )
    private void pickCheckLootItem(Random random, CallbackInfoReturnable<ItemStack> cir) {
        if (random.nextInt(64) == 0){
            int j = random.nextInt(2);
            switch (j){
                case 0:
                    cir.setReturnValue(new ItemStack(SIItems.romChipProjectile,1));
                    break;
                case 1:
                    cir.setReturnValue(new ItemStack(SIItems.romChipBoost,1));
                    break;
            }
        } else {
            cir.setReturnValue(null);
        }
    }

}
