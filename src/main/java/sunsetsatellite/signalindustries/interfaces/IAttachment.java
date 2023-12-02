package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;

import java.util.List;

public interface IAttachment {
    List<AttachmentPoint> getAttachmentPoints();

    void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world);

    void openSettings(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world);
}
