package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerDimAnchor;
import sunsetsatellite.signalindustries.gui.GuiDimAnchor;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockDimensionalAnchor extends BlockMachineBase {

    public BlockDimensionalAnchor(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);

    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityDimensionalAnchor();
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
    }

    @Override
    public String getDescription(ItemStack stack) {
        String s = super.getDescription(stack);
        return s + "\n" + TextFormatting.YELLOW + "Multiblock" + TextFormatting.WHITE;
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityDimensionalAnchor tile = (TileEntityDimensionalAnchor) world.getBlockTileEntity(i, j, k);
        if (tile != null) {
            for (Direction dir : Direction.values()) {

                if (dir == Direction.Y_NEG || dir == Direction.Y_POS) continue;
                Vec3i v = dir.getVec().multiply(2);
                Vec3i tileVec = new Vec3i(i, j, k);
                TileEntity stabilizer = dir.getTileEntity(world, tileVec.add(v));
                if (stabilizer instanceof TileEntityStabilizer) {
                    ((TileEntityStabilizer) stabilizer).connectedTo = null;
                }
            }
            dropContents(world, i, j, k);
        }

        super.onBlockRemoved(world, i, j, k, data);
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityDimensionalAnchor tile = (TileEntityDimensionalAnchor) world.getBlockTileEntity(i, j, k);
            if (tile.multiblock != null && tile.multiblock.isValidAt(world, new BlockInstance(this, new Vec3i(i, j, k), tile), Direction.getDirectionFromSide(world.getBlockMetadata(i, j, k)).getOpposite())) {
                SignalIndustries.displayGui(entityplayer, () -> new GuiDimAnchor(entityplayer.inventory, tile), new ContainerDimAnchor(entityplayer.inventory, tile), tile, i, j, k);
                entityplayer.triggerAchievement(SIAchievements.HORIZONS);
            } else {
                entityplayer.sendTranslatedChatMessage("event.signalindustries.invalidMultiblock");
            }
            return true;
        }
    }
}
