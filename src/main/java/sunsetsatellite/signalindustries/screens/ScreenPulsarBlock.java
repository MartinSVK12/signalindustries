package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import org.useless.DragonFly;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.menus.MenuPulsarBlock;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;

public class ScreenPulsarBlock extends ScreenFluid implements IExtendedScreenDraw {

    public Player player;
    public TileEntityPulsar tile;

    public ScreenPulsarBlock(ContainerInventory inv, TileEntity tile) {
        super(new MenuPulsarBlock(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityPulsar) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/pulsar_block_ui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int counter;
        if (this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 56, y + 36 + 12 - counter, 176, 12 - counter, 14, counter + 2);
        }

        counter = this.tile.getProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, counter + 1, 16);
        /*if(this.tile.speedMultiplier > 1){
            this.drawStringCentered(font, this.tile.speedMultiplier+"x",x + xSize - 16,y + ySize/2 - 16,tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }*/
        this.drawString(font, (int) ((tile.progressTicks / (float) tile.progressMaxTicks) * 100) + "%", x + 24, y + 36, 0xFFFFFFFF);
        if (tile.getItem(0) != null && tile.getItem(0).getItem() instanceof ItemWarpOrb) {
            this.drawStringCentered(font, "Warp", x + 140, y + 36, 0xFFFF00FF);
        } else {
            this.drawStringCentered(font, "Pulse", x + 140, y + 36, 0xFFFF0000);
        }

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
        font.drawCenteredString(I18n.getInstance().translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 56, Math.round((float) height / 2) - 75, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 56, Math.round((float) height / 2) - 30, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (button == itemIoButton) {
            mc.displayScreen(new ScreenVisualItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if (button == fluidIoButton) {
            mc.displayScreen(new ScreenVisualFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }
        super.buttonClicked(button);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        GL11.glPushMatrix();
        GL11.glScalef(2, 2, 2);
        Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar.png").bind();
        if (tile.getItem(0) != null && tile.getItem(0).getItem() instanceof ItemWarpOrb) {
            Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar_warp.png").bind();
        }
        StaticEntityModel item = DragonFly.loadEntityModel("geometry.signalindustries.pulsar_item", 0);
        StaticEntityModel innerCore = DragonFly.loadEntityModel("geometry.signalindustries.pulsar_inner_core", 0);
        StaticEntityModel outerCore = DragonFly.loadEntityModel("geometry.signalindustries.pulsar_outer_core", 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslatef(44, -10, 0);
        GL11.glRotatef(tile.orbRotation * 20 + partialTick, 0, 1, 0);
        GL11.glScalef(1.3f, 1.3f, 1.3f);
        if (tile.fuelBurnTicks <= 0) {
            item.render(Tessellator.instance);
        }
        if (tile.progressTicks > tile.progressMaxTicks / 2) {
            innerCore.render(Tessellator.instance);
        }
        if (tile.progressTicks >= tile.progressMaxTicks) {
            outerCore.render(Tessellator.instance);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
