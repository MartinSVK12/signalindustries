package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.tiles.TileEntityFluidConduit;

public class BlockFluidConduit extends BlockContainerTiered {
    public BlockFluidConduit(int i, Tier tier, Material material) {
        super(i, tier, material);
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityFluidConduit();
    }

    @Override
    public int getRenderType() {
        return 32;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if(entityplayer.isSneaking() && !world.isMultiplayerAndNotHost){
            TileEntityFluidPipe tile = (TileEntityFluidPipe) world.getBlockTileEntity(i,j,k);
            if(tile.getFluidInSlot(0) != null && tile.getFluidInSlot(0).getLiquid() != null){
                entityplayer.addChatMessage("Liquid: "+tile.getFluidInSlot(0).toString());
            } else {
                entityplayer.addChatMessage("Liquid: Empty");
            }
            entityplayer.addChatMessage("Capacity: "+tile.fluidCapacity[0]);
            entityplayer.addChatMessage("Is pressurized? "+tile.isPressurized);
            return false;
        }
        return false;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return super.getDescription(stack);
    }
}
