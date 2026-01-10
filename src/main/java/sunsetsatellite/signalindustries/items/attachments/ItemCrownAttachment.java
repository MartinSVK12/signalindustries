package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

//TODO: implement
public class ItemCrownAttachment extends ItemTieredAttachment {
    public ItemCrownAttachment(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(translationKey, namespaceId, id, attachmentPoints, tier);
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
