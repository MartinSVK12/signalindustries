package sunsetsatellite.signalindustries.blocks;


import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityConduit;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockConduit extends BlockContainerTiered {

    public BlockConduit(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    protected TileEntity getNewBlockEntity() {
        return new TileEntityConduit();
    }

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
                Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Liquid: "+tile.getFluidInSlot(0).toString());
            } else {
                Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Liquid: Empty");
            }
            Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Capacity: "+tile.fluidCapacity[0]);
            Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Is pressurized? "+tile.isPressurized);
            return false;
        }
        return false;
    }
}
