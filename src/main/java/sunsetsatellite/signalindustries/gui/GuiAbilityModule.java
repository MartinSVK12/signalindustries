package sunsetsatellite.signalindustries.gui;


import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ContainerItemFluid;
import sunsetsatellite.catalyst.fluids.impl.GuiItemFluid;
import sunsetsatellite.signalindustries.containers.ContainerAbilityModule;
import sunsetsatellite.signalindustries.items.ItemAbilityModule;
import sunsetsatellite.signalindustries.util.DrawUtil;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;
import sunsetsatellite.signalindustries.util.Tier;

public class GuiAbilityModule extends GuiItemFluid {

    public ContainerItemFluid container;
    public Tier tier;
    public GuiAbilityModule(ContainerItemFluid containerItemFluid, InventoryPlayer invP) {
        super(containerItemFluid, invP);
        this.container = containerItemFluid;
        this.tier = ((ItemAbilityModule)container.inv.item.getItem()).getTier();
    }

    protected void drawGuiContainerBackgroundLayer(float f1) {
        super.drawGuiContainerBackgroundLayer(f1);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        int color = tier.getColor();
        int color2 = tier.getColor(0x40);
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
            drawGradientRect(i+x,j+y,i+x+16,j+y+16, tier.getColor(0xA0), tier.getColor(0xA0));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        if(tier == Tier.AWAKENED){
            drawStringCentered(fontRenderer, "Awakened Application Module",xSize/2,8, tier.getColor());
        } else {
            drawStringCentered(fontRenderer,"Application Module",xSize/2,8, tier.getColor());
        }
        GL11.glDisable(3042);
        GL11.glDisable(2896);
    }

    @Override
    public void onClosed(){
        NBTHelper.saveInvToNBT(container.inv.item,((ContainerAbilityModule)inventorySlots).inv);
    }

}
