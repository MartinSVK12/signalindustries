package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.signalindustries.gui.menus.MenuHarness;
import sunsetsatellite.signalindustries.util.InventorySerializer;

public class ScreenHarness extends ScreenFluid {

    public int slotIndex;
    public boolean isArmor;
    public ItemStack armor;
    public Player player;

    public ScreenHarness(ContainerInventory inventoryPlayer, int slotIndex, boolean isArmor) {
        super(new MenuHarness(inventoryPlayer, slotIndex, isArmor));
        this.slotIndex = slotIndex;
        this.isArmor = isArmor;
        this.player = inventoryPlayer.player;
        this.armor = inventoryPlayer.getItem(slotIndex);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/harness_ui.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        drawStringCenteredShadow(fontRenderer, Catalyst.translateNameKey(fluidSlots.itemInventory.getNameTranslationKey()), 90, 6, 0xFFFF8080);
    }

    @Override
    public void removed() {
        super.removed();
        InventorySerializer.saveInvToNBT(player.inventory.getItem(slotIndex), fluidSlots.itemInventory);
    }
}
