package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseEffectAbility;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithAbility;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.util.ApplicationType;
import sunsetsatellite.signalindustries.util.DrawUtil;
import sunsetsatellite.signalindustries.util.Tier;

public class SignalumPowerSuitClient extends SignalumPowerSuit implements IHasOverlay {


    public SignalumPowerSuitClient(PlayerLocal player) {
        super(player);
    }


    @Override
    public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {
        Minecraft mc = Minecraft.getMinecraft();
        Tier mode = Tier.BASIC;
        if (!mc.gameSettings.immersiveMode.drawOverlays()) {
            return;
        }
        DrawUtil drawUtil = new DrawUtil();
        if(!active){
            KeyBinding openSuitKey = ((IKeybinds) Minecraft.getMinecraft().gameSettings).signalIndustries$getKeyOpenSuit();
            fontRenderer.drawCenteredString(String.format("%s | Press [Shift+%s]", TextFormatting.GRAY+"OFFLINE"+TextFormatting.WHITE, openSuitKey.getKeyName()),width/2,height-64,0xFFFFFFFF);
            return;
        }
        if(status == Status.NO_ENERGY){
            KeyBinding openSuitKey = ((IKeybinds) Minecraft.getMinecraft().gameSettings).signalIndustries$getKeyOpenSuit();
            fontRenderer.drawCenteredString(String.format("%s | %s %s/%s | Press [%s] | %s C", status,TextFormatting.RED+String.format("%.2f",getEnergyPercent())+"%","("+getEnergy(), getMaxEnergy() +")"+TextFormatting.WHITE,openSuitKey.getKeyName(),String.format("%.2f",temperature)),width/2,height-64,0xFFFFFFFF);
            return;
        }

        fontRenderer.drawCenteredString(String.format("%s | %s %s/%s | %s C",status.toString(),TextFormatting.RED+String.format("%.2f",getEnergyPercent())+"%","("+getEnergy(), getMaxEnergy() +")"+TextFormatting.WHITE,String.format("%.2f",temperature)),width/2,height-64,0xFFFFFFFF);

        int color = mode.getColor(0x40);//0x40808080;
        int color2 = mode.getColor();//0xFF808080;

        //top
        drawUtil.drawGradientRect(0,0,width,16,color,color);
        drawUtil.drawGradientRect(0,16,width/2-100,20,color,color2);
        drawUtil.drawGradientRect(width/2+100,16,width,20,color,color2);
        drawUtil.drawGradientRect(width/2-100,36,width/2+100,40,color,color2);
        drawUtil.drawGradientRect(width/2-102,20,width/2-100,40,color2,color2);
        drawUtil.drawGradientRect(width/2+100,20,width/2+102,40,color2,color2);

        drawUtil.drawGradientRect(width/2-100,16,width/2+100,36,color,color);

        if(module == null){
            fontRenderer.drawCenteredString(String.format("%s","No module."),width/2,25,color2);
        } else {
            if(module.contents[selectedApplicationSlot] != null){
                IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
                if(app.getType() == ApplicationType.ABILITY){
                    SuitBaseAbility selectedAbility = ((ItemWithAbility)module.contents[selectedApplicationSlot].getItem()).getApplication();
                    I18n t = I18n.getInstance();
                    if(selectedAbility instanceof SuitBaseEffectAbility){
                        if(effectTimes.containsKey(selectedAbility)){
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+String.valueOf(effectTimes.get(selectedAbility)/20)+"s"),width/2,25,color2);
                        } else if(cooldowns.containsKey(selectedAbility)){
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.RED+String.valueOf(cooldowns.get(selectedAbility)/20)+"s"),width/2,25,color2);
                        } else {
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+"READY"),width/2,25,color2);
                        }
                    } else {
                        if(cooldowns.containsKey(selectedAbility)){
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.RED+String.valueOf(cooldowns.get(selectedAbility)/20)+"s"),width/2,25,color2);
                        } else {
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+"READY"),width/2,25,color2);
                        }
                    }
                } else if (app.getType() == ApplicationType.UTILITY) {
                    ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                    fontRenderer.drawCenteredString(String.format("%s%s%s",item.getTier().getTextColor(),item.getTranslatedName(module.contents[selectedApplicationSlot]),TextFormatting.WHITE),width/2,25,color2);
                }
            } else {
                fontRenderer.drawCenteredString(String.format("%s","No application selected."),width/2,25,color2);
            }
        }

        //bottom
        drawUtil.drawGradientRect(0,height-20,width,height,color,color);
        drawUtil.drawGradientRect(width/2-170,height-24,width/2-100,height-20,color2,color);
        drawUtil.drawGradientRect(width/2+100,height-24,width,height-20,color2,color);
        drawUtil.drawGradientRect(width/2-100,height-44,width/2+100,height-40,color2,color);
        drawUtil.drawGradientRect(width/2-102,height-44,width/2-100,height-24,color2,color2);
        drawUtil.drawGradientRect(width/2+100,height-44,width/2+102,height-24,color2,color2);

        drawUtil.drawGradientRect(width/2-100,height-40,width/2+100,height-20,color,color);

        //armor display
        drawUtil.drawGradientRect(0,height-74,width/2-170,height-70,color2,color);
        drawUtil.drawGradientRect(width/2-168,height-24,width/2-170,height-74,color2,color2);
        drawUtil.drawGradientRect(0,height-74,width/2-170,height-20,color,color);

        //ability hotbar
        if(module == null){
            GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            mc.textureManager.loadTexture("/gui/gui.png").bind();
            int x = width / 2 - 91;
            int y = 0;
            drawUtil.drawTexturedModalRect(x, y, 0, 0, 182, 22);
            //selected ability
            //GL11.glColor4f(1F, 0F, 0F, 1.0F);
            //drawUtil.drawTexturedModalRect(x - 1 + selectedAbilitySlot % 9 * 20, y - 1, 0, 22, 24, 22 + 2);
            //GL11.glBindTexture(3553, mc.renderEngine.getTexture("/gui/icons.png"));
        } else {
            //Tier mode = getModuleMode();
            Color c = new Color().setARGB(mode.getColor());
            GL11.glColor4f((float) c.getRed() /255, (float) c.getGreen() /255, (float) c.getBlue() /255, (float) c.getAlpha() /255);
            //GL11.glColor4b((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue(), (byte) c.getAlpha());
            mc.textureManager.loadTexture("/gui/gui.png").bind();
            int x = width / 2 - 91;
            int y = 0;
            drawUtil.drawTexturedModalRect(x, y, 0, 0, 182, 22);
            int i = x;
            int j = y;
            for (int i1 = 0; i1 < module.contents.length; i1++) {
                ItemModel model = ItemModelDispatcher.getInstance().getDispatch(module.contents[i1]);
                model.renderItemIntoGui(Tessellator.instance, fontRenderer, mc.textureManager, module.contents[i1], i+3, j+3, 1.0F);
                GL11.glDisable(3042);
                GL11.glDisable(2896);
                i+=20;
            }
            //selected ability
            mc.textureManager.loadTexture("/gui/gui.png").bind();
            GL11.glColor4f(1F, 1F, 1F, 1.0F);
            drawUtil.drawTexturedModalRect(x - 1 + selectedApplicationSlot % 9 * 20, y - 1, 0, 22, 24, 22 + 2);
            mc.textureManager.loadTexture("/gui/icons.png").bind();
        }


        //draw armor pieces and attachments
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = this.player.inventory.armorInventory[i];
            if (stack != null) {
                int x = 2 ;
                int y = height - 64 + ((3-i) * 16);
                ItemModel model = ItemModelDispatcher.getInstance().getDispatch(stack);
                model.renderItemIntoGui(Tessellator.instance,fontRenderer, mc.textureManager, stack, x, y, 1.0F);
                GL11.glDisable(3042);
                GL11.glDisable(2896);
                InventoryPowerSuit pieceInv = getArmorPiece(i);
                if (!pieceInv.isEmpty()) {
                    int k = 16;
                    for (ItemStack content : pieceInv.contents) {
                        if(content != null){
                            model = ItemModelDispatcher.getInstance().getDispatch(content);
                            model.renderItemIntoGui(Tessellator.instance,fontRenderer, mc.textureManager, content, x+k, y, 1.0F);
                            k+=16;
                        }
                    }
                }
            }
        }
        GL11.glDisable(3042);
        GL11.glDisable(2896);


        //render attachment info
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet,chestplate,leggings,boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if(content != null){
                    if(content.getItem() instanceof IHasOverlay){
                        ((IHasOverlay) content.getItem()).renderOverlay(content, this, guiIngame,player,height,width,mouseX,mouseY,fontRenderer,itemRenderer);
                    }
                }
            }
        }

    }

    @Override
    public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {

    }
}
