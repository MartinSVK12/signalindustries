package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerMultiblock;
import sunsetsatellite.signalindustries.gui.GuiMultiblock;
import sunsetsatellite.signalindustries.inventories.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockWakingInfuser extends BlockMachineBase {
    public BlockWakingInfuser(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityWakingInfuser();
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityWakingInfuser tile = (TileEntityWakingInfuser) world.getBlockTileEntity(i, j, k);
            if (tile.getMultiblock() != null && tile.getMultiblock().isValid()) {
                SignalIndustries.displayGui(entityplayer, () -> new GuiMultiblock(entityplayer.inventory, tile), new ContainerMultiblock(entityplayer.inventory, tile), tile, i, j, k);
                entityplayer.triggerAchievement(SIAchievements.HORIZONS);
                entityplayer.triggerAchievement(SIAchievements.WAKING3);
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