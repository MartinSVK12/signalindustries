package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sunsetsatellite.signalindustries.interfaces.mixins.IMutableDimensionListAccess;

import java.util.Map;

@Mixin(value = Dimension.class,remap = false)
public class DimensionMixin implements IMutableDimensionListAccess {

    @Shadow
    @Final
    private static Map<Integer, Dimension> dimensionList;


    @Override
    public Map<Integer, Dimension> getMutableDimensionList() {
        return dimensionList;
    }
}
