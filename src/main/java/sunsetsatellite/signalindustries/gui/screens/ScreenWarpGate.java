package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.gui.menus.MenuWarpGate;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityWarpGate;

public class ScreenWarpGate extends ScreenFluid {

    public Player player;
    public TileEntityWarpGate tile;

    public ScreenWarpGate(ContainerInventory inv, TileEntity tile) {
        super(new MenuWarpGate(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityWarpGate) tile;
        this.player = inv.player;
        this.ySize = 192;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/warp_gate.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (tile.tier) {
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
		drawStringCenteredShadow(fontRenderer, Catalyst.translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
        if (tile.itemContents[0] != null && tile.itemContents[0].getItem() instanceof ItemWarpOrb) {
			drawStringCenteredShadow(fontRenderer, ((ItemWarpOrb) tile.itemContents[0].getItem()).getLocationString(tile.itemContents[0]), 90, 20, 0xFFEEEEEE);
        } else {
			drawStringCenteredShadow(fontRenderer, "No location", 90, 20, 0xFF808080);
        }
        switch (tile.state) {
            case IDLE:
                drawStringCenteredShadow(fontRenderer, "Idle", 89, 70, 0xFFEEEEEE);
                break;
            case CHARGING:
                drawStringCenteredShadow(fontRenderer, "Charging...", 89, 70, 0xFFFF8000);
                break;
            case CONNECTED_ONE_WAY:
                drawStringCenteredShadow(fontRenderer, "Active! (One-way)", 89, 70, 0xFF00FF00);
                break;
            case CONNECTED_TWO_WAY:
                drawStringCenteredShadow(fontRenderer, "Active! (Two-way)", 89, 70, 0xFF00FF00);
                break;
            case STABILIZATION_FAILURE:
                drawStringCenteredShadow(fontRenderer, "/!\\ STABILIZATION FAILURE /!\\", 89, 70, 0xFFFF0000);
                break;
            case POWER_FAILURE:
                drawStringCenteredShadow(fontRenderer, "/!\\ POWER FAILURE /!\\", 89, 70, 0xFFFF0000);
                break;
        }
    }
}
