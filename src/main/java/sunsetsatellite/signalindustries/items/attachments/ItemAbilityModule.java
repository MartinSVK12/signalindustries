package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemAbilityModule extends ItemTieredAttachment {
    public ItemAbilityModule(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(translationKey, namespaceId, id, attachmentPoints, tier);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player player) {
        if (world.isClientSide) {
            return itemstack;
        } else {
            Catalyst.displayGui(player, new InventoryAbilityModule(itemstack), player.inventory.getCurrentItemIndex(), false, key("gui/ability_module"));
            return itemstack;
        }
    }

    @Override
    public void tick(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, int slot) {

    }

    @Override
    public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void altActivate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, ModelBiped modelBipedMain, ItemStack stack) {

    }
}
