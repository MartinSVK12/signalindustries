package sunsetsatellite.signalindustries.screens;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.covers.RedstoneCover;
import sunsetsatellite.signalindustries.covers.SwitchCover;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenSwitchCoverConfig extends Screen {
    public int xSize = 176;
    public int ySize = 90;

    public SwitchCover cover;
    public Player player;
    public TileEntityTieredMachineBase tile;

    public ScreenSwitchCoverConfig(ContainerInventory playerInv, TileEntity tile, CompoundTag data) {
        this.player = playerInv.player;
        this.tile = (TileEntityTieredMachineBase) tile;
        this.cover = (SwitchCover) ((TileEntityCoverable) tile).getCovers().get(Direction.values()[data.getInteger("side")]);
    }

    @Override
    public void render(int mouseX, int mouseY, float renderPartialTicks) {
        this.renderBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(renderPartialTicks);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.enableInventoryLight();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) centerX, (float) centerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        GL11.glDisable(32826);
        Lighting.disable();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.render(mouseX, mouseY, renderPartialTicks);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2929);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/signalindustries/gui/config.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Configure: Switch", 45, 6, 0xFF404040);
        if (tile.hasCoverAnywhere(RedstoneCover.class)) {
            font.drawString("Control by Redstone Cover?", 20, 50, 0xFF404040);
        }
    }

    @Override
    public void init() {
        if (tile.hasCoverAnywhere(RedstoneCover.class)) {
            buttons.add(new ButtonElement(0, (width / 2) - 15, (height / 2) - 25, 30, 20, tile.disabled ? "OFF" : "ON"));
            buttons.add(new ButtonElement(1, (width / 2) - 15, (height / 2) + 20, 30, 20, cover.controlledByRedstone ? "Yes" : "No"));
        } else {
            buttons.add(new ButtonElement(0, (width / 2) - 15, (height / 2) - 10, 30, 20, tile.disabled ? "OFF" : "ON"));
        }

        if (cover.controlledByRedstone) {
            buttons.get(0).enabled = false;
        }

        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        switch (button.id) {
            case 0:
                tile.disabled = !tile.disabled;
                button.displayString = tile.disabled ? "OFF" : "ON";
                break;
            case 1:
                cover.controlledByRedstone = !cover.controlledByRedstone;
                button.displayString = cover.controlledByRedstone ? "Yes" : "No";
                buttons.get(0).enabled = !cover.controlledByRedstone;
                break;
        }
        if (EnvironmentHelper.isClientWorld()) {
            NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, TileEntityCoverable.CHANNEL_COVERS_START + cover.getDir().getSideNumber(), new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
        }
    }
}
