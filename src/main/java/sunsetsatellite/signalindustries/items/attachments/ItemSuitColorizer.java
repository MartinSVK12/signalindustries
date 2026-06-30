package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemSuitColorizer extends ItemTieredAttachment {

	public final String path;

	public ItemSuitColorizer(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier, String path) {
		super(translationKey, namespaceId, id, attachmentPoints, tier);
		this.path = path;
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
	public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, StaticEntityModel model, ItemStack stack) {

	}
}
