package sunsetsatellite.signalindustries.misc.powersuit;





import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.ContainerItemFluid;
import sunsetsatellite.fluidapi.api.GuiItemFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.SlotAttachment;
import sunsetsatellite.signalindustries.util.DrawUtil;

public class GuiPowerSuit extends GuiItemFluid {

    public ContainerItemFluid container;
    public SignalumPowerSuit powerSuit = null;
    public String name = "";
    public GuiPowerSuit(ContainerItemFluid containerItemFluid, InventoryPlayer invP) {
        super(containerItemFluid, invP);
        this.container = containerItemFluid;
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
        for (Slot inventorySlot : container.inventorySlots) {
            int x = inventorySlot.xDisplayPosition;
            int y = inventorySlot.yDisplayPosition;
            //drawCenteredString(fontRenderer,String.valueOf(inventorySlot.id),i+x,j+y,0xFFFFFFFF);
            if(inventorySlot instanceof SlotAttachment && ((SlotAttachment) inventorySlot).getAttachmentPoint() == AttachmentPoint.CORE_MODULE){
                drawGradientRect(i+x-4,j+y-4,i+x+20,j+y+20,0xA0808080,0xA0808080);
            } else {
                drawGradientRect(i+x,j+y,i+x+16,j+y+16,0xA0808080,0xA0808080);
            }
        }
        for (SlotFluid fluidSlot : container.fluidSlots) {
            int x = fluidSlot.xPos;
            int y = fluidSlot.yPos;
            drawGradientRect(i+x,j+y,i+x+16,j+y+16,0x40FF0000,0x40FF0000);
        }

    }

    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x,y,f);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        itemRender.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, new ItemStack(SignalIndustries.signalumPowerSuitHelmet), i+(xSize / 2)-68, j+(ySize/2)-23, 1.0F);
        itemRender.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, new ItemStack(SignalIndustries.signalumPowerSuitChestplate), i+(xSize / 2)-38, j+(ySize/2)-23, 1.0F);
        itemRender.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, new ItemStack(SignalIndustries.signalumPowerSuitLeggings), i+(xSize / 2)+22, j+(ySize/2)-23, 1.0F);
        itemRender.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, new ItemStack(SignalIndustries.signalumPowerSuitBoots), i+(xSize / 2)+52, j+(ySize/2)-23, 1.0F);
        for (int k = 0; k < container.inventorySlots.size(); k++) {
            Slot slot = container.inventorySlots.get(k);
            if(getIsMouseOverSlot(slot,x,y) && slot instanceof SlotAttachment) {
                if(slot.getStack() == null){
                    drawTooltip("Slot accepts attachments of type:\n- "+((SlotAttachment) slot).getAttachmentPoint(),x,y,8,-8,true);
                }
            }
        }
    }

    public boolean getIsMouseOverSlot(Slot slot, int i, int j) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        i -= k;
        j -= l;
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + 16 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + 16 + 1;
    }

    public Slot getSlotAtPosition(int i, int j) {
        for(int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
            Slot slot = this.inventorySlots.inventorySlots.get(k);
            if (this.getIsMouseOverSlot(slot, i, j)) {
                return slot;
            }
        }

        return null;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        drawCenteredString(fontRenderer,name,xSize/2,-16,0xFFFFFFFF);
        GL11.glDisable(3042);
        GL11.glDisable(2896);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        ContainerItemFluid container;
        GuiPowerSuit gui;
        switch (guibutton.id){
            case 0:
                container = new ContainerPowerSuit(mc.thePlayer.inventory,powerSuit.helmet);
                gui = new GuiPowerSuit(container,mc.thePlayer.inventory);
                gui.powerSuit = powerSuit;
                gui.name = "Signalum Power Suit | Helmet";
                SignalIndustries.displayGui(mc.thePlayer,gui,container,powerSuit.helmet,powerSuit.armor[3]);
                break;
            case 1:
                container = new ContainerPowerSuit(mc.thePlayer.inventory,powerSuit.chestplate);
                gui = new GuiPowerSuit(container,mc.thePlayer.inventory);
                gui.powerSuit = powerSuit;
                gui.name = "Signalum Power Suit | Core";
                SignalIndustries.displayGui(mc.thePlayer,gui,container,powerSuit.chestplate,powerSuit.armor[2]);
                break;
            case 2:
                container = new ContainerPowerSuit(mc.thePlayer.inventory,powerSuit.leggings);
                gui = new GuiPowerSuit(container,mc.thePlayer.inventory);
                gui.powerSuit = powerSuit;
                gui.name = "Signalum Power Suit | Leggings";
                SignalIndustries.displayGui(mc.thePlayer,gui,container,powerSuit.leggings,powerSuit.armor[1]);
                break;
            case 3:
                container = new ContainerPowerSuit(mc.thePlayer.inventory,powerSuit.boots);
                gui = new GuiPowerSuit(container,mc.thePlayer.inventory);
                gui.powerSuit = powerSuit;
                gui.name = "Signalum Power Suit | Boots";
                SignalIndustries.displayGui(mc.thePlayer,gui,container,powerSuit.boots,powerSuit.armor[0]);
                break;
            default:
                break;
        }
    }

    public void initGui()
    {
        GuiButton helmerButton = new GuiButton(0, (width / 2)-70, (height / 2)-25, 20, 20, "");
        GuiButton chestButton = new GuiButton(1, (width / 2)-40, (height / 2)-25, 20, 20, "");
        GuiButton legsButton = new GuiButton(2, (width / 2)+20, (height / 2)-25, 20, 20, "");
        GuiButton bootsButton = new GuiButton(3, (width / 2)+50, (height / 2)-25, 20, 20, "");

        helmerButton.enabled = container.inv.item.itemID != SignalIndustries.signalumPowerSuitHelmet.itemID;
        chestButton.enabled = container.inv.item.itemID != SignalIndustries.signalumPowerSuitChestplate.itemID;
        legsButton.enabled = container.inv.item.itemID != SignalIndustries.signalumPowerSuitLeggings.itemID;
        bootsButton.enabled = container.inv.item.itemID != SignalIndustries.signalumPowerSuitBoots.itemID;

        controlList.add(helmerButton);
        controlList.add(chestButton);
        controlList.add(legsButton);
        controlList.add(bootsButton);

        super.initGui();
    }
}
