package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;

import java.util.HashMap;
import java.util.Map;

@Mixin(
        value = {CompoundTag.class},
        remap = false
)

public abstract class NBTTagCompoundMixin  extends Tag<Map<String, Tag<?>>> implements INBTCompound {

    public NBTTagCompoundMixin(){}

    public void removeTag(String s){
        this.getValue().remove(s);
    }

}
