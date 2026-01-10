package sunsetsatellite.signalindustries.items;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.invs.InventoryBlueprint;
import sunsetsatellite.signalindustries.util.SIMultiblock;

import java.util.Objects;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemGoldprint extends Item implements ICustomDescription {

    public ItemGoldprint(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack stack, Player entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if(entityplayer.isSneaking()){
            stack.getData().getValue().remove("multiblock");
            stack.getData().getValue().remove("structure");
            entityplayer.sendMessage("Blueprint cleared!");
        }
        return super.onUseItemOnBlock(stack, entityplayer, world, blockX, blockY, blockZ, side, xPlaced, yPlaced);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player player) {
        if(!player.isSneaking()){
            Catalyst.displayGui(player, new InventoryBlueprint(itemstack), player.inventory.getCurrentItemIndex(), false, key("gui/blueprint"));
        }
        return itemstack;
    }

    @Override
    public String getDescription(ItemStack stack) {
        String key2 = stack.getData().getStringOrDefault("structure", "");
        if (!key2.isEmpty()) {
            return TextFormatting.GRAY + key2 + TextFormatting.RESET;
        }
        return TextFormatting.GRAY + "Blank" + TextFormatting.RESET;
    }
}
