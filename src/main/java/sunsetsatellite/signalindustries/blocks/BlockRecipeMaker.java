package sunsetsatellite.signalindustries.blocks;

import net.minecraft.client.Minecraft;

import sunsetsatellite.signalindustries.gui.GuiRecipeMaker;
import sunsetsatellite.signalindustries.inventories.TileEntityRecipeMaker;

public class BlockRecipeMaker extends BlockContainer {
    public BlockRecipeMaker(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityRecipeMaker();
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityRecipeMaker tile = (TileEntityRecipeMaker) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiRecipeMaker(entityplayer.inventory,tile));
                //SignalIndustries.displayGui(entityplayer,new GuiEnergyCell(entityplayer.inventory, tile),new ContainerFluidTank(entityplayer.inventory,tile),tile);
            }
            return true;
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
