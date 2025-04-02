package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.util.AttachmentPoint;

import java.util.List;

public interface IAttachment {
    List<AttachmentPoint> getAttachmentPoints();

    void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt);

    void altActivate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt);

    void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, ModelBiped modelBipedMain, ItemStack stack);
}
