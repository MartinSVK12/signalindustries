package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import sunsetsatellite.signalindustries.util.GuiRenderFakeItem;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerAssembler;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAssembler;

public class GuiAssembler extends GuiFluid {
    public String name = "Assembler";
    public EntityPlayer entityplayer;
    public TileEntityAssembler tile;


    public GuiAssembler(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerAssembler(inventoryPlayer, (TileEntityFluidItemContainer) tile),inventoryPlayer);
        this.tile = (TileEntityAssembler) tile;
        this.entityplayer = inventoryPlayer.player;
        ySize = 215;
    }

    @Override
    public void drawScreen(int i1, int i2, float f3) {
        super.drawScreen(i1, i2, f3);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        Slot slot = this.inventorySlots.inventorySlots.get(0);
        GuiRenderFakeItem guiRenderFakeItem = new GuiRenderFakeItem(mc);
        if(tile.recipe != null && slot.getStack() == null){
            guiRenderFakeItem.render(tile.recipe.getOutput(),i+slot.xDisplayPosition,j+slot.yDisplayPosition);
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        if(x > i+80 && x < i+94) {
            if (y > j + 40 && y < j + 46) {
                I18n translator = I18n.getInstance();
                String name = translator.translateKey(tile.getBlockType().getLanguageKey(0)+".name");
                ////GuidebookPlusPlus.nameFocus = ">"+ name;
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
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/basic_assembler_gui.png");
        /*switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/generic_prototype_machine_double.png");
                break;
            case BASIC:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/generic_basic_machine_double.png");
                break;
            case REINFORCED:
            case AWAKENED:
                break;
        }*/
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
        int i5;
        if(this.tile.isBurning()) {
            i5 = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(i3 + 153, i4 + 18 + 12 - i5, 176, 28 - i5, 14, i5 + 2);
        }

        i5 = this.tile.getProgressScaled(24);
        this.drawTexturedModalRect(i3 + 85, i4 + 35, 176, 0, i5 + 1, 16);
        if(this.tile.speedMultiplier > 1){
            this.drawString(fontRenderer, this.tile.speedMultiplier+"x",i3 + 6,i4 + 6,tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }

        //this.drawString(fontRenderer,tile.recipe == null ? "null" : tile.recipe.toString(),i3+2,i4+120,0xFFFF0000);
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
                SignalIndustries.displayGui(entityplayer, () -> new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 1:
                SignalIndustries.displayGui(entityplayer, () -> new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            default:
                break;
        }
    }

    public void init()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) + 40, Math.round(height / 2) - 50, 20, 20, "F"));
        controlList.add(new GuiButton(1, Math.round(width / 2) + 60, Math.round(height / 2) - 50, 20, 20, "I"));
        super.init();
    }
}
