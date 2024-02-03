package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityFluidConduit;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockFluidConduit extends BlockContainerTiered {

    public BlockFluidConduit(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    protected TileEntity getNewBlockEntity() {
        return new TileEntityFluidConduit();
    }

    /*@Override
    public int getRenderType() {
        return 32;
    }*/

    public boolean isSolidRender() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if(entityplayer.isSneaking() && !world.isClientSide){
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
    public void setBlockBoundsForItemRender() {
        float width = 0.35f;
        float halfWidth = (1.0F - width) / 2.0F;
        setBlockBounds(halfWidth, halfWidth, halfWidth, halfWidth + width, halfWidth + width, halfWidth + width);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return super.getDescription(stack);
    }
}
