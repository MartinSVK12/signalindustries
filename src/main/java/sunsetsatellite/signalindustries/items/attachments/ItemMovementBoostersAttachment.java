package sunsetsatellite.signalindustries.items.attachments;


import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemMovementBoostersAttachment extends ItemTieredAttachment {

    boolean alreadyTookEnergy = false;

    public ItemMovementBoostersAttachment(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(translationKey, namespaceId, id, attachmentPoints, tier);
    }

    @Override
   public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {
        if(signalumPowerSuit.getEnergy() >= 1 && signalumPowerSuit.isActive() && signalumPowerSuit.hasAttachment(SIItems.movementBoosters, Catalyst.listOf(SignalumPowerSuit.AttachmentLocation.BOOT_BACK_L, SignalumPowerSuit.AttachmentLocation.BOOT_BACK_R))) {
            boolean state = stack.getData().getBoolean("active");
            stack.getData().putBoolean("active", !state);
        }
    }

    @Override
    public void altActivate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, ModelBiped modelBipedMain, ItemStack stack) {

    }

    @Override
    public void tick(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, int slot) {
        if(signalumPowerSuit.getEnergy() < 1){
            stack.getData().putBoolean("active",false);
            return;
        }
        if(stack.getData().getBoolean("active")){
            if(!alreadyTookEnergy) {
                signalumPowerSuit.decrementEnergy(1);
                alreadyTookEnergy = true;
            } else {
                alreadyTookEnergy = false;
            }
        }
    }
}
