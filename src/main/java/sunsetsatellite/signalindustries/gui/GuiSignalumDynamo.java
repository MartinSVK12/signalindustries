package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import sunsetsatellite.catalyst.CatalystEnergy;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerSignalumDynamo;
import sunsetsatellite.signalindustries.inventories.machines.TileEntitySignalumDynamo;

public class GuiSignalumDynamo extends GuiFluid {

    public String name = "Signalum Dynamo";
    public TileEntitySignalumDynamo tile;
    public EntityPlayer entityplayer;
    public GuiSignalumDynamo(InventoryPlayer inventoryPlayer, TileEntitySignalumDynamo tile) {
        super(new ContainerSignalumDynamo(inventoryPlayer,tile),inventoryPlayer);
        entityplayer = inventoryPlayer.player;
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/dynamo_basic.png");
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
            case BASIC:
                i = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/dynamo_basic.png");
                break;
            case REINFORCED:
            case AWAKENED:
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        int color;
        //1 (red, empty) -> 0.65 (green, full)
        double color_mapped = CatalystEnergy.map((float)tile.energy/(float)tile.capacity,0,1,1,0.65);
        double x_mapped = CatalystEnergy.map((float)tile.energy/(float)tile.capacity, 0,1,0,15);
        Color c = new Color();
        c.fromHSB((float) color_mapped,1.0F,1.0F);
        color = c.getAlpha() << 24 | c.getRed() << 16 | c.getBlue() << 8 | c.getGreen();
        drawRectWidthHeight(x+80,y+40, (int) x_mapped,7,color);
        GL11.glEnable(3553);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        j = (this.width - this.xSize) / 2;
        k = (this.height - this.ySize) / 2;
        int i5;
        if(this.tile.isBurning()) {
            i5 = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(j + 9, k + 39 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
        }
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        super.drawScreen(x, y, renderPartialTicks);
        I18n trans = I18n.getInstance();
        StringBuilder text = new StringBuilder();
        if(x > i+80 && x < i+94){
            if(y > j+40 && y < j+46){
                text.append(CatalystEnergy.ENERGY_NAME).append(": ").append(tile.energy).append(" ").append(CatalystEnergy.ENERGY_SUFFIX).append("/").append(tile.capacity).append(" ").append(CatalystEnergy.ENERGY_SUFFIX);
                GuiTooltip tooltip = new GuiTooltip(mc);
                tooltip.render(text.toString(),x,y,8,-8);
                //this.drawTooltip(text.toString(),x,y,8,-8,true);
            }
        }
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
                break;
            case AWAKENED:
                color = 0xFFFFA500;
                break;
        }
        fontRenderer.drawCenteredString(name, 90, 6, color);
    }

    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                SignalIndustries.displayGui(entityplayer, new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 1:
                SignalIndustries.displayGui(entityplayer, new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            default:
                break;
        }
    }


    public void init()
    {
        controlList.add(new GuiButton(0, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 80, 20, 20, "F"));
        controlList.add(new GuiButton(1, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 60, 20, 20, "I"));
        super.init();
    }

}