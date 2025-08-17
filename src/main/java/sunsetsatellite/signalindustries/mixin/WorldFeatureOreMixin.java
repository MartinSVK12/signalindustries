package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureOre;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mixin(value = WorldFeatureOre.class, remap = false)
public class WorldFeatureOreMixin {

    @Shadow
    @Final
    private WorldFeatureOre.OreMap variantMap;

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlock(IIII)Z", ordinal = 0))
    public void place(World world, Random random, int xStart, int yStart, int zStart, CallbackInfoReturnable<Boolean> cir) {
        //Block<?> block = Blocks.getBlock(this.variantMap.get(Blocks.STONE.id()));
        //if(block != null && block.hasTag(SignalIndustries.ORE_BLOCK)){
        //    SignalIndustries.ORE_BLOCK_COUNT.compute(block,(k,v) -> v == null ? 1 : v + 1);
        //}
        //SignalIndustries.LOGGER.info("Ore Count: {}", SignalIndustries.ORE_BLOCK_COUNT.values().stream().mapToInt(Integer::intValue).sum());
    }
}
