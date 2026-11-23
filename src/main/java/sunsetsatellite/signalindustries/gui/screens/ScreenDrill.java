package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;

import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.signalindustries.gui.menus.MenuDrill;
import sunsetsatellite.signalindustries.gui.menus.MenuPulsar;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.base.ItemTiered;
import sunsetsatellite.signalindustries.items.tools.ItemPulsar;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumDrill;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.util.slot.SlotAttachment;

import java.util.Objects;

public class ScreenDrill extends ScreenFluid {

    public int slotIndex;
    public boolean isArmor;
    public ItemStack stack;
    public Player player;

    public ScreenDrill(ContainerInventory inventoryPlayer, int slotIndex, boolean isArmor) {
        super(new MenuDrill(inventoryPlayer, slotIndex, isArmor));
        this.slotIndex = slotIndex;
        this.isArmor = isArmor;
        this.player = inventoryPlayer.player;
        /*if(isArmor){
            this.pulsar = ((IPlayerPowerSuit<?>) inventoryPlayer.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        this.stack = inventoryPlayer.getItem(slotIndex);
        //}
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/basic_item_ui.png");
		if(stack.getItem() instanceof ITiered tiered){
			switch (tiered.getTier()){
				case BASIC -> bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/basic_item_ui.png");
				case REINFORCED -> bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/reinforced_item_ui.png");
			}
		}
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
        if (stack.getItem() instanceof ITiered tiered) {
            name = Catalyst.translateNameKey(fluidSlots.itemInventory.getNameTranslationKey());
			switch (tiered.getTier()) {
				case PROTOTYPE -> {}
				case BASIC -> color = 0xFFFF8080;
				case REINFORCED -> color = 0xFFFF0000;
				case AWAKENED -> color = 0xFFFFA500;
			}
            drawStringCenteredShadow(fontRenderer, name, 90, 8, color);
        }
    }

    @Override
    public void removed() {
        super.removed();
        /*if(isArmor){
            this.pulsar = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        this.stack = player.inventory.getItem(slotIndex);
        //}
        InventorySerializer.saveInvToNBT(stack, fluidSlots.itemInventory, ItemSignalumDrill.class);
    }
	@Override
	public void render(int mx, int my, float partialTick) {
		super.render(mx, my, partialTick);
		for (int k = 0; k < inventorySlots.slots.size(); k++) {
			Slot slot = inventorySlots.slots.get(k);
			if (getIsMouseOverSlot(slot, mx, my) && slot instanceof SlotAttachment) {
				if (slot.getItemStack() == null) {
					Lighting.disable();
					GLRenderer.disableState(State.CULL_FACE);
					TooltipElement tooltip = new TooltipElement(mc);
					tooltip.render("Slot accepts attachments of type:\n- " + ((SlotAttachment) slot).getAttachmentPoint(), mx, my, 8, -8);
				}
			}
		}
	}

}
