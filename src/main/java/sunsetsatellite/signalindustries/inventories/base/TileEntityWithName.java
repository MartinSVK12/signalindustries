package sunsetsatellite.signalindustries.inventories.base;

import com.b100.utils.ReflectUtils;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.signalindustries.interfaces.INamedTileEntity;

import java.lang.reflect.Field;

public class TileEntityWithName extends TileEntity implements INamedTileEntity {

    public String getName(){
        Field field = ReflectUtils.getField(TileEntity.class,"classToNameMap");
        return ReflectUtils.getValue(field,null,String.class);
    }

}
