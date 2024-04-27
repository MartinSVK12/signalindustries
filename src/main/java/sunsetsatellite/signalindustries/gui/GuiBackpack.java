package sunsetsatellite.signalindustries.gui;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiItemFluid;
import sunsetsatellite.signalindustries.containers.ContainerBackpack;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;

public class GuiBackpack extends GuiItemFluid {

    ItemStack backpack;
    EntityPlayer player;
    public GuiBackpack(InventoryPlayer inventoryPlayer, ItemStack backpack) {
        super(new ContainerBackpack(inventoryPlayer,backpack),inventoryPlayer);
        this.backpack = backpack;
        this.player = inventoryPlayer.player;
        if(backpack.getItem() instanceof ItemBackpackAttachment){
            switch (((ItemBackpackAttachment) backpack.getItem()).tier){
                case BASIC:
                    ySize = 168;
                    xSize = 198;
                    break;
                case REINFORCED:
                    ySize = 223;
                    xSize = 198;
                    break;
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/signalindustries/gui/basic_backpack.png");
        if(backpack.getItem() instanceof ItemBackpackAttachment){
            switch (((ItemBackpackAttachment) backpack.getItem()).tier){
                case BASIC:
                    i = mc.renderEngine.getTexture("assets/signalindustries/gui/basic_backpack.png");
                    break;
                case REINFORCED:
                    i = mc.renderEngine.getTexture("assets/signalindustries/gui/reinforced_backpack.png");
                    break;
            }
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        String name = "";
        if(backpack.getItem() instanceof ItemBackpackAttachment){
            switch (((ItemBackpackAttachment) backpack.getItem()).tier){
                case BASIC:
                    color = 0xFFFF8080;
                    name = "Basic Backpack";
                    break;
                case REINFORCED:
                    color = 0xFFFF0000;
                    name = "Reinforced Backpack";
                    break;
            }
            fontRenderer.drawCenteredString(name, 90, 6, color);
        }
    }

    @Override
    public void onClosed() {
        if(backpack.getItem() instanceof ItemBackpackAttachment){
            NBTHelper.saveInvToNBT(backpack,((ContainerBackpack)inventorySlots).inv);
        }
    }

}
