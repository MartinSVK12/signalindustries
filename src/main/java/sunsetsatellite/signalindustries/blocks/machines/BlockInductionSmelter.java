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
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityInductionSmelter;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockInductionSmelter extends BlockMachineBase {
    public BlockInductionSmelter(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);

    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityInductionSmelter();
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityInductionSmelter tile = (TileEntityInductionSmelter) world.getBlockTileEntity(i, j, k);
            if (tile.getMultiblock() != null && tile.getMultiblock().isValidAt(world, new BlockInstance(this, new Vec3i(i, j, k), tile), Direction.getDirectionFromSide(world.getBlockMetadata(i, j, k)).getOpposite())) {
                //SignalIndustries.displayGui(entityplayer,() -> new GuiDimAnchor(entityplayer.inventory, tile),new ContainerDimAnchor(entityplayer.inventory,tile),tile,i,j,k);
                entityplayer.triggerAchievement(SIAchievements.HORIZONS);
            } else {
                entityplayer.sendTranslatedChatMessage("event.signalindustries.invalidMultiblock");
            }
            return true;
        }

    }

    @Override
    public String getDescription(ItemStack stack) {
        String s = super.getDescription(stack);
        return s + "\n" + TextFormatting.YELLOW + "Multiblock" + TextFormatting.WHITE;
    }
}
