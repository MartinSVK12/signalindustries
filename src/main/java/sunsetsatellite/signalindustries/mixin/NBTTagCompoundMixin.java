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

    @Override
    public boolean equals(CompoundTag tag) {
        HashMap<?,?> otherTagMap;
        otherTagMap = (HashMap<?, ?>) tag.getValue();//(HashMap<?, ?>) RetroStorage.getPrivateValue(tag.getClass(),tag,"tagMap");
        if(otherTagMap == null){
            otherTagMap = new HashMap<>();
        }
        if(getValue().isEmpty() && otherTagMap.isEmpty()){
            return true;
        }
        if(getValue().isEmpty()){
            return false;
        } else if (otherTagMap.isEmpty()){
            return false;
        }
        int s = 0;
        for (Map.Entry<?, ?> entry : ((HashMap<?,?>)getValue()).entrySet()) {
            Object K = entry.getKey();
            Object V = entry.getValue();
            if(tag.containsKey((String) K)){
                if(V.equals(otherTagMap.get(K))){
                    s++;
                }
            }
        }
        return s == getValue().size();
    }

}
