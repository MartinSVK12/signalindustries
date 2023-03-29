package sunsetsatellite.signalindustries.gui;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.GuiFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerExtractor;
import sunsetsatellite.signalindustries.tiles.TileEntityExtractor;

public class GuiExtractor extends GuiFluid {

    public String name = "Signalum Extractor";
    public EntityPlayer entityplayer;
    public TileEntityExtractor tile;


    public GuiExtractor(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerExtractor(inventoryPlayer, (TileEntityFluidItemContainer) tile),inventoryPlayer);
        this.tile = (TileEntityExtractor) tile;
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
                GuidebookPlusPlus.nameFocus = "*signalum extractor";
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

        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/extractor_prototype.png");;
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/extractor_prototype.png");
                break;
            case BASIC:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/extractor_basic.png");
                break;
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
            this.drawTexturedModalRect(i3 + 57, i4 + 36 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
        }

        i5 = this.tile.getProgressScaled(24);
        this.drawTexturedModalRect(i3 + 79, i4 + 34, 176, 14, i5 + 1, 16);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                break;
            case BASIC:
                color = 0xFFFF8080;
                break;
            case REINFORCED:
                color = 0xFFFF0000;
            case AWAKENED:
                color = 0xFFFFA500;
        }
        fontRenderer.drawCenteredString(name, 90, 6, color);
    }
    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                SignalIndustries.displayGui(entityplayer, new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, (IInventory) tile);
                break;
            default:
                break;
        }
    }

    public void initGui()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) - 80, Math.round(height / 2) - 80, 20, 20, "F"));
        super.initGui();
    }
}
