package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.experimental.ChunkProviderDynamic;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityChunkloader;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockChunkloader extends BlockContainerTiered {
    public BlockChunkloader(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityChunkloader();
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        if (world.getChunkProvider() instanceof ChunkProviderDynamic) {
            Chunk chunk = world.getChunkFromBlockCoords(x, z);
            ((ChunkProviderDynamic) world.getChunkProvider()).removeFromForceLoaded(chunk.xPosition, chunk.zPosition);
            SignalIndustries.chunkLoaders.remove(new ChunkCoordinates(x,y,z));
        }
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityChunkloader tile = (TileEntityChunkloader) world.getBlockTileEntity(x, y, z);
            if (world.getChunkProvider() instanceof ChunkProviderDynamic) {
                tile.active = !tile.active;
                if (tile.active) {
                    Chunk chunk = world.getChunkFromBlockCoords(x, z);
                    if (!((ChunkProviderDynamic) world.getChunkProvider()).keepLoaded(chunk.xPosition, chunk.zPosition)) {
                        tile.active = false;
                        player.sendMessage("Chunkloader failed to active. Either this chunk is already chunkloaded or the chunkloading limit has been reached.");
                        return true;
                    } else {
                        SignalIndustries.chunkLoaders.add(new ChunkCoordinates(x,y,z));
                    }
                } else {
                    Chunk chunk = world.getChunkFromBlockCoords(x, z);
                    ((ChunkProviderDynamic) world.getChunkProvider()).removeFromForceLoaded(chunk.xPosition, chunk.zPosition);
                    SignalIndustries.chunkLoaders.remove(new ChunkCoordinates(x,y,z));
                }
                player.sendMessage("Chunkloader " + (tile.active ? "activated!" : "deactivated."));
                return true;
            } else {
                player.sendMessage("Chunkloading requires the experimental dynamic chunk provider, please enable it in the Signal Industries configuration file.");
                player.sendMessage("It can be found in your instances config folder as signalindustries.cfg.");
            }
        }
        return true;
    }
}
