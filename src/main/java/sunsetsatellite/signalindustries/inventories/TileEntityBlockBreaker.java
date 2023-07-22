package sunsetsatellite.signalindustries.inventories;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;

public class TileEntityBlockBreaker extends TileEntity {

    public Tier tier = Tier.PROTOTYPE;
    public boolean active = false;
    public boolean blockBroken = false;
    public TileEntityBlockBreaker(){

    }

    public boolean isActive(){
        return active;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj != null && getBlockType() != null){
            tier = ((BlockContainerTiered)getBlockType()).tier;
        }
        if(active && !blockBroken){
            Direction dir = Direction.getDirectionFromSide(getBlockMetadata());
            Vec3i vec = new Vec3i(xCoord,yCoord,zCoord).add(dir.getVec());
            Vec3i vec2 = new Vec3i(xCoord,yCoord,zCoord).add(dir.getOpposite().getVec());
            int blockId = worldObj.getBlockId(vec.x, vec.y, vec.z);
            if(blockId > 0){
                Block block = Block.blocksList[blockId];
                if(block.getHardness() == -1 || block instanceof BlockFluid){
                    blockBroken = true;
                    return;
                }
                block.dropBlockAsItem(worldObj, vec2.x, vec2.y, vec2.z, worldObj.getBlockMetadata(vec.x, vec.y, vec.z));
                worldObj.setBlockWithNotify(vec.x, vec.y, vec.z, 0);
            }
            blockBroken = true;
        }
    }

}
