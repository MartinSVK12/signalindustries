package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.world.generate.feature.WorldFeatureDungeon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIItems;

@Mixin(
        value = WorldFeatureDungeon.class,
        remap = false
)
public class WorldGenDungeonMixin {

    @Shadow public WeightedRandomBag<WeightedRandomLootObject> chestLoot;

    @Inject(method = "<init>",at = @At(value = "INVOKE",target = "Lnet/minecraft/core/WeightedRandomBag;addEntry(Ljava/lang/Object;D)V",ordinal = 0,shift = At.Shift.AFTER))
    private void init(int blockIdWalls, int blockIdFloor, String mobOverride, CallbackInfo ci){
        this.chestLoot.addEntry(new WeightedRandomLootObject(SIItems.romChipBoost.getDefaultStack()), 30);
        this.chestLoot.addEntry(new WeightedRandomLootObject(SIItems.romChipProjectile.getDefaultStack()), 30);
        this.chestLoot.addEntry(new WeightedRandomLootObject(SIItems.romChipShield.getDefaultStack()), 30);
        this.chestLoot.addEntry(new WeightedRandomLootObject(SIItems.romChipScan.getDefaultStack()), 30);
    }

}
