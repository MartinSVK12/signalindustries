package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;

public class TileEntityUVLamp extends TileEntity {

    @Override
    public void tick() {
        super.tick();
        if(SignalIndustries.uvLamps.stream().noneMatch((B) -> B.pos.equals(new Vec3i(x, y, z)))){
            if(worldObj.getBlock(x,y,z) == SignalIndustries.uvLamp){
                SignalIndustries.uvLamps.add(new BlockInstance(SignalIndustries.uvLamp,new Vec3i(x,y,z),null));
            }
        };
    }

    @Override
    public void invalidate() {
        super.invalidate();
        SignalIndustries.uvLamps.removeIf((B)->B.pos.equals(new Vec3i(x,y,z)));
    }
}
