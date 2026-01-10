package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.menus.MenuCrystalCutter;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageRecipeIdChange;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityCrystalCutter;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenCrystalCutter extends ScreenMachineSimple {

    public Player player;
    public TileEntityCrystalCutter tile;

    public ScreenCrystalCutter(ContainerInventory inv, TileEntity tile) {
        super(new MenuCrystalCutter(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityCrystalCutter) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/generic_prototype_machine_double.png");
        switch (tile.tier) {
            case PROTOTYPE:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/generic_prototype_machine_double.png");
                break;
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/generic_basic_machine_double.png");
                break;
            case REINFORCED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/generic_reinforced_machine_double.png");
                break;
            case AWAKENED:
                break;
        }
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
        if (this.tile.speedMultiplier > 1) {
            this.drawStringCentered(font, this.tile.speedMultiplier + "x", x + xSize - 16, y + ySize / 2 - 16, tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
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

    @Override
    public void render(int mx, int my, float partialTick) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        super.render(mx, my, partialTick);
        I18n trans = I18n.getInstance();
        StringBuilder text = new StringBuilder();
        int xOffset = 148;
        int yOffset = 52;
        int size = 20;
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (mx > i + xOffset && mx < i + xOffset + size) {
            if (my > j + yOffset && my < j + yOffset + size) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_CULL_FACE);
                TooltipElement tooltip = new TooltipElement(mc);
                tooltip.render("Click to increment ID of recipe that will be performed.\nShift+click to decrement.", mx, my, 8, -8);
            }
        }
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 80, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 60, 20, 20, "I");
        buttons.add(itemIo);

        fluidIoButton = fluidIo;
        itemIoButton = itemIo;

        buttons.add(new ButtonElement(2, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 30, 20, 20, String.valueOf(tile.recipeId)));
        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 2) {
            if (EnvironmentHelper.isClientWorld()) {
                if (tile.recipeId > 0 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54))) {
                    button.displayString = String.valueOf(tile.recipeId - 1);
                    NetworkHandler.sendToServer(new NetworkMessageRecipeIdChange(tile.recipeId - 1, new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
                } else {
                    button.displayString = String.valueOf(tile.recipeId + 1);
                    NetworkHandler.sendToServer(new NetworkMessageRecipeIdChange(tile.recipeId + 1, new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
                }

            } else {
                if (tile.recipeId > 0 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54))) {
                    tile.recipeId--;
                } else {
                    tile.recipeId++;
                }
                button.displayString = String.valueOf(tile.recipeId);
            }
        }

        if (button == itemIoButton) {
            mc.displayScreen(new ScreenItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if (button == fluidIoButton) {
            mc.displayScreen(new ScreenFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }
        super.buttonClicked(button);
    }
}
