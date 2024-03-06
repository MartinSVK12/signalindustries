package sunsetsatellite.signalindustries.blocks.machines;


import net.minecraft.client.Minecraft;
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
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWrathBeaconBase;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityReinforcedWrathBeacon;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityWrathBeacon;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockWrathBeacon extends BlockContainerTiered {

    public BlockWrathBeacon(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        if(tier == Tier.REINFORCED){
            return new TileEntityReinforcedWrathBeacon();
        }
        return new TileEntityWrathBeacon();
    }

    @Override
    public String getDescription(ItemStack stack) {
        if(tier == Tier.REINFORCED){
            String s = super.getDescription(stack);
            return s+"\n"+ TextFormatting.YELLOW+"Multiblock"+ TextFormatting.WHITE;
        } else {
            return super.getDescription(stack);
        }
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            if(tier == Tier.BASIC){
                TileEntityWrathBeaconBase tile = (TileEntityWrathBeaconBase) world.getBlockTileEntity(i, j, k);
                if(tile != null) {
                    tile.activate(entityplayer);
                }
            } else {
                TileEntityReinforcedWrathBeacon tile = (TileEntityReinforcedWrathBeacon) world.getBlockTileEntity(i, j, k);
                if(tile != null && tile.multiblock != null && tile.multiblock.isValidAt(world,new BlockInstance(this,new Vec3i(i,j,k),tile), Direction.getDirectionFromSide(world.getBlockMetadata(i,j,k)))){
                    tile.activate(entityplayer);
                    entityplayer.triggerAchievement(SignalIndustriesAchievementPage.HORIZONS);
                    //Minecraft.getMinecraft(this).ingameGUI.addChatMessage("This world does not know such evil yet.");
                } else {
                    entityplayer.addChatMessage("event.signalindustries.invalidMultiblock");
                }
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityWrathBeaconBase tile = (TileEntityWrathBeaconBase) world.getBlockTileEntity(i,j,k);
        if(tile != null && tile.active){
            for (EntityPlayer player : world.players) {
                player.addChatMessage("Challenge failed!");
            }
            //world.newExplosion(null,i,j,k,5f,false,false);
            if(tier == Tier.REINFORCED){
                TileEntityReinforcedWrathBeacon w = (TileEntityReinforcedWrathBeacon) tile;
                for (BlockInstance bi : w.multiblock.getBlocks(new Vec3i(i, j, k), Direction.Z_POS)) {
                    if(world.getBlockId(bi.pos.x,bi.pos.y,bi.pos.z) == SignalIndustries.fueledEternalTreeLog.id){
                        world.setBlockWithNotify(bi.pos.x, bi.pos.y, bi.pos.z, bi.block.id);
                    }
                }
            }
        }


        super.onBlockRemoved(world, i, j, k, data);
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityWrathBeaconBase tile = (TileEntityWrathBeaconBase) blockAccess.getBlockTileEntity(x,y,z);
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        if(tile.active){
            return SignalIndustries.textures.get(tile.tier.name()+".wrathBeacon.active").getTexture(Side.getSideById(index));
        }
        return this.atlasIndices[index];
    }
}
