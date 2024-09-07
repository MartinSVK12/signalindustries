package sunsetsatellite.signalindustries.items;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.util.SIMultiblock;

import java.util.Objects;

public class ItemBlueprint extends Item implements ICustomDescription {
    public ItemBlueprint(String name, int id) {
        super(name, id);
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack stack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        TileEntity tile = world.getBlockTileEntity(blockX, blockY, blockZ);
        if(tile instanceof IMultiblock) {
            IMultiblock multiblock = (IMultiblock)tile;
            stack.getData().putString("multiblock",multiblock.getMultiblock().translateKey);
            entityplayer.sendMessage("Blueprint written down!");
        }
        return super.onUseItemOnBlock(stack, entityplayer, world, blockX, blockY, blockZ, side, xPlaced, yPlaced);
    }

    @Override
    public String getDescription(ItemStack stack) {
        String key = stack.getData().getStringOrDefault("multiblock", "");
        if (!Objects.equals(key, "")){
            SIMultiblock multiblock = (SIMultiblock) Multiblock.multiblocks.get(key.replace("multiblock.signalindustries.",""));
            return "Tier: " + multiblock.tier.getTextColor() + multiblock.tier.getRank() + "\n" + TextFormatting.LIGHT_BLUE + multiblock.getTranslatedName() + TextFormatting.RESET;
        }
        return TextFormatting.GRAY + "Blank" + TextFormatting.RESET;
    }
}
