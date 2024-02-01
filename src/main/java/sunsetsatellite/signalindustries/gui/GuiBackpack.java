package sunsetsatellite.signalindustries.gui;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiItemFluid;
import sunsetsatellite.signalindustries.containers.ContainerBackpack;
import sunsetsatellite.signalindustries.containers.ContainerPulsar;
import sunsetsatellite.signalindustries.items.ItemPulsar;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;
import sunsetsatellite.signalindustries.util.NBTHelper;

public class GuiBackpack extends GuiItemFluid {

    ItemStack backpack;
    EntityPlayer player;
    public GuiBackpack(InventoryPlayer inventoryPlayer, ItemStack backpack) {
        super(new ContainerBackpack(inventoryPlayer,backpack),inventoryPlayer);
        this.backpack = backpack;
        this.player = inventoryPlayer.player;
        ySize = 223;
        xSize = 198;
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/signalindustries/gui/reinforced_backpack.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawCenteredString("Reinforced Backpack", 90, 6, 0xFFCC0000);
    }

    @Override
    public void onClosed() {
        if(backpack.getItem() instanceof ItemBackpackAttachment){
            NBTHelper.saveInvToNBT(backpack,((ContainerBackpack)inventorySlots).inv);
        }
    }

}
