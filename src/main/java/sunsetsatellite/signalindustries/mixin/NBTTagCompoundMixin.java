package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;

import java.util.HashMap;
import java.util.Map;

@Mixin(
        value = {NBTTagCompound.class},
        remap = false
)

public class NBTTagCompoundMixin implements INBTCompound {
    @Shadow
    private Map tagMap = new HashMap();

    public NBTTagCompoundMixin(){}

    public void removeTag(String s){
        this.tagMap.remove(s);
    }

}
