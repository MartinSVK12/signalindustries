package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.menus.MenuSensorPipe;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageSensorPipeSetFilter;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenSensorPipeConfig extends ScreenContainerAbstract {

    public ItemElement guiRenderItem;
    public TooltipElement TooltipElement;
    public ContainerInventory inventoryPlayer;

    public ScreenSensorPipeConfig(ContainerInventory inventoryplayer, TileEntityItemConduit conduit) {
        super(new MenuSensorPipe(inventoryplayer, conduit));
        inventoryPlayer = inventoryplayer;
        tile = conduit;
        Minecraft mc = Minecraft.getMinecraft();
        guiRenderItem = new ItemElement(mc);
        TooltipElement = new TooltipElement(mc);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture l = mc.textureManager.loadTexture("/assets/signalindustries/gui/sensor_pipe_gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Configure: Sensor", 45, 6, 0x404040);
        font.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        font.drawString(String.valueOf(tile.sensorAmount), 120, 42, 0x404040);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {
        super.render(mouseX, mouseY, partialTick);
        //pseudo slot rendering
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        mouseX -= k;
        mouseY -= l;
        int pseudoSlotX = 45;
        int pseudoSlotY = 35;
        ItemStack grabbedItem = inventoryPlayer.getHeldItemStack();
        if (mouseX >= pseudoSlotX - 1 && mouseX < pseudoSlotX + 16 + 1 && mouseY >= pseudoSlotY - 1 && mouseY < pseudoSlotY + 16 + 1) {
            guiRenderItem.render(tile.sensorStack, k + 45, l + 35, true);
            if (grabbedItem == null && tile.sensorStack != null) {
                boolean showDescription = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || this.mc.gameSettings.itemDescriptions.value == DescriptionPromptEnum.ALWAYS_SHOW;
                String str = this.TooltipElement.getTooltipText(tile.sensorStack, showDescription);
                if (!str.isEmpty()) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    this.TooltipElement.render(str, k + mouseX, l + mouseY, 8, -8);
                }
            }
        } else {
            if (tile.sensorStack != null) {
                guiRenderItem.render(tile.sensorStack, k + 45, l + 35, false);
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
        if (mouseX >= pseudoSlotX - 1 && mouseX < pseudoSlotX + 16 + 1 && mouseY >= pseudoSlotY - 1 && mouseY < pseudoSlotY + 16 + 1) {
            if (mouseButton == 0 && grabbedItem != null) {
                ItemStack copy = grabbedItem.copy();
                copy.stackSize = 1;
                tile.sensorStack = copy;
            } else if (mouseButton == 1) {
                tile.sensorStack = null;
            }
            if (EnvironmentHelper.isClientWorld()) {
                NetworkHandler.sendToServer(new NetworkMessageSensorPipeSetFilter(new Vec3i(tile.x, tile.y, tile.z), tile.sensorStack, tile.getClass()));
            }
        }
    }

    public void init() {
        super.init();
        ButtonElement guibutton = new ButtonElement(0, Math.round((float) width / 2 - 10), Math.round((float) height / 2 - 50), 20, 20, "=");
        buttons.add(guibutton);
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 + 30), Math.round((float) height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonElement(2, Math.round((float) width / 2 + 30), Math.round((float) height / 2 - 30), 20, 20, "-"));
        buttons.add(new ButtonElement(3, Math.round((float) width / 2 + 60), Math.round((float) height / 2) - 75, 20, 20, tile.sensorUseMeta ? "M" : "!M"));
        buttons.add(new ButtonElement(4, Math.round((float) width / 2 + 60), Math.round((float) height / 2) - 55, 20, 20, tile.sensorUseData ? "D" : "!D"));
        buttons.add(new ButtonElement(5, Math.round((float) width / 2) - 25, Math.round((float) height / 2) - 25, 50, 15, String.valueOf(tile.mode)));
        switch (tile.sensorMode) {
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
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 2) {
            if (tile.sensorAmount > 0)
                tile.sensorAmount--;
        }
        if (guibutton.id == 1) {
            tile.sensorAmount++;
        }
        if (guibutton.id == 3) {
            tile.sensorUseMeta = !tile.sensorUseMeta;
            guibutton.displayString = tile.sensorUseMeta ? "M" : "!M";
        }
        if (guibutton.id == 4) {
            tile.sensorUseData = !tile.sensorUseData;
            guibutton.displayString = tile.sensorUseData ? "D" : "!D";
        }
        if (guibutton.id == 0) {
            tile.sensorMode++;
            switch (tile.sensorMode) {
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
        if (guibutton.id == 5) {
            switch (tile.mode) {
                case RANDOM:
                    tile.mode = PipeMode.SPLIT;
                    break;
                case SPLIT:
                    tile.mode = PipeMode.RANDOM;
                    break;
            }
            guibutton.displayString = String.valueOf(tile.mode);
        }
        if (EnvironmentHelper.isClientWorld()) {
            NetworkHandler.sendToServer(new PacketScreenAction(guibutton.id, 0, 0, new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
        }

    }

    private final TileEntityItemConduit tile;
}
