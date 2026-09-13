package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.SIConfig;
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
		int cost = SIConfig.config.getInt("Balance.wingsFlightCost");
        if (signalumPowerSuit.getEnergy() < cost) {
            stack.getData().putBoolean("active", false);
            return;
        }
        if (stack.getData().getBoolean("active")) {
            signalumPowerSuit.decrementEnergy(cost);
        }
    }

    @Override
    public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {
		int cost = SIConfig.config.getInt("Balance.wingsFlightCost");
        if (signalumPowerSuit.getEnergy() >= cost) {
            boolean state = stack.getData().getBoolean("active");
            stack.getData().putBoolean("active", !state);
        }
    }

    @Override
    public void altActivate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, StaticEntityModel modelBipedMain, ItemStack stack) {
        if (stack.getData().getBoolean("active")) {
            loadTexture("/assets/signalindustries/textures/attachments/wings_texture.png");
        } else {
            loadTexture("/assets/signalindustries/textures/attachments/wings_texture_inactive.png");
        }
		StaticEntityModel model = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.wings", 0);
        model.render();
    }
}
