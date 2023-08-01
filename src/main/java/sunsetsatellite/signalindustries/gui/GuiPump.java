package sunsetsatellite.signalindustries.gui;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.GuiFluid;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerPump;
import sunsetsatellite.signalindustries.inventories.TileEntityPump;
import sunsetsatellite.signalindustries.inventories.TileEntityTieredMachine;

public class GuiPump extends GuiFluid {

    public String name = "Pump";
    public EntityPlayer entityplayer;
    public TileEntityPump tile;


    public GuiPump(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerPump(inventoryPlayer, (TileEntityTieredMachine) tile),inventoryPlayer);
        this.tile = (TileEntityPump) tile;
        this.entityplayer = inventoryPlayer.player;
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        super.drawScreen(x, y, renderPartialTicks);
        StringTranslate trans = StringTranslate.getInstance();
        StringBuilder text = new StringBuilder();
        if(x > i+80 && x < i+94){
            if(y > j+40 && y < j+46){
                text.append("View Recipes");
                this.drawTooltip(text.toString(),x,y,8,-8,true);
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        if(x > i+80 && x < i+94) {
            if (y > j + 40 && y < j + 46) {
                StringTranslate translator = StringTranslate.getInstance();
                String name = translator.translateKey(tile.getBlockType().getBlockName(0)+".name");
                GuidebookPlusPlus.nameFocus = ">"+ name;
                if(entityplayer instanceof EntityPlayerSP){
                    ((EntityPlayerSP)entityplayer).displayGUIGuidebook();
                } else if (entityplayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)entityplayer).displayGUIGuidebook();
                }
            }
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {

        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/prototype_pump.png");
        switch (tile.tier){
            case PROTOTYPE:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/prototype_pump.png");
                break;
            case BASIC:
            case REINFORCED:
            case AWAKENED:
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
        int i5;
        if(this.tile.isBurning()) {
            i5 = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(i3 + 62, i4 + 36 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
        }

        i5 = this.tile.getProgressScaled(15);
        this.drawTexturedModalRect(i3 + 82, i4 + 35 + 15 - i5, 176, 29 - i5, 12, i5 + 2);
        if(this.tile.speedMultiplier > tile.tier.ordinal()+1){
            this.drawCenteredString(fontRenderer, "2x",i3 + xSize - 16,i4 + ySize/2 - 16,0xFFFF00FF);
        }
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (tile.tier){
            case PROTOTYPE:
                break;
            case BASIC:
                color = 0xFFFF8080;
                break;
            case REINFORCED:
                color = 0xFFFF0000;
                break;
            case AWAKENED:
                color = 0xFFFFA500;
                break;
        }
        fontRenderer.drawCenteredString(name, 90, 6, color);
    }
    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            SignalIndustries.displayGui(entityplayer, new GuiFluidIOConfig(entityplayer, inventorySlots, tile, this), inventorySlots, tile, tile.xCoord, tile.yCoord, tile.zCoord);
                /*case 1:
                SignalIndustries.displayGui(entityplayer, new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.xCoord,tile.yCoord,tile.zCoord);
                break;*/
        }
    }

    public void initGui()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) + 60, Math.round(height / 2) - 80, 20, 20, "F"));
        //controlList.add(new GuiButton(1, Math.round(width / 2) + 60, Math.round(height / 2) - 60, 20, 20, "I"));
        super.initGui();
    }
}