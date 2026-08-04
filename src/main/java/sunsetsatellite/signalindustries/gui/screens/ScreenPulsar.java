package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;

import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.signalindustries.gui.menus.MenuPulsar;
import sunsetsatellite.signalindustries.items.tools.ItemPulsar;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Objects;

public class ScreenPulsar extends ScreenFluid {

    public int pulsarSlotIndex;
    public boolean isArmor;
    public ItemStack pulsar;
    public Player player;

    public ScreenPulsar(ContainerInventory inventoryPlayer, int pulsarSlotIndex, boolean isArmor) {
        super(new MenuPulsar(inventoryPlayer, pulsarSlotIndex, isArmor));
        this.pulsarSlotIndex = pulsarSlotIndex;
        this.isArmor = isArmor;
        this.player = inventoryPlayer.player;
        /*if(isArmor){
            this.pulsar = ((IPlayerPowerSuit<?>) inventoryPlayer.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        this.pulsar = inventoryPlayer.getItem(pulsarSlotIndex);
        //}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/pulsar_ui.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        String name = "";
        if (pulsar.getItem() instanceof ItemPulsar) {
            name = Catalyst.translateNameKey(fluidSlots.itemInventory.getNameTranslationKey());
            if (Objects.requireNonNull(((ItemPulsar) pulsar.getItem()).tier) == Tier.REINFORCED) {
                color = 0xFFFF0000;
            }
            drawStringCenteredShadow(fontRenderer, name, 90, 6, color);
        }
    }

    @Override
    public void removed() {
        super.removed();
        /*if(isArmor){
            this.pulsar = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        this.pulsar = player.inventory.getItem(pulsarSlotIndex);
        //}
        InventorySerializer.saveInvToNBT(pulsar, fluidSlots.itemInventory);
    }
}
