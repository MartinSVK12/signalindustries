package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAbilityModule;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;
import sunsetsatellite.signalindustries.menus.MenuAbilityModule;
import sunsetsatellite.signalindustries.menus.MenuBackpack;
import sunsetsatellite.signalindustries.util.DrawUtil;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;

public class ScreenAbilityModule extends ScreenFluid {

    public int slotIndex;
    public boolean isArmor;
    public ItemStack module;
    public Player player;

    public ScreenAbilityModule(ContainerInventory inventoryPlayer, int slotIndex, boolean isArmor) {
        super(new MenuAbilityModule(inventoryPlayer, slotIndex, isArmor));
        this.slotIndex = slotIndex;
        this.isArmor = isArmor;
        this.player = inventoryPlayer.player;
        if(isArmor){
            this.module = ((IPlayerPowerSuit<?>) inventoryPlayer.player).getPowerSuit().getAttachmentClass(ItemAbilityModule.class);
        } else {
            this.module = inventoryPlayer.getItem(slotIndex);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        Tier tier = ((ItemAbilityModule) module.getItem()).getTier();
        int color = tier.getColor();
        int color2 = tier.getColor(0x40);
        DrawUtil drawUtil = new DrawUtil();
        drawGradientRect(i - 4, j - 4, i + xSize + 4, j, color, color2);
        drawGradientRect(i - 4, j + ySize, i + xSize + 4, j + ySize + 4, color2, color);
        drawUtil.drawGradientRect(i - 4, j - 4, i, j + ySize + 4, color2, color, color, color2);
        drawUtil.drawGradientRect(i + xSize, j - 4, i + xSize + 4, j + ySize + 4, color, color2, color2, color);
        drawGradientRect(i, j, i + xSize, j + ySize, color2, color2);
        //drawUtil.drawGradientRect(i,j,i+xSize,j+ySize,color,0xFF00FF00,0xFF00FF00,color);
        for (Slot inventorySlot : inventorySlots.slots) {
            int x = inventorySlot.x;
            int y = inventorySlot.y;
            //drawStringCentered(fontRenderer,String.valueOf(inventorySlot.id),i+x,j+y,color);
            drawGradientRect(i + x, j + y, i + x + 16, j + y + 16, tier.getColor(0xA0), tier.getColor(0xA0));
        }
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        String name = I18n.getInstance().translateNameKey(fluidSlots.itemInventory.getNameTranslationKey());
        Tier tier = ((ItemAbilityModule) module.getItem()).getTier();
        if(module.getItem() instanceof ItemAbilityModule){
            switch (tier){
                case REINFORCED:
                    name = I18n.getInstance().translateNameKey("container.signalindustries.abilityModule");
                    break;
                case AWAKENED:
                    name = I18n.getInstance().translateNameKey("container.signalindustries.awakenedAbilityModule");
                    break;
            }
            font.drawCenteredString(name, 90, 6, tier.getColor());
        }
    }

    @Override
    public void removed() {
        super.removed();
        if(isArmor){
            this.module = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemAbilityModule.class);
        } else {
            this.module = player.inventory.getItem(slotIndex);
        }
        InventorySerializer.saveInvToNBT(module,fluidSlots.itemInventory);
    }
}
