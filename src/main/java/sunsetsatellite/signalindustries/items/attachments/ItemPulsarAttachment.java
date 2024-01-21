package sunsetsatellite.signalindustries.items.attachments;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import org.lwjgl.input.Keyboard;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerPulsarAttachment;
import sunsetsatellite.signalindustries.entities.ExplosionEnergy;
import sunsetsatellite.signalindustries.gui.GuiPulsarAttachment;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;
import sunsetsatellite.signalindustries.inventories.item.InventoryPulsar;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemPulsarAttachment extends ItemTieredAttachment implements IHasOverlay {

    public ItemPulsarAttachment(String name, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(name, id, attachmentPoints, tier);
    }

    @Override
    public void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
        boolean alt = (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        if(!stack.getData().getBoolean("charging") && stack.getData().getByte("charge") < 100 && signalumPowerSuit.getEnergy() > 0 && alt) {
            stack.getData().putBoolean("charging", true);
        }
        if(stack.getData().getByte("charge") >= 100){
            stack.getData().putByte("charge", (byte) 0);
            world.playSoundAtEntity(player, "signalindustries.pulsar", 0.5F, 1.0f);
            if(getAbility(stack).contains("Warp")){
                ExplosionEnergy ex = new ExplosionEnergy(world,player,player.x,player.y,player.z,3f);
                ex.doExplosionA();
                ex.doExplosionB(true,0.7f,0.0f,0.7f);
                CompoundTag warpPosition = getItemFromSlot(0,stack).getCompound("Data").getCompound("position");
                if(warpPosition.containsKey("x") && warpPosition.containsKey("y") && warpPosition.containsKey("z")){
                    player.setPos(warpPosition.getInteger("x"),warpPosition.getInteger("y"),warpPosition.getInteger("z"));
                    ex = new ExplosionEnergy(world,player,player.x,player.y,player.z,3f);
                    ex.doExplosionA();
                    ex.doExplosionB(true,0.7f,0.0f,0.7f);
                } else {
                    SignalIndustries.usePortal(SignalIndustries.dimEternity.id);
                }
                ((INBTCompound)stack.getData().getCompound("inventory")).removeTag(String.valueOf(0));
            } else {
                world.spawnParticle("pulse_shockwave", player.x, player.y, player.z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void openSettings(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
        if(!stack.getData().getBoolean("charging")){
            SignalIndustries.displayGui(player,new GuiPulsarAttachment(player.inventory,stack),new ContainerPulsarAttachment(player.inventory,stack),new InventoryPulsar(stack),stack);
        }
    }

    @Override
    public void tick(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world, int slot) {
        boolean charging = stack.getData().getBoolean("charging");
        byte charge = stack.getData().getByte("charge");
        int energy = signalumPowerSuit.getEnergy();
        if(stack.getData().getBoolean("charging")){
            if(charge < 100){
                if(energy <= 0){
                    stack.getData().putBoolean("charging",false);
                    Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage(TextFormatting.WHITE+"The Pulsar> "+TextFormatting.RED+" ERROR: "+TextFormatting.WHITE+"Ran out of energy while charging!");
                    return;
                }
                if(getItemIdFromSlot(0,stack) == SignalIndustries.warpOrb.id){
                    signalumPowerSuit.decrementEnergy(80); //charging takes 100 ticks
                } else {
                    signalumPowerSuit.decrementEnergy(40); //charging takes 100 ticks
                }
                stack.getData().putByte("charge", (byte) (charge+1));
            } else {
                stack.getData().putBoolean("charging",false);
            }

        }
        super.tick(stack, signalumPowerSuit, player, world, slot);
    }

    public int getItemIdFromSlot(int id, ItemStack stack){
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id)).getShort("id");
    }

    public CompoundTag getItemFromSlot(int id, ItemStack stack){
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id));
    }

    public CompoundTag getFluidStack(int id, ItemStack stack){
        return stack.getData().getCompound("fluidInventory").getCompound(String.valueOf(id));
    }

    public String getAbility(ItemStack stack){
        return getItemIdFromSlot(0,stack) == SignalIndustries.warpOrb.id ? TextFormatting.PURPLE+"Warp" : TextFormatting.RED+"Pulse";
    }

    @Override
    public void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {

    }

    @Override
    public void renderOverlay(ItemStack pulsar, SignalumPowerSuit signalumPowerSuit, GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {
        int i = height - 52;
        int j = width - 72;
        fontRenderer.drawStringWithShadow("The Pulsar", j, i += 16, 0xFFFF0000);
        fontRenderer.drawStringWithShadow("Ability: ", j, i += 16, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow(((ItemPulsarAttachment) pulsar.getItem()).getAbility(pulsar), j + fontRenderer.getStringWidth("Ability: "), i, 0xFFFF0000);
        fontRenderer.drawStringWithShadow("Charge: ", j, i += 10, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow(String.valueOf(pulsar.getData().getByte("charge")) + "%", j + fontRenderer.getStringWidth("Charge: "), i, pulsar.getData().getByte("charge") >= 100 ? 0xFFFF0000 : 0xFFFFFFFF);
    }
}
