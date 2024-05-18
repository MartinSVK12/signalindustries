package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.helper.ModelHelper;
import org.useless.dragonfly.model.entity.BenchEntityModel;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemWingsAttachment extends ItemTieredAttachment {
    public ItemWingsAttachment(String name, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(name, id, attachmentPoints, tier);
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
        }
    }

    @Override
    public void renderWhenAttached(EntityPlayer player, ItemStack stack) {
        if(stack.getData().getBoolean("active")){
            loadTexture("/assets/signalindustries/attachments/wings_texture.png");
        } else {
            loadTexture("/assets/signalindustries/attachments/wings_texture_inactive.png");
        }
        BenchEntityModel model = ModelHelper.getOrCreateEntityModel(SignalIndustries.MOD_ID, "wings.json", BenchEntityModel.class);
        GL11.glTranslatef(0f,0,0.1f);
        model.renderModel(0,0,0,0,0,0.0625f);
    }
}
