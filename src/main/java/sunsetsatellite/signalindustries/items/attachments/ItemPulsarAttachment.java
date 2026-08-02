package sunsetsatellite.signalindustries.items.attachments;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.PacketAddParticle;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.entities.EntityShockwave;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IWarpPlayer;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.List;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemPulsarAttachment extends ItemTieredAttachment implements IHasOverlay {
    public ItemPulsarAttachment(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(translationKey, namespaceId, id, attachmentPoints, tier);
    }

    @Override
    public void tick(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, int slot) {
        boolean charging = stack.getData().getBoolean("charging");
        byte charge = stack.getData().getByte("charge");
        int energy = signalumPowerSuit.getEnergy();
        if (stack.getData().getBoolean("charging")) {
            if (charge < 100) {
                if (energy <= 0) {
                    stack.getData().putBoolean("charging", false);
                    player.sendMessage(TextFormatting.RED + "[Pulsar] Ran out of energy while charging!");
                    return;
                }
                if (getItemIdFromSlot(0, stack) == SIItems.warpOrb.id) {
                    signalumPowerSuit.decrementEnergy(80); //charging takes 100 ticks
                } else {
                    signalumPowerSuit.decrementEnergy(40); //charging takes 100 ticks
                }
                stack.getData().putByte("charge", (byte) (charge + 1));
            } else {
                stack.getData().putBoolean("charging", false);
            }

        }
    }

    @Override
    public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {
        if (!stack.getData().getBoolean("charging") && stack.getData().getByte("charge") < 100 && shift && signalumPowerSuit.getEnergy() > 0) {
            stack.getData().putBoolean("charging", true);
            return;
        }
        if (stack.getData().getByte("charge") >= 100) {
            stack.getData().putByte("charge", (byte) 0);
            if (getAbility(stack).contains("Warp")) {
                CompoundTag data = getItemFromSlot(0, stack).getCompound("Data");
                CompoundTag warpPosition = data.getCompound("position");
                if (warpPosition.containsKey("x") && warpPosition.containsKey("y") && warpPosition.containsKey("z")) {
                    if (data.getInteger("dim") != player.dimension) {
                        ((IWarpPlayer) player).warp(data.getInteger("dim"));
                    }
                    player.setPos(warpPosition.getInteger("x"), warpPosition.getInteger("y"), warpPosition.getInteger("z"));
                } else {
                    ((IWarpPlayer) player).warp(SIDimensions.ETERNITY.id);
                }
                stack.getData().getCompound("inventory").getValue().remove(String.valueOf(0));
            } else {
				if(EnvironmentHelper.isMultiplayerServer() || EnvironmentHelper.isSingleplayerClient()){
					EntityShockwave s = new EntityShockwave(world, new Vec3f(player.x, player.y-1, player.z));
					world.entityJoinedWorld(s);
				}
                //if (EnvironmentHelper.isMultiplayerClient()) {
                //    MinecraftServer.getInstance().playerList.sendPacketToAllPlayers(new PacketAddParticle("signalindustries.shockwave", player.x, player.y - 1, player.z, 0.0, 0.0, 0.0, 0));
                //}
                //world.spawnParticle("signalindustries.shockwave", player.x, player.y - 1, player.z, 0.0, 0.0, 0.0, 0, 16, true);
            }
        }
    }

    @Override
    public void altActivate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {
        if (!world.isClientSide) {
            if (!stack.getData().getBoolean("charging")) {
                Catalyst.displayGui(player, new InventoryPulsar(stack), player.inventory.getCurrentSlot(), true, key("gui/pulsar_attch"));
            }
        }
    }

	@Override
	public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, StaticEntityModel model, ItemStack stack) {

	}

    public String getAbility(ItemStack stack) {
        return getItemIdFromSlot(0, stack) == SIItems.warpOrb.id ? TextFormatting.PURPLE + "Warp" : TextFormatting.RED + "Pulse";
    }

    public CompoundTag getItemFromSlot(int id, ItemStack stack) {
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id));
    }

    public int getItemIdFromSlot(int id, ItemStack stack) {
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id)).getShort("id");
    }

	@Override
	public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}

	@Override
	public void renderOverlay(ItemStack pulsar, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {
		int i = height - 52;
		int j = width - 72;
		gui.drawStringShadow(fontRenderer,"The Pulsar", j, i += 16, 0xFFFF0000);
		gui.drawStringShadow(fontRenderer,"Ability: ", j, i += 16, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer,((ItemPulsarAttachment) pulsar.getItem()).getAbility(pulsar), j + fontRenderer.stringWidth("Ability: "), i, 0xFFFF0000);
		gui.drawStringShadow(fontRenderer,"Charge: ", j, i += 10, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer,String.valueOf(pulsar.getData().getByte("charge")) + "%", j + fontRenderer.stringWidth("Charge: "), i, pulsar.getData().getByte("charge") >= 100 ? 0xFFFF0000 : 0xFFFFFFFF);
	}
}
