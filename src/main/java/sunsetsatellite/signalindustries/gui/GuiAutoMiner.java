package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.GuiFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerAutoMiner;
import sunsetsatellite.signalindustries.inventories.TileEntityAutoMiner;

public class GuiAutoMiner extends GuiFluid {

    public String name = "Automatic Miner";
    public EntityPlayer entityplayer;
    public TileEntityAutoMiner tile;


    public GuiAutoMiner(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerAutoMiner(inventoryPlayer, (TileEntityFluidItemContainer) tile),inventoryPlayer);
        this.tile = (TileEntityAutoMiner) tile;
        this.entityplayer = inventoryPlayer.player;
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        super.drawScreen(x, y, renderPartialTicks);
        I18n trans = I18n.getInstance();
        StringBuilder text = new StringBuilder();
        /*if(x > i+80 && x < i+94){
            if(y > j+43 && y < j+49){
                text.append("View Recipes");
                GuiTooltip tooltip = new GuiTooltip(mc);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_CULL_FACE);
                tooltip.render(text.toString(),x,y,8,-8);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
        }*/
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        /*int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        if(x > i+80 && x < i+94) {
            if (y > j + 43 && y < j + 49) {
                I18n translator = I18n.getInstance();
                String name = translator.translateKey(tile.getBlockType().getLanguageKey(0)+".name");
                GuidebookPlusPlus.nameFocus = ">"+ name;
                if(entityplayer instanceof EntityPlayerSP){
                    ((EntityPlayerSP)entityplayer).displayGUIGuidebook();
                } else if (entityplayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)entityplayer).displayGUIGuidebook();
                }
            }
        }*/
        super.mouseClicked(x, y, button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/autominer.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
        int i5;
        if(this.tile.isBurning()) {
            i5 = 12; //this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(i3 + 9, i4 + 36 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
        }

        /*i5 = this.tile.getProgressScaled(24);
        this.drawTexturedModalRect(i3 + 79, i4 + 35, 176, 14, i5 + 1, 18);*/
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
        fontRenderer.drawCenteredString(name, 90, 4, color);
        fontRenderer.drawStringWithShadow("X: "+(tile.current.x-tile.xCoord),38,20,0xFFFF0000);
        fontRenderer.drawStringWithShadow("Y: "+(tile.current.y-tile.yCoord),38,32,0xFF4080FF);
        fontRenderer.drawStringWithShadow("Z: "+(tile.current.z-tile.zCoord),38,44,0xFF00FF00);
        fontRenderer.drawStringWithShadow("S: "+(tile.progressMaxTicks/tile.speedMultiplier)+"t",80,44,0xFFFF8000);
        fontRenderer.drawStringWithShadow("C: "+(tile.cost),80,32,0xFF800000);
        //fontRenderer.drawString(String.format("X: %d Y: %d Z: %d",tile.current.x,tile.current.y,tile.current.z),38,20,0xFFFFFFFF);
    }

    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                SignalIndustries.displayGui(entityplayer, new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.xCoord,tile.yCoord,tile.zCoord);
                break;
            case 1:
                SignalIndustries.displayGui(entityplayer, new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.xCoord,tile.yCoord,tile.zCoord);
                break;
            case 2:
                if(tile.workTimer.isPaused()){
                    tile.workTimer.unpause();
                } else {
                    tile.workTimer.pause();
                }
                guibutton.displayString = tile.workTimer.isPaused() ? "OFF" : "ON";
            default:
                break;
        }
    }

    public void initGui()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) + 60, Math.round(height / 2) - 80, 20, 20, "F"));
        controlList.add(new GuiButton(1, Math.round(width / 2) + 60, Math.round(height / 2) - 24, 20, 20, "I"));
        controlList.add(new GuiButton(2, Math.round(width / 2) - 81, Math.round(height / 2) - 80, 20, 20, tile.workTimer.isPaused() ? "OFF" : "ON"));
        super.initGui();
    }
}
