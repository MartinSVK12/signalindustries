package sunsetsatellite.signalindustries.gui;


import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.ContainerItemFluid;
import sunsetsatellite.fluidapi.api.GuiItemFluid;
import sunsetsatellite.signalindustries.containers.ContainerAbilityModule;
import sunsetsatellite.signalindustries.items.ItemAbilityModule;
import sunsetsatellite.signalindustries.util.DrawUtil;
import sunsetsatellite.signalindustries.util.Mode;
import sunsetsatellite.signalindustries.util.NBTHelper;

public class GuiAbilityModule extends GuiItemFluid {

    public ContainerItemFluid container;
    public Mode mode;
    public GuiAbilityModule(ContainerItemFluid containerItemFluid, InventoryPlayer invP) {
        super(containerItemFluid, invP);
        this.container = containerItemFluid;
        this.mode = ((ItemAbilityModule)container.inv.item.getItem()).mode;
    }

    protected void drawGuiContainerBackgroundLayer(float f1) {
        super.drawGuiContainerBackgroundLayer(f1);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        int color = mode.getColor();
        int color2 = mode.getColor(0x40);
        DrawUtil drawUtil = new DrawUtil();
        drawGradientRect(i-4,j-4,i+xSize+4,j,color,color2);
        drawGradientRect(i-4,j+ySize,i+xSize+4,j+ySize+4,color2,color);
        drawUtil.drawGradientRect(i-4,j-4,i,j+ySize+4,color2,color,color,color2);
        drawUtil.drawGradientRect(i+xSize,j-4,i+xSize+4,j+ySize+4,color,color2,color2,color);
        drawGradientRect(i,j,i+xSize,j+ySize,color2,color2);
        //drawUtil.drawGradientRect(i,j,i+xSize,j+ySize,color,0xFF00FF00,0xFF00FF00,color);
        for (Slot inventorySlot : container.inventorySlots) {
            int x = inventorySlot.xDisplayPosition;
            int y = inventorySlot.yDisplayPosition;
            //drawStringCentered(fontRenderer,String.valueOf(inventorySlot.id),i+x,j+y,color);
            drawGradientRect(i+x,j+y,i+x+16,j+y+16,mode.getColor(0xA0), mode.getColor(0xA0));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        if(mode == Mode.AWAKENED){
            drawStringCentered(fontRenderer,mode.getName()+" Ability Module",xSize/2,8,mode.getColor());
        } else {
            drawStringCentered(fontRenderer,"Ability Module",xSize/2,8,mode.getColor());
        }
        GL11.glDisable(3042);
        GL11.glDisable(2896);
    }

    public void onGuiClosed(){
        NBTHelper.saveInvToNBT(container.inv.item,((ContainerAbilityModule)inventorySlots).inv);
    }

}
