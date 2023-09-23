package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.GuiSignalumReactor;
import sunsetsatellite.signalindustries.inventories.TileEntitySignalumReactor;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockSignalumReactorCore extends BlockContainerTiered{
    public BlockSignalumReactorCore(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntitySignalumReactor();
    }


    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntitySignalumReactor tile = (TileEntitySignalumReactor) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer, new GuiSignalumReactor(entityplayer.inventory, tile), tile, i, j, k);
            }
            return true;
        }
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        /*TileEntityTieredMachine tile = (TileEntityTieredMachine) blockAccess.getBlockTileEntity(x,y,z);
        if(tile.isBurning()){
            return SignalIndustries.textures.get(tile.tier.name()+".signalumReactorCore.active").getTexture(Side.getSideById(index));
        }*/
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        return this.atlasIndices[index];
    }

}
