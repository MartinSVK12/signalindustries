package sunsetsatellite.signalindustries.blocks.machines;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerExternalIO;
import sunsetsatellite.signalindustries.gui.GuiExternalIO;
import sunsetsatellite.signalindustries.inventories.TileEntityExternalIO;
import sunsetsatellite.signalindustries.util.Tier;


public class BlockExternalIO extends BlockMachineBase {
    public BlockExternalIO(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityExternalIO();
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(super.blockActivated(world, i, j, k, entityplayer)){
            return true;
        }
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityExternalIO tile = (TileEntityExternalIO) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,() -> new GuiExternalIO(entityplayer.inventory, tile),new ContainerExternalIO(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    /*
        @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityExternalIO tile = (TileEntityExternalIO) blockAccess.getBlockTileEntity(x,y,z);
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        Connection connection = tile.itemConnections.get(Direction.getDirectionFromSide(side.getId()));
        if(tier == Tier.REINFORCED){
            if(connection == Connection.INPUT){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"reinforced_external_io_input.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(connection == Connection.OUTPUT){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"reinforced_external_io_output.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(connection == Connection.BOTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"reinforced_external_io_both.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"reinforced_external_io_blank.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else {
            if(connection == Connection.INPUT){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"external_io_input.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(connection == Connection.OUTPUT){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"external_io_output.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(connection == Connection.BOTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"external_io_both.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,"external_io_blank.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        }
    }
     */
}
