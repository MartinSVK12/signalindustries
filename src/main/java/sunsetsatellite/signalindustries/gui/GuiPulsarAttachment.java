package sunsetsatellite.signalindustries.gui;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiItemFluid;
import sunsetsatellite.signalindustries.containers.ContainerPulsarAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemPulsarAttachment;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;

public class GuiPulsarAttachment extends GuiItemFluid {

    ItemStack pulsar;
    EntityPlayer player;
    public GuiPulsarAttachment(InventoryPlayer inventoryPlayer, ItemStack pulsar) {
        super(new ContainerPulsarAttachment(inventoryPlayer,pulsar),inventoryPlayer);
        this.pulsar = pulsar;
        this.player = inventoryPlayer.player;

    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/signalindustries/gui/pulsar_ui_2.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawCenteredString("The Pulsar", 90, 6, 0xFFCC0000);
    }

    @Override
    public void onClosed(){
        if(pulsar.getItem() instanceof ItemPulsarAttachment){
            NBTHelper.saveInvToNBT(pulsar,((ContainerPulsarAttachment)inventorySlots).inv);
        }
    }

}
