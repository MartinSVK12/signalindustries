package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageOpenSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.DrawUtil;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.SlotAttachment;
import turniplabs.halplibe.helper.network.NetworkHandler;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ScreenPowerSuit extends ScreenFluid {

    public ItemStack armorPiece;
    public ContainerInventory inv;
    public int slotIndex;

    public ScreenPowerSuit(ContainerInventory inventoryPlayer, int slotIndex, boolean isArmor) {
        super(new MenuPowerSuit(inventoryPlayer, slotIndex, isArmor));
        this.armorPiece = inventoryPlayer.armorItemInSlot(slotIndex);
        this.inv = inventoryPlayer;
        this.slotIndex = slotIndex;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        super.drawGuiContainerBackgroundLayer(f1);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        DrawUtil drawUtil = new DrawUtil();
        drawGradientRect(i-4,j-4,i+xSize+4,j,0xFFFFFFFF,0x40FFFFFF);
        drawGradientRect(i-4,j+ySize,i+xSize+4,j+ySize+4,0x40FFFFFF,0xFFFFFFFF);
        drawUtil.drawGradientRect(i-4,j-4,i,j+ySize+4,0x40FFFFFF,0xFFFFFFFF,0xFFFFFFFF,0x40FFFFFF);
        drawUtil.drawGradientRect(i+xSize,j-4,i+xSize+4,j+ySize+4,0xFFFFFFFF,0x40FFFFFF,0x40FFFFFF,0xFFFFFFFF);
        drawGradientRect(i,j,i+xSize,j+ySize,0x40FFFFFF,0x40FFFFFF);
        //drawUtil.drawGradientRect(i,j,i+xSize,j+ySize,0xFFFFFFFF,0xFF00FF00,0xFF00FF00,0xFFFFFFFF);
        for (Slot inventorySlot : inventorySlots.slots) {
            int x = inventorySlot.x;
            int y = inventorySlot.y;
            //drawStringCentered(fontRenderer,String.valueOf(inventorySlot.id),i+x,j+y,0xFFFFFFFF);
            if(inventorySlot instanceof SlotAttachment && ((SlotAttachment) inventorySlot).getAttachmentPoint() == AttachmentPoint.CORE_MODULE){
                drawGradientRect(i+x-4,j+y-4,i+x+20,j+y+20,0xA0808080,0xA0808080);
            } else {
                drawGradientRect(i+x,j+y,i+x+16,j+y+16,0xA0808080,0xA0808080);
            }
        }
        for (SlotFluid fluidSlot : fluidSlots.fluidSlots) {
            int x = fluidSlot.x;
            int y = fluidSlot.y;
            drawGradientRect(i+x,j+y,i+x+16,j+y+16,0x40FF0000,0x40FF0000);
        }

    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        String name = I18n.getInstance().translateNameKey(fluidSlots.itemInventory.getNameTranslationKey());
        drawStringCentered(font,name,xSize/2,-16,0xFFFFFFFF);
        GL11.glDisable(3042);
        GL11.glDisable(2896);
    }

    @Override
    public void render(int x, int y, float f) {
        super.render(x,y,f);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        ItemStack helmet = new ItemStack(SIItems.signalumPowerSuitHelmet);
        ItemStack chest = new ItemStack(SIItems.signalumPowerSuitChestplate);
        ItemStack leg = new ItemStack(SIItems.signalumPowerSuitLeggings);
        ItemStack boots = new ItemStack(SIItems.signalumPowerSuitBoots);
        ItemModelDispatcher.getInstance().getDispatch(helmet).renderItemIntoGui(Tessellator.instance,font, this.mc.textureManager, helmet,i+(xSize / 2)-68, j+(ySize/2)-23, 1.0F);
        ItemModelDispatcher.getInstance().getDispatch(chest).renderItemIntoGui(Tessellator.instance,font, this.mc.textureManager, chest,i+(xSize / 2)-38, j+(ySize/2)-23, 1.0F);
        ItemModelDispatcher.getInstance().getDispatch(leg).renderItemIntoGui(Tessellator.instance,font, this.mc.textureManager, leg,i+(xSize / 2)+22, j+(ySize/2)-23, 1.0F);
        ItemModelDispatcher.getInstance().getDispatch(boots).renderItemIntoGui(Tessellator.instance,font, this.mc.textureManager, boots,i+(xSize / 2)+52, j+(ySize/2)-23, 1.0F);
        for (int k = 0; k < inventorySlots.slots.size(); k++) {
            Slot slot = inventorySlots.slots.get(k);
            if(getIsMouseOverSlot(slot,x,y) && slot instanceof SlotAttachment) {
                if(slot.getItemStack() == null){
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    TooltipElement tooltip = new TooltipElement(mc);
                    tooltip.render("Slot accepts attachments of type:\n- "+((SlotAttachment) slot).getAttachmentPoint(),x,y,8,-8);
                }
            }
        }

        SignalumPowerSuit suit = ((IPlayerPowerSuit<SignalumPowerSuit>) inv.player).getPowerSuit();
        if(suit != null){
            for (int k = 0; k < fluidSlots.fluidSlots.size(); k++) {
                SlotFluid slot = fluidSlots.fluidSlots.get(k);
                slot.putStack(suit.getArmorPiece(slotIndex).fluidContents[k]);
            }
        }
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        super.buttonClicked(button);
        if (!button.enabled) {
            return;
        }
        if (inv.player.world != null && inv.player.world.isClientSide) {
            switch (button.id){
                case 0:
                    mc.thePlayer.closeScreen();
                    NetworkHandler.sendToServer(new NetworkMessageOpenSuit(3));
                    break;
                case 1:
                    mc.thePlayer.closeScreen();
                    NetworkHandler.sendToServer(new NetworkMessageOpenSuit(2));
                    break;
                case 2:
                    mc.thePlayer.closeScreen();
                    NetworkHandler.sendToServer(new NetworkMessageOpenSuit(1));
                    break;
                case 3:
                    mc.thePlayer.closeScreen();
                    NetworkHandler.sendToServer(new NetworkMessageOpenSuit(0));
                    break;
            }
        } else {
            switch (button.id){
                case 0:
                    mc.thePlayer.closeScreen();
                    Catalyst.displayGui(inv.player, new InventoryPowerSuit(inv.armorItemInSlot(3)),3,true,key("gui/power_suit"));
                    break;
                case 1:
                    mc.thePlayer.closeScreen();
                    Catalyst.displayGui(inv.player, new InventoryPowerSuit(inv.armorItemInSlot(2)),2,true,key("gui/power_suit"));
                    break;
                case 2:
                    mc.thePlayer.closeScreen();
                    Catalyst.displayGui(inv.player, new InventoryPowerSuit(inv.armorItemInSlot(1)),1,true,key("gui/power_suit"));
                    break;
                case 3:
                    mc.thePlayer.closeScreen();
                    Catalyst.displayGui(inv.player, new InventoryPowerSuit(inv.armorItemInSlot(0)),0,true,key("gui/power_suit"));
                    break;
            }
        }

    }

    public void init()
    {
        ButtonElement helmetButton = new ButtonElement(0, (width / 2)-70, (height / 2)-25, 20, 20, "");
        ButtonElement chestButton = new ButtonElement(1, (width / 2)-40, (height / 2)-25, 20, 20, "");
        ButtonElement legsButton = new ButtonElement(2, (width / 2)+20, (height / 2)-25, 20, 20, "");
        ButtonElement bootsButton = new ButtonElement(3, (width / 2)+50, (height / 2)-25, 20, 20, "");

        helmetButton.enabled = armorPiece.itemID != SIItems.signalumPowerSuitHelmet.id;
        chestButton.enabled = armorPiece.itemID != SIItems.signalumPowerSuitChestplate.id;
        legsButton.enabled = armorPiece.itemID != SIItems.signalumPowerSuitLeggings.id;
        bootsButton.enabled = armorPiece.itemID != SIItems.signalumPowerSuitBoots.id;

        buttons.add(helmetButton);
        buttons.add(chestButton);
        buttons.add(legsButton);
        buttons.add(bootsButton);

        super.init();
    }

    @Override
    public void removed() {
        super.removed();
        InventorySerializer.saveInvToNBT(armorPiece,fluidSlots.itemInventory);
    }
}
