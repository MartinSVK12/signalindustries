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
import sunsetsatellite.signalindustries.menus.MenuFilter;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageExternalIOLinkBreak;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageFilterConfig;
import sunsetsatellite.signalindustries.tiles.TileEntityFilter;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenFilter extends ScreenFluid {

    public Player player;
    public TileEntityFilter tile;

    public ScreenFilter(ContainerInventory inv, TileEntity tile) {
        super(new MenuFilter(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityFilter) tile;
        this.player = inv.player;
        this.ySize = 233;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/filter.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        int color = 0x404040;
        String s = I18n.getInstance().translateNameKey(tile.getNameTranslationKey());
        int stringWidth = font.getStringWidth(s);
        font.drawString(s, 90 - stringWidth/2, 6, color);
    }

    public ButtonElement defaultSide;
    public ButtonElement ignoreMeta;

    @Override
    public void init() {
        int w = (this.width - this.xSize) / 2;
        int h = (this.height - this.ySize) / 2;
        ButtonElement defaultSide = new ButtonElement(0,w+8+22,h+128,120,20,"Default: "+(tile.defaultSide.ordinal()+1)+" ("+tile.defaultSide.name()+")");
        buttons.add(defaultSide);
        ButtonElement ignoreMeta = new ButtonElement(1,w+8,h+128,20,20, tile.ignoreMeta ? "!M" : "M");
        buttons.add(ignoreMeta);
        this.ignoreMeta = ignoreMeta;
        this.defaultSide = defaultSide;
        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if(!button.enabled) return;

        if(button == defaultSide){
            int ord = tile.defaultSide.ordinal();
            if(ord++ >= TileEntityFilter.FilterSide.values().length-1){
                ord = 0;
            }
            tile.defaultSide = TileEntityFilter.FilterSide.values()[ord];
            button.displayString = "Default: " + (ord+1) + " ("+tile.defaultSide.name()+")";
            if(EnvironmentHelper.isClientWorld()){
                NetworkHandler.sendToServer(new NetworkMessageFilterConfig(tile.getPosition(), tile.getClass(), tile.defaultSide, tile.ignoreMeta));
            }
        } else if(button == ignoreMeta){
            tile.ignoreMeta = !tile.ignoreMeta;
            button.displayString = tile.ignoreMeta ? "!M" : "M";
            if(EnvironmentHelper.isClientWorld()){
                NetworkHandler.sendToServer(new NetworkMessageFilterConfig(tile.getPosition(), tile.getClass(), tile.defaultSide, tile.ignoreMeta));
            }
        }
        super.buttonClicked(button);
    }
}
