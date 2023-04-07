package sunsetsatellite.signalindustries.mixin;

import b100.utils.ReflectUtils;
import net.minecraft.src.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;

import java.lang.reflect.Field;
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

    @Override
    public boolean equals(NBTTagCompound tag) {
        HashMap<?,?> otherTagMap = null;
        Field field = ReflectUtils.getField(tag.getClass(),"tagMap");
        otherTagMap = (HashMap<?, ?>) ReflectUtils.getValue(field,tag);//(HashMap<?, ?>) RetroStorage.getPrivateValue(tag.getClass(),tag,"tagMap");
        if(otherTagMap == null){
            otherTagMap = new HashMap<>();
        }
        if(tagMap.size() == 0 && otherTagMap.size() == 0){
            return true;
        }
        if(tagMap.size() == 0){
            return false;
        } else if (otherTagMap.size() == 0){
            return false;
        }
        int s = 0;
        for (Map.Entry<?, ?> entry : ((HashMap<?,?>)tagMap).entrySet()) {
            Object K = entry.getKey();
            Object V = entry.getValue();
            if(tag.hasKey((String) K)){
                if(V.equals(otherTagMap.get(K))){
                    s++;
                }
            }
        }
        return s == tagMap.size();
    }

}
