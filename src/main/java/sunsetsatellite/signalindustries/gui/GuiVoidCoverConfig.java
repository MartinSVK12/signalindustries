package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.popup.GuiPopup;
import net.minecraft.client.gui.popup.PopupBuilder;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.covers.VoidCover;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;


public class GuiVoidCoverConfig extends GuiScreen {

    public EntityPlayer entityplayer;
    public TileEntityTieredMachineBase tile;
    public int xSize = 176;
    public int ySize = 90;
    public VoidCover cover;
    private static final ItemEntityRenderer itemRenderer = new ItemEntityRenderer();
    public GuiVoidCoverConfig(EntityPlayer player, TileEntity tile, VoidCover voidCover) {
        super();
        this.tile = (TileEntityTieredMachineBase) tile;
        this.entityplayer = player;
        this.cover = voidCover;
    }

    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x,y,button);
        if (button == 1) {
            for (int l = 0; l < this.controlList.size(); ++l) {
                GuiButton guibutton = this.controlList.get(l);
                if (guibutton.mouseClicked(this.mc, x, y)) {
                    this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
                    action2Performed(guibutton);
                }
            }
        }
    }

    private void action2Performed(GuiButton guibutton) {
        if(tile != null){
            switch (guibutton.id){
                case 1: {
                    if(cover.voidingItemSlot > -2){
                        cover.voidingItemSlot--;
                        if(cover.voidingItemSlot == -2){
                            GuiPopup popup = new PopupBuilder(this, 246)
                                    .withLabel("signalindustries.warning")
                                    .withMessageBox("warning", 128, "Setting a voiding slot to \"*\" will\n" + TextFormatting.RED + ">> REMOVE EVERY ITEM <<\n" + TextFormatting.WHITE + "in the inventory of the machine this cover\nis attached to.\n\nClick outside this popup to continue.", 44)
                                    .closeOnClickOut(0)
                                    .closeOnEnter(0)
                                    .closeOnEsc(0)
                                    .build();
                            Minecraft.getMinecraft(this).displayGuiScreen(popup);
                            cover.active = false;
                        }
                    }
                    String voidSlotItemStr;
                    if(cover.voidingItemSlot == -2){
                        voidSlotItemStr = "*";
                    } else if (cover.voidingItemSlot == -1) {
                        voidSlotItemStr = "X";
                    } else {
                        voidSlotItemStr = String.valueOf(cover.voidingItemSlot);
                    }
                    guibutton.displayString = voidSlotItemStr;
                    break;
                }
                case 2: {
                    if(cover.voidingFluidSlot > -2){
                        cover.voidingFluidSlot--;
                        if(cover.voidingFluidSlot == -2){
                            GuiPopup popup = new PopupBuilder(this, 246)
                                    .withLabel("signalindustries.warning")
                                    .withMessageBox("warning", 128, "Setting a voiding slot to \"*\" will\n" + TextFormatting.RED + ">> REMOVE EVERY FLUID <<\n" + TextFormatting.WHITE + "in the inventory of the machine this cover\nis attached to.\n\nClick outside this popup to continue.", 44)
                                    .closeOnClickOut(0)
                                    .closeOnEnter(0)
                                    .closeOnEsc(0)
                                    .build();
                            Minecraft.getMinecraft(this).displayGuiScreen(popup);
                            cover.active = false;
                        }
                    }
                    String voidSlotFluidStr;
                    if(cover.voidingFluidSlot == -2){
                        voidSlotFluidStr = "*";
                    } else if (cover.voidingFluidSlot == -1) {
                        voidSlotFluidStr = "X";
                    } else {
                        voidSlotFluidStr = String.valueOf(cover.voidingFluidSlot);
                    }
                    guibutton.displayString = voidSlotFluidStr;
                    break;
                }
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/assets/signalindustries/gui/config.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
        this.drawDefaultBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(renderPartialTicks);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.enableInventoryLight();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)centerX, (float)centerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        GL11.glDisable(32826);
        Lighting.disable();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, renderPartialTicks);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2929);
    }

    @Override
    public void init() {
        String voidSlotItemStr;
        String voidSlotFluidStr;
        if(cover.voidingItemSlot == -2){
            voidSlotItemStr = "*";
        } else if (cover.voidingItemSlot == -1) {
            voidSlotItemStr = "X";
        } else {
            voidSlotItemStr = String.valueOf(cover.voidingItemSlot);
        }
        if(cover.voidingFluidSlot == -2){
            voidSlotFluidStr = "*";
        } else if (cover.voidingFluidSlot == -1) {
            voidSlotFluidStr = "X";
        } else {
            voidSlotFluidStr = String.valueOf(cover.voidingFluidSlot);
        }
        controlList.add(new GuiButton(1, Math.round((float) width / 2) - 40, (height / 2) - 10, 20, 20, voidSlotItemStr));
        controlList.add(new GuiButton(2, Math.round((float) width / 2) + 20, (height / 2) - 10, 20, 20, voidSlotFluidStr));

        controlList.add(new GuiButton(0, (width / 2) - 15, (height / 2) - 10, 30, 20, cover.active ? "ON" : "OFF"));

        super.init();
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        if(tile != null){
            switch (guibutton.id){
                case 0: {
                    cover.active = !cover.active;
                    guibutton.displayString = cover.active ? "ON" : "OFF";
                    break;
                }
                case 1: {
                    int max = tile.getSizeInventory()-1;
                    if(cover.voidingItemSlot < max){
                        cover.voidingItemSlot++;
                    }
                    String voidSlotItemStr;
                    if(cover.voidingItemSlot == -2){
                        voidSlotItemStr = "*";
                    } else if (cover.voidingItemSlot == -1) {
                        voidSlotItemStr = "X";
                    } else {
                        voidSlotItemStr = String.valueOf(cover.voidingItemSlot);
                    }
                    guibutton.displayString = voidSlotItemStr;
                    break;
                }
                case 2: {
                    int max = tile.getFluidInventorySize()-1;
                    if(cover.voidingFluidSlot < max){
                        cover.voidingFluidSlot++;
                    }
                    String voidSlotFluidStr;
                    if(cover.voidingFluidSlot == -2){
                        voidSlotFluidStr = "*";
                    } else if (cover.voidingFluidSlot == -1) {
                        voidSlotFluidStr = "X";
                    } else {
                        voidSlotFluidStr = String.valueOf(cover.voidingFluidSlot);
                    }
                    guibutton.displayString = voidSlotFluidStr;
                    break;
                }
            }
        }
        super.buttonPressed(guibutton);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Configure: Voiding", 45, 6, 0xFF404040);
        fontRenderer.drawString("Item Slot", xSize/2 - 50, 23, 0xFF404040);
        fontRenderer.drawString("Fluid Slot",  xSize/2 + 10, 23, 0xFF404040);
        fontRenderer.drawString("Activate",  xSize/2-20, 60, 0xFF404040);
    }
}
