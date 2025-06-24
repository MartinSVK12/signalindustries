package sunsetsatellite.signalindustries.tiles;

import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;

public class TileEntityUVLamp extends TileEntity implements IActiveForm {

    @Override
    public void tick() {
        super.tick();
        if(SignalIndustries.uvLamps.stream().noneMatch((B) -> B.pos.equals(new Vec3i(x, y, z)))){
            if(worldObj != null && worldObj.getBlock(x,y,z) == SIBlocks.uvLamp){
                SignalIndustries.uvLamps.add(new BlockInstance(SIBlocks.uvLamp,new Vec3i(x,y,z),null));
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        SignalIndustries.uvLamps.removeIf((B)->B.pos.equals(new Vec3i(x,y,z)));
    }

    @Override
    public boolean isBurning() {
        return worldObj != null && worldObj.getBlockMetadata(x,y,z) == 1;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }
}
