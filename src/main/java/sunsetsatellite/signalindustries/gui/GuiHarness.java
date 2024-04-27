package sunsetsatellite.signalindustries.gui;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiItemFluid;
import sunsetsatellite.signalindustries.containers.ContainerHarness;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;

public class GuiHarness extends GuiItemFluid {

    ItemStack armor;
    EntityPlayer player;
    public GuiHarness(InventoryPlayer inventoryPlayer, ItemStack armor) {
        super(new ContainerHarness(inventoryPlayer,armor),inventoryPlayer);
        this.armor = armor;
        this.player = inventoryPlayer.player;

    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/signalindustries/gui/harness_ui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawCenteredString("Signalum Prototype Harness", 90, 6, 0xFFFF8080);
    }

    @Override
    public void onClosed(){
        if(armor.getItem() instanceof ItemSignalumPrototypeHarness){
            NBTHelper.saveInvToNBT(armor,((ContainerHarness)inventorySlots).inv);
        }
    }

}
