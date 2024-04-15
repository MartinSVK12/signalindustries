

package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.containers.ContainerSensorPipe;
import sunsetsatellite.signalindustries.inventories.TileEntityConduit;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;

public class GuiSensorPipeConfig extends GuiContainer
{

    public GuiRenderItem guiRenderItem;
    public GuiTooltip guiTooltip;
    public InventoryPlayer inventoryPlayer;

    public GuiSensorPipeConfig(InventoryPlayer inventoryplayer, TileEntityItemConduit conduit)
    {
        super(new ContainerSensorPipe(inventoryplayer));
        inventoryPlayer = inventoryplayer;
        tile = conduit;
        Minecraft mc = Minecraft.getMinecraft(this);
        guiRenderItem = new GuiRenderItem(mc);
        guiTooltip = new GuiTooltip(mc);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int l = mc.renderEngine.getTexture("/assets/signalindustries/gui/sensor_pipe_gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Configure: Sensor", 45, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        fontRenderer.drawString(String.valueOf(tile.sensorAmount), 120, 42, 0x404040);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        super.drawScreen(mouseX, mouseY, partialTick);
        //pseudo slot rendering
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        mouseX -= k;
        mouseY -= l;
        int pseudoSlotX = 45;
        int pseudoSlotY = 35;
        ItemStack grabbedItem = inventoryPlayer.getHeldItemStack();
        if(mouseX >= pseudoSlotX - 1 && mouseX < pseudoSlotX + 16 + 1 && mouseY >= pseudoSlotY - 1 && mouseY < pseudoSlotY + 16 + 1){
            guiRenderItem.render(tile.sensorStack,k+45,l+35,true);
            if (grabbedItem == null && tile.sensorStack != null) {
                boolean showDescription = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || this.mc.gameSettings.alwaysShowDescriptions.value;
                String str = this.guiTooltip.getTooltipText(tile.sensorStack, showDescription);
                if (!str.isEmpty()) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    this.guiTooltip.render(str, k+mouseX, l+mouseY, 8, -8);
                }
            }
        } else {
            if(tile.sensorStack != null){
                guiRenderItem.render(tile.sensorStack,k+45,l+35,false);
            }
        }
        GL11.glEnable(2929);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        mouseX -= k;
        mouseY -= l;
        int pseudoSlotX = 45;
        int pseudoSlotY = 35;
        ItemStack grabbedItem = inventoryPlayer.getHeldItemStack();
        if(mouseX >= pseudoSlotX - 1 && mouseX < pseudoSlotX + 16 + 1 && mouseY >= pseudoSlotY - 1 && mouseY < pseudoSlotY + 16 + 1){
            if(mouseButton == 0 && grabbedItem != null){
                ItemStack copy = grabbedItem.copy();
                copy.stackSize = 1;
                tile.sensorStack = copy;
            } else if (mouseButton == 1) {
                tile.sensorStack = null;
            }
        }
    }

    public void init()
    {
        super.init();
        GuiButton guibutton = new GuiButton(0, Math.round(width / 2 - 10), Math.round(height / 2 - 50), 20, 20, "=");
        controlList.add(guibutton);
        controlList.add(new GuiButton(1, Math.round(width / 2 + 30) , Math.round(height / 2 - 65), 20, 20, "+"));
        controlList.add(new GuiButton(2, Math.round(width / 2 + 30), Math.round(height / 2 - 30), 20, 20, "-"));
        controlList.add(new GuiButton(3, Math.round(width / 2 + 60) , Math.round(height / 2) - 75, 20, 20, tile.sensorUseMeta ? "M" : "!M"));
        controlList.add(new GuiButton(4, Math.round(width / 2 + 60) , Math.round(height / 2) - 55, 20, 20, tile.sensorUseData ? "D" : "!D"));
        controlList.add(new GuiButton(5, Math.round(width / 2) - 25, Math.round(height / 2) - 25, 50, 15, String.valueOf(tile.mode)));
        switch (tile.sensorMode){
            case 0:
                guibutton.displayString = "=";
                break;
            case 1:
                guibutton.displayString = "!=";
                break;
            case 2:
                guibutton.displayString = ">";
                break;
            case 3:
                guibutton.displayString = "<";
                break;
            case 4:
                guibutton.displayString = ">=";
                break;
            case 5:
                guibutton.displayString = "<=";
                break;
            case 6:
                tile.sensorMode = 0;
                guibutton.displayString = "=";
                break;
        }
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 2) {
            if(tile.sensorAmount > 0)
                tile.sensorAmount--;
        }
        if (guibutton.id == 1) {
            tile.sensorAmount++;
        }
        if(guibutton.id == 3){
            tile.sensorUseMeta = !tile.sensorUseMeta;
            guibutton.displayString = tile.sensorUseMeta ? "M" : "!M";
        }
        if(guibutton.id == 4){
            tile.sensorUseData = !tile.sensorUseData;
            guibutton.displayString = tile.sensorUseData ? "D" : "!D";
        }
        if(guibutton.id == 0) {
            tile.sensorMode++;
            switch (tile.sensorMode){
                case 0:
                    guibutton.displayString = "=";
                    break;
                case 1:
                    guibutton.displayString = "!=";
                    break;
                case 2:
                    guibutton.displayString = ">";
                    break;
                case 3:
                    guibutton.displayString = "<";
                    break;
                case 4:
                    guibutton.displayString = ">=";
                    break;
                case 5:
                    guibutton.displayString = "<=";
                    break;
                case 6:
                    tile.sensorMode = 0;
                    guibutton.displayString = "=";
                    break;
            }
        }
        if(guibutton.id == 5){
            switch (tile.mode){
                case RANDOM:
                    tile.mode = PipeMode.SPLIT;
                    break;
                case SPLIT:
                    tile.mode = PipeMode.RANDOM;
                    break;
            }
            guibutton.displayString = String.valueOf(tile.mode);
        }

    }

    private final TileEntityItemConduit tile;
}
