package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.client.world.chunk.provider.ChunkProviderDynamic;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.tiles.TileEntityChunkloader;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicChunkloader extends BlockLogicMachine {

    public BlockLogicChunkloader(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, material, tier, tileEntitySupplier, guiId);
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
    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityChunkloader tile = (TileEntityChunkloader) world.getTileEntity(x, y, z);
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
