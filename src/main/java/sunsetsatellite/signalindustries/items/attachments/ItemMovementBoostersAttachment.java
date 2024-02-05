package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemMovementBoostersAttachment extends ItemTieredAttachment {
    public ItemMovementBoostersAttachment(String name, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(name, id, attachmentPoints, tier);
        setMaxStackSize(2);
    }

    @Override
    public void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
        super.activate(stack, signalumPowerSuit, player, world);
        if(signalumPowerSuit.getEnergy() >= 1) {
            boolean state = stack.getData().getBoolean("active");
            stack.getData().putBoolean("active", !state);
        }
    }

    @Override
    public void tick(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world, int slot) {
        super.tick(stack, signalumPowerSuit, player, world, slot);
        if(signalumPowerSuit.getEnergy() < 1){
            stack.getData().putBoolean("active",false);
            return;
        }
        if(stack.getData().getBoolean("active")){
            signalumPowerSuit.decrementEnergy(1);
            SignalIndustries.spawnParticle(new EntityColorParticleFX(world,player.x+0.2f,player.y-1.3f,player.z,player.xd,player.yd,player.zd,1,1,0,1));
            SignalIndustries.spawnParticle(new EntityColorParticleFX(world,player.x-0.2f,player.y-1.3f,player.z,player.xd,player.yd,player.zd,1,1,0,1));
        }
    }
}
