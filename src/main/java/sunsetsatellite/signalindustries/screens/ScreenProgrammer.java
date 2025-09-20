package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.menus.MenuCrusher;
import sunsetsatellite.signalindustries.menus.MenuProgrammer;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityCrusher;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityProgrammer;

public class ScreenProgrammer extends ScreenFluid {

    public Player player;
    public TileEntityProgrammer tile;

    public ScreenProgrammer(ContainerInventory inv, TileEntity tile) {
        super(new MenuProgrammer(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityProgrammer) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/programmer_basic_new.png");
        switch (tile.tier){
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/programmer_basic_new.png");
                break;
            case REINFORCED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/programmer_reinforced_new.png");
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int counter;
        if(this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 9, y + 36 + 14 - counter, 176, 12 - counter, 14, counter + 2);
        }

        //todo: later
        /*
        int progressUnscaled = this.tile.getProgressScaled(100);
        if(progressUnscaled < 20){
            if(progressUnscaled < 10){
                this.drawTexturedModalRect(x + 131, y + 53 - progressUnscaled, 186, 33 - progressUnscaled, 2, progressUnscaled + 2);
            } else {
                this.drawTexturedModalRect(x + 131, y + 53 - 9, 186, 33 - 9, 2, 9 + 2);
                this.drawTexturedModalRect(x + 130 - (progressUnscaled - 10), y + 45, 185 - (progressUnscaled - 10), 25, (progressUnscaled-10) + 2, 2);
            }
        } else if(progressUnscaled > 20){
            if(progressUnscaled < 30){
                this.drawTexturedModalRect(x + 131, y + 53 - 9, 186, 33 - 9, 2, 9 + 2);
                this.drawTexturedModalRect(x + 130 - 10, y + 45, 185 - 10, 25, 10 + 2, 2);
                this.drawTexturedModalRect(x + 121, y + 39, 176, 19, progressUnscaled - 20, 2);
            }
        } else {

        }*/

        counter = Math.min(this.tile.getProgressScaled(22),22);
        this.drawTexturedModalRect(x + 121, y + 34, 176, 14, counter + 1, 20);
        if(this.tile.speedMultiplier > 1){
            this.drawStringCentered(font, this.tile.speedMultiplier+"x",x + xSize - 16,y + ySize/2 - 16,tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
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
        int progress = tile.getProgressScaled(100);
        font.drawCenteredString(I18n.getInstance().translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
        if(tile.isBurning() && tile.canProcess()){
            StringBuilder s = new StringBuilder();
            int count = tile.getProgressScaled(100) / 15;
            for(int i = 0; i < count; i++){
                s.append("#");
            }
            for(int i = 0; i < 7-count; i++){
                s.append("-");
            }
            font.drawStringWithShadow(String.format("[%s] %d%%", s,progress),38,60,color);
        }
        if(!tile.isBurning()){
            if(tile.progressTicks > 0){
                font.drawStringWithShadow("Out of energy!",38,20,0xFFFF0000);
            } else {
                font.drawStringWithShadow("Idle...",38,20,color);
            }
        } else if(!tile.canProcess()){
            font.drawStringWithShadow("Interrupted!",38,20,0xFFFF0000);
        } else {
            font.drawStringWithShadow("Working...",38,20,0xFF00FF00);
            if(progress > 10){
                font.drawStringWithShadow("Reading...",38,30,0xFF00FF00);
            }
            if(progress > 25){
                font.drawStringWithShadow("Copying...",38,40,0xFF00FF00);
            }
        }

    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 60, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 40, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if(!button.enabled) return;

        if(button == itemIoButton){
            mc.displayScreen(new ScreenItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if(button == fluidIoButton){
            mc.displayScreen(new ScreenFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }
        super.buttonClicked(button);
    }
}
