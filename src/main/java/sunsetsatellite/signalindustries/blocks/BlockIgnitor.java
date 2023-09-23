package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IConduitsConnect;
import sunsetsatellite.signalindustries.inventories.TileEntityIgnitor;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockIgnitor extends BlockContainerTiered implements IConduitsConnect {

    public BlockIgnitor(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        if(world.getBlock(x,y-1,z) == this && world.getBlockMetadata(x,y-1,z) != 1){
            world.setBlockMetadataWithNotify(x,y,z,1);
        } else {
            world.setBlockMetadataWithNotify(x,y,z,0);
        }
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        world.setBlockTileEntity(i, j, k, this.getNewBlockEntity());
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(Side side, int meta) {
        return this.atlasIndices[side.getId()];
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityIgnitor tile = (TileEntityIgnitor) blockAccess.getBlockTileEntity(x,y,z);
        int meta = blockAccess.getBlockMetadata(x,y-1,z);
        if(tile == null) return atlasIndices[side.getId()];
        if(tile.isBurning()){
            if(meta == 0 && blockAccess.getBlock(x,y-1,z) == this){
                return SignalIndustries.textures.get(tier.name()+".ignitor.inverted.active").getTexture(side);
            } else {
                return SignalIndustries.textures.get(tier.name()+".ignitor.active").getTexture(side);
            }
        } else if (tile.isReady()) {
            if(meta == 0 && blockAccess.getBlock(x,y-1,z) == this){
                return SignalIndustries.textures.get(tier.name()+".ignitor.inverted.ready").getTexture(side);
            } else {
                return SignalIndustries.textures.get(tier.name()+".ignitor.ready").getTexture(side);
            }
        } else {
            if(meta == 0 && blockAccess.getBlock(x,y-1,z) == this){
                return SignalIndustries.textures.get(tier.name()+".ignitor.inverted").getTexture(side);
            } else {
                return atlasIndices[side.getId()];
            }
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityIgnitor();
    }
}
