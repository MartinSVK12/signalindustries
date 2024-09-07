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
import sunsetsatellite.signalindustries.containers.ContainerExtractor;
import sunsetsatellite.signalindustries.containers.ContainerReinforcedExtractor;
import sunsetsatellite.signalindustries.gui.GuiExtractor;
import sunsetsatellite.signalindustries.gui.GuiReinforcedExtractor;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityExtractor;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityReinforcedExtractor;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockExtractor extends BlockMachineBase {

    public BlockExtractor(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);

    }

    @Override
    protected TileEntity getNewBlockEntity() {
        if (tier == Tier.REINFORCED) {
            return new TileEntityReinforcedExtractor();
        }
        return new TileEntityExtractor();
    }

    @Override
    public String getDescription(ItemStack stack) {
        if (tier == Tier.REINFORCED) {
            String s = super.getDescription(stack);
            return s + "\n" + TextFormatting.YELLOW + "Multiblock" + TextFormatting.WHITE;
        } else {
            return super.getDescription(stack);
        }
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        dropContents(world, i, j, k);

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
            if (tier == Tier.REINFORCED) {
                TileEntityReinforcedExtractor tile = (TileEntityReinforcedExtractor) world.getBlockTileEntity(i, j, k);
                if (tile != null) {
                    if (tile.multiblock != null && tile.multiblock.isValidAt(world, new BlockInstance(this, new Vec3i(i, j, k), tile), Direction.getDirectionFromSide(world.getBlockMetadata(i, j, k)).getOpposite())) {
                        SignalIndustries.displayGui(entityplayer, () -> new GuiReinforcedExtractor(entityplayer.inventory, tile), new ContainerReinforcedExtractor(entityplayer.inventory, tile), tile, i, j, k);
                        entityplayer.triggerAchievement(SIAchievements.HORIZONS);
                    } else {
                        entityplayer.sendTranslatedChatMessage("event.signalindustries.invalidMultiblock");
                    }
                }
                return true;
            } else {
                TileEntityExtractor tile = (TileEntityExtractor) world.getBlockTileEntity(i, j, k);
                if (tile != null) {
                    SignalIndustries.displayGui(entityplayer, () -> new GuiExtractor(entityplayer.inventory, tile), new ContainerExtractor(entityplayer.inventory, tile), tile, i, j, k);
                }
                return true;
            }
        }
    }

}
