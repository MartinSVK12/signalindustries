package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;
import sunsetsatellite.signalindustries.menus.MenuBackpack;
import sunsetsatellite.signalindustries.util.InventorySerializer;

public class ScreenBackpack extends ScreenFluid {

    public int backpackSlotIndex;
    public boolean isArmor;
    public ItemStack backpack;
    public Player player;

    public ScreenBackpack(ContainerInventory inventoryPlayer, int backpackSlotIndex, boolean isArmor) {
        super(new MenuBackpack(inventoryPlayer, backpackSlotIndex, isArmor));
        this.backpackSlotIndex = backpackSlotIndex;
        this.isArmor = isArmor;
        this.player = inventoryPlayer.player;
        if(isArmor){
            this.backpack = ((IPlayerPowerSuit<?>) inventoryPlayer.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {
            this.backpack = inventoryPlayer.getItem(backpackSlotIndex);
        }

        ySize = 168;
        xSize = 198;
        if (backpack != null && backpack.getItem() instanceof ItemBackpackAttachment) {
            switch (((ItemBackpackAttachment) backpack.getItem()).tier) {
                case REINFORCED:
                    ySize = 223;
                    break;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/basic_backpack.png");
        switch (((ItemBackpackAttachment) backpack.getItem()).getTier()){
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/basic_backpack.png");
                break;
            case REINFORCED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/reinforced_backpack.png");
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        String name = "";
        if(backpack.getItem() instanceof ItemBackpackAttachment){
            name = I18n.getInstance().translateNameKey(fluidSlots.itemInventory.getNameTranslationKey());
            switch (((ItemBackpackAttachment) backpack.getItem()).tier){
                case BASIC:
                    color = 0xFFFF8080;
                    break;
                case REINFORCED:
                    color = 0xFFFF0000;
                    break;
            }
            font.drawCenteredString(name, 90, 6, color);
        }
    }

    @Override
    public void removed() {
        super.removed();
        if(isArmor){
            this.backpack = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {
            this.backpack = player.inventory.getItem(backpackSlotIndex);
        }
        InventorySerializer.saveInvToNBT(backpack,fluidSlots.itemInventory);
    }
}
