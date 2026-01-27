package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.menus.MenuSIFluidTank;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEnergyCell;
import sunsetsatellite.signalindustries.util.Tier;

public class ScreenEnergyCell extends ScreenFluid {

    public Player player;
    public TileEntityEnergyCell tile;

    public ScreenEnergyCell(ContainerInventory inv, TileEntity tile) {
        super(new MenuSIFluidTank(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityEnergyCell) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        font.drawString(I18n.getInstance().translateNameKey(tile.getNameTranslationKey()), 64, 6, 0xFF404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture t = mc.textureManager.loadTexture("/assets/catalyst-fluids/gui/tank_gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(t);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton == fluidIoButton) {
            mc.displayScreen(new ScreenVisualFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }
        if (tile.getTier() == Tier.INFINITE && guibutton.id == 1) {
            tile.isInfiniteSource = !tile.isInfiniteSource;
            guibutton.displayString = tile.isInfiniteSource ? "INF" : "VOID";
        }
    }

    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 80, 20, 20, "F");
        buttons.add(fluidIo);
        fluidIoButton = fluidIo;

        if (tile.getTier() == Tier.INFINITE) {
            buttons.add(new ButtonElement(1, Math.round((float) width / 2) - 80, Math.round((float) height / 2) - 30, 20, 20, tile.isInfiniteSource ? "INF" : "VOID"));
        }

        super.init();
    }
}
