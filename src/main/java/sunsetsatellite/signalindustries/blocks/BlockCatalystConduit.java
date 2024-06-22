package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.core.util.IConduitBlock;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityCatalystConduit;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockCatalystConduit extends BlockContainerTiered implements IConduitBlock {

    public BlockCatalystConduit(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    protected TileEntity getNewBlockEntity() {
        return new TileEntityCatalystConduit();
    }

    public boolean isSolidRender() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }


    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (entityplayer.isSneaking() && !world.isClientSide) {
            TileEntityCatalystConduit tile = (TileEntityCatalystConduit) world.getBlockTileEntity(i, j, k);
            entityplayer.sendMessage(TextFormatting.WHITE + "Max Transfer: " + TextFormatting.LIGHT_GRAY + "IN: " + tile.maxReceive + TextFormatting.WHITE + " / " + TextFormatting.LIGHT_GRAY + "OUT: " + tile.maxProvide + " | "
                    + TextFormatting.WHITE + "Energy: " + TextFormatting.LIGHT_GRAY + tile.energy + TextFormatting.WHITE + " / " + TextFormatting.LIGHT_GRAY + tile.capacity);
            return false;
        }
        return false;
    }

    @Override
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.CATALYST_ENERGY;
    }
}
