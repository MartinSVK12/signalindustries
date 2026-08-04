package sunsetsatellite.signalindustries.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.interfaces.mixins.IMutableDimensionListAccess;

import java.util.Map;

@Mixin(value = Dimension.class, remap = false)
public class DimensionMixin implements IMutableDimensionListAccess {

    @Shadow
    @Final
    private static Int2ObjectMap<Dimension> dimensionList;


    @Override
    public Int2ObjectMap<Dimension> getMutableDimensionList() {
        return dimensionList;
    }

	@Inject(method = "init", at = @At("TAIL"))
	private static void init(CallbackInfo ci){
		new SIDimensions().init();
	}
}
