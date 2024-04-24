package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.gui.GuiSignalumReactor;
import sunsetsatellite.signalindustries.inventories.machines.TileEntitySignalumReactor;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.util.Tier;




public class BlockSignalumReactorCore extends BlockContainerTiered {
    public BlockSignalumReactorCore(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
        hasOverbright = true;
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
            if(tile.multiblock != null && tile.multiblock.isValidAt(world,new BlockInstance(this,new Vec3i(i,j,k),tile), Direction.Z_POS/*Direction.getDirectionFromSide(world.getBlockMetadata(i,j,k)))*/)){
                SignalIndustries.displayGui(entityplayer, () -> new GuiSignalumReactor(entityplayer.inventory, tile), tile, i, j, k);
                entityplayer.triggerAchievement(SignalIndustriesAchievementPage.HORIZONS);
            } else {
                entityplayer.addChatMessage("event.signalindustries.invalidMultiblock");
            }
            return true;
        }
    }

    @Override
    public String getDescription(ItemStack stack) {
        String s = super.getDescription(stack);
        return s+"\n"+ TextFormatting.YELLOW+"Multiblock"+ TextFormatting.WHITE;
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntitySignalumReactor tile = (TileEntitySignalumReactor) blockAccess.getBlockTileEntity(x,y,z);
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        if(tile.isActive() && tile.tier == tier){
            return SignalIndustries.textures.get(tile.tier.name()+".signalumReactorCore.active").getTexture(Side.getSideById(index));
        }

        return this.atlasIndices[index];
    }

    @Override
    public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntitySignalumReactor tile = (TileEntitySignalumReactor) blockAccess.getBlockTileEntity(x,y,z);
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index = Sides.orientationLookUpHorizontal[6 * meta + side];
        if(tile.isActive() && tile.tier == tier){
            return SignalIndustries.textures.get(tile.tier.name()+".signalumReactorCore.active.overlay").getTexture(Side.getSideById(index));
        }

        return -1;
    }
}
