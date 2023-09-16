package sunsetsatellite.signalindustries.items;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerPulsar;
import sunsetsatellite.signalindustries.entities.ExplosionEnergy;
import sunsetsatellite.signalindustries.gui.GuiPulsar;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;
import sunsetsatellite.signalindustries.inventories.InventoryPulsar;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemPulsar extends ItemTiered implements IHasOverlay {
    public ItemPulsar(int i, Tier tier) {
        super(i, tier);
    }

    @Override
    public String getDescription(ItemStack stack) {
        String text = super.getDescription(stack);
        String ability = getAbility(stack);
        text += "\nCharge: "+ (stack.getData().getByte("charge") >= 100 ? TextFormatting.RED : TextFormatting.LIGHT_GRAY) +stack.getData().getByte("charge")+"%"+TextFormatting.WHITE+" | Ability: "+ability;
        return text;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if(entityplayer.isSneaking() && !itemstack.getData().getBoolean("charging")){
            SignalIndustries.displayGui(entityplayer,new GuiPulsar(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new ContainerPulsar(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new InventoryPulsar(entityplayer.inventory.getCurrentItem()),itemstack);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(!itemstack.getData().getBoolean("charging") && itemstack.getData().getByte("charge") < 100 && !entityplayer.isSneaking() && getFluidStack(0,itemstack).getInteger("amount") > 0) {
            itemstack.getData().putBoolean("charging", true);
            return itemstack;
        }
        if(itemstack.getData().getByte("charge") >= 100){
            itemstack.getData().putByte("charge", (byte) 0);
            world.playSoundAtEntity(entityplayer, "signalindustries.pulsar", 0.5F, 1.0f);
            if(getAbility(itemstack).contains("Warp")){
                ExplosionEnergy ex = new ExplosionEnergy(world,entityplayer,entityplayer.x,entityplayer.y,entityplayer.z,3f);
                ex.doExplosionA();
                ex.doExplosionB(true,0.7f,0.0f,0.7f);
                CompoundTag warpPosition = getItemFromSlot(0,itemstack).getCompound("Data").getCompound("position");
                if(warpPosition.containsKey("x") && warpPosition.containsKey("y") && warpPosition.containsKey("z")){
                    entityplayer.setPos(warpPosition.getInteger("x"),warpPosition.getInteger("y"),warpPosition.getInteger("z"));
                    ex = new ExplosionEnergy(world,entityplayer,entityplayer.x,entityplayer.y,entityplayer.z,3f);
                    ex.doExplosionA();
                    ex.doExplosionB(true,0.7f,0.0f,0.7f);
                } else {
                    SignalIndustries.usePortal(SignalIndustries.dimEternity.id);
                }
                ((INBTCompound)itemstack.getData().getCompound("inventory")).removeTag(String.valueOf(0));
            } else {
                world.spawnParticle("pulse_shockwave", entityplayer.x, entityplayer.y, entityplayer.z, 0.0, 0.0, 0.0);
            }
            return itemstack;
        }
        return itemstack;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        boolean charging = itemstack.getData().getBoolean("charging");
        byte charge = itemstack.getData().getByte("charge");
        int energy = getFluidStack(0,itemstack).getInteger("amount");
        if(itemstack.getData().getBoolean("charging")){
            if(charge < 100){
                if(energy <= 0){
                    getFluidStack(0,itemstack).putInt("amount",0);
                    itemstack.getData().putBoolean("charging",false);
                    Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage(TextFormatting.WHITE+"The Pulsar> "+TextFormatting.RED+" ERROR: "+TextFormatting.WHITE+"Ran out of energy while charging!");
                    return;
                }
                if(getItemIdFromSlot(0,itemstack) == SignalIndustries.warpOrb.id){
                    getFluidStack(0,itemstack).putInt("amount",energy-80); //charging takes 100 ticks
                } else {
                    getFluidStack(0,itemstack).putInt("amount",energy-40); //charging takes 100 ticks
                }
                itemstack.getData().putByte("charge", (byte) (charge+1));
            } else {
                itemstack.getData().putBoolean("charging",false);
            }

        }
        super.inventoryTick(itemstack, world, entity, i, flag);
    }

    @Override
    public int getIconIndex(ItemStack itemstack) {
        if(getFluidStack(0,itemstack).getInteger("amount") <= 0 && itemstack.getData().getByte("charge") <= 0){
            return Item.iconCoordToIndex(SignalIndustries.pulsarTex[0][0],SignalIndustries.pulsarTex[0][1]);
        }
        int tex = Item.iconCoordToIndex(SignalIndustries.pulsarTex[1][0],SignalIndustries.pulsarTex[1][1]);
        if(itemstack.getData().getByte("charge") >= 100){
            tex = Item.iconCoordToIndex(SignalIndustries.pulsarTex[2][0],SignalIndustries.pulsarTex[2][1]);
        }
        if(getItemIdFromSlot(0,itemstack) == SignalIndustries.warpOrb.id){
            tex = Item.iconCoordToIndex(SignalIndustries.pulsarTex[3][0],SignalIndustries.pulsarTex[3][1]);
            if(itemstack.getData().getByte("charge") >= 100){
                tex = Item.iconCoordToIndex(SignalIndustries.pulsarTex[4][0],SignalIndustries.pulsarTex[4][1]);
            }
        }
        return tex;
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
        InventoryPlayer inv = player.inventory;
        ItemStack pulsar = inv.getCurrentItem();
        int i = (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) ? height - 128 : height - 64;
        fontRenderer.drawStringWithShadow("The Pulsar", 4, i += 16, 0xFFFF0000);
        fontRenderer.drawStringWithShadow("Ability: ", 4, i += 16, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow(((ItemPulsar) pulsar.getItem()).getAbility(pulsar), 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFFFF0000);
        fontRenderer.drawStringWithShadow("Charge: ", 4, i += 10, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow(String.valueOf(pulsar.getData().getByte("charge")) + "%", 4 + fontRenderer.getStringWidth("Charge: "), i, pulsar.getData().getByte("charge") >= 100 ? 0xFFFF0000 : 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow("Energy: ", 4, i += 10, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow(String.valueOf(((ItemPulsar) pulsar.getItem()).getFluidStack(0, pulsar).getInteger("amount")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
    }

}
