package sunsetsatellite.signalindustries.mixin.accessors;

import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
        value = WorldType.class,
        remap = false
)
public interface WorldTypeAccessor {
    @Accessor("worldTypes")
    static void setWorldTypes(WorldType[] list){
        throw new AssertionError();
    }
}
