package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerBackpack;
import sunsetsatellite.signalindustries.containers.ContainerPulsar;
import sunsetsatellite.signalindustries.gui.GuiBackpack;
import sunsetsatellite.signalindustries.gui.GuiPulsar;
import sunsetsatellite.signalindustries.inventories.item.InventoryBackpack;
import sunsetsatellite.signalindustries.inventories.item.InventoryPulsar;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemBackpackAttachment extends ItemTieredAttachment{

    public ItemBackpackAttachment(String name, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(name, id, attachmentPoints, tier);
    }

    @Override
    public void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer entityplayer, World world) {
        SignalIndustries.displayGui(entityplayer,new GuiBackpack(entityplayer.inventory,stack),new ContainerBackpack(entityplayer.inventory,stack),new InventoryBackpack(stack),stack);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        SignalIndustries.displayGui(entityplayer,new GuiBackpack(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new ContainerBackpack(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new InventoryBackpack(entityplayer.inventory.getCurrentItem()),itemstack);
        return true;
    }
}
