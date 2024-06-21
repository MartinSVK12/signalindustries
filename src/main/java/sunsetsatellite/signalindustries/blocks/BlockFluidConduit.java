package sunsetsatellite.signalindustries.blocks;


import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.blocks.base.BlockConduitBase;
import sunsetsatellite.signalindustries.inventories.TileEntityFluidConduit;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockFluidConduit extends BlockConduitBase {

    public BlockFluidConduit(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    protected TileEntity getNewBlockEntity() {
        return new TileEntityFluidConduit();
    }

    public boolean isSolidRender() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if(super.blockActivated(world, i, j, k, entityplayer)){
            return true;
        }
        if(entityplayer.isSneaking() && !world.isClientSide){
            TileEntityFluidPipe tile = (TileEntityFluidPipe) world.getBlockTileEntity(i,j,k);
            if(tile.getFluidInSlot(0) != null && tile.getFluidInSlot(0).getLiquid() != null){
                Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Liquid: "+tile.getFluidInSlot(0).toString());
            } else {
                Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Liquid: Empty");
            }
            Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Capacity: "+tile.fluidCapacity[0]);
            return false;
        }
        return false;
    }

    @Override
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.FLUID;
    }
}
