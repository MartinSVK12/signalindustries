package sunsetsatellite.signalindustries.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.gui.GuiRecipeMaker;
import sunsetsatellite.signalindustries.inventories.TileEntityRecipeMaker;

public class BlockRecipeMaker extends BlockTileEntity {

    public BlockRecipeMaker(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityRecipeMaker();
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityRecipeMaker tile = (TileEntityRecipeMaker) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(new GuiRecipeMaker(entityplayer.inventory,tile));
                //SignalIndustries.displayGui(entityplayer,new GuiEnergyCell(entityplayer.inventory, tile),new ContainerFluidTank(entityplayer.inventory,tile),tile);
            }
            return true;
        }
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }
}
