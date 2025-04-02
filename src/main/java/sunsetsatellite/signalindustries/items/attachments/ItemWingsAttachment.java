package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import org.useless.DragonFly;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemWingsAttachment extends ItemTieredAttachment {
    public ItemWingsAttachment(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(translationKey, namespaceId, id, attachmentPoints, tier);
    }

    @Override
    public void tick(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, int slot) {
        if(signalumPowerSuit.getEnergy() < 1){
            stack.getData().putBoolean("active",false);
            return;
        }
        if(stack.getData().getBoolean("active")){
            signalumPowerSuit.decrementEnergy(1);
        }
    }

    @Override
   public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {
        if(signalumPowerSuit.getEnergy() >= 1) {
            boolean state = stack.getData().getBoolean("active");
            stack.getData().putBoolean("active", !state);
        }
    }

    @Override
    public void altActivate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, ModelBiped modelBipedMain, ItemStack stack) {
        if(stack.getData().getBoolean("active")){
            loadTexture("/assets/signalindustries/attachments/wings_texture.png");
        } else {
            loadTexture("/assets/signalindustries/attachments/wings_texture_inactive.png");
        }
        StaticEntityModel model = DragonFly.loadEntityModel("geometry.signalindustries.wings", 0);
        GL11.glRotatef(180, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.0625f, 0.0625f, 0.0625f);
        GL11.glTranslatef(0f,0,0.1f);
        model.render(Tessellator.instance);
    }

    /*@Override
   public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void tick(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, int slot) {

    }

    @Override
    public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, ModelBiped modelBipedMain, ItemStack stack) {

    }*/
}
