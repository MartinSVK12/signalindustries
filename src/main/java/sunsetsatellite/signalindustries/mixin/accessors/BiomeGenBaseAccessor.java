package sunsetsatellite.signalindustries.mixin.accessors;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.WorldType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
        value = BiomeGenBase.class,
        remap = false
)
public interface BiomeGenBaseAccessor {
    @Accessor("biomeList")
    static void setBiomeList(BiomeGenBase[] list){
        throw new AssertionError();
    }
}
