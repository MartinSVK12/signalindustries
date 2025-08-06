package sunsetsatellite.signalindustries.screens;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.container.ScreenContainer;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.covers.RedstoneCover;
import sunsetsatellite.signalindustries.menus.MenuCover;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageSensorPipeSetFilter;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.PipeMode;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenRedstoneCoverConfig extends ScreenContainerAbstract {

    public RedstoneCover cover;
    public Player player;
    public TileEntityTieredMachineBase tile;

    public ItemElement guiRenderItem;
    public TooltipElement TooltipElement;

    public ScreenRedstoneCoverConfig(ContainerInventory playerInv, TileEntity tile, CompoundTag data) {
        super(new MenuCover(playerInv, (TileEntityFluidItemContainer) tile, data));
        this.xSize = 176;
        this.ySize = 166;
        this.player = playerInv.player;
        this.tile = (TileEntityTieredMachineBase) tile;
        this.cover = (RedstoneCover) ((TileEntityCoverable) tile).getCovers().get(Direction.values()[data.getInteger("side")]);
        Minecraft mc = Minecraft.getMinecraft();
        guiRenderItem = new ItemElement(mc);
        TooltipElement = new TooltipElement(mc);
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
        ItemStack grabbedItem = player.inventory.getHeldItemStack();
        if(mouseX >= pseudoSlotX - 1 && mouseX < pseudoSlotX + 16 + 1 && mouseY >= pseudoSlotY - 1 && mouseY < pseudoSlotY + 16 + 1){
            guiRenderItem.render(cover.sensorStack,k+45,l+35,true);
            if (grabbedItem == null && cover.sensorStack != null) {
                boolean showDescription = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || this.mc.gameSettings.itemDescriptions.value == DescriptionPromptEnum.ALWAYS_SHOW;
                String str = this.TooltipElement.getTooltipText(cover.sensorStack, showDescription);
                if (!str.isEmpty()) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    this.TooltipElement.render(str, k+mouseX, l+mouseY, 8, -8);
                }
            }
        } else {
            if(cover.sensorStack != null){
                guiRenderItem.render(cover.sensorStack,k+45,l+35,false);
            }
        }
        GL11.glEnable(2929);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/signalindustries/gui/sensor_pipe_gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        font.drawString("Configure: Sensor", 45, 6, 0xFF404040);
        font.drawString("Slot", 8, 8, 0x404040);
        font.drawString(String.valueOf(cover.sensorAmount), 120, 42, 0x404040);
        if(cover.sensorSlot == -1){
            font.drawString("*", 10, 42, 0x404040);
        } else {
            font.drawString(String.valueOf(cover.sensorSlot), 10, 42, 0x404040);
        }
    }

    @Override
    public void init() {
        super.init();
        ButtonElement button = new ButtonElement(0, Math.round((float)width / 2 - 10), Math.round((float)height / 2 - 50), 20, 20, "=");
        buttons.add(button);
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 + 30) ,Math.round((float)height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonElement(2, Math.round((float) width / 2 + 30),Math.round((float)height / 2 - 30), 20, 20, "-"));
        buttons.add(new ButtonElement(3, Math.round((float) width / 2 + 60) ,Math.round((float)height / 2) - 75, 20, 20, cover.sensorUseMeta ? "M" : "!M"));
        buttons.add(new ButtonElement(4, Math.round((float) width / 2 + 60) ,Math.round((float)height / 2) - 55, 20, 20, cover.sensorUseData ? "D" : "!D"));
        buttons.add(new ButtonElement(5, Math.round((float) width / 2 - 80) ,Math.round((float)height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonElement(6, Math.round((float) width / 2 - 80) ,Math.round((float)height / 2 - 30), 20, 20, "-"));
        switch (cover.sensorMode){
            case 0:
                button.displayString = "=";
                break;
            case 1:
                button.displayString = "!=";
                break;
            case 2:
                button.displayString = ">";
                break;
            case 3:
                button.displayString = "<";
                break;
            case 4:
                button.displayString = ">=";
                break;
            case 5:
                button.displayString = "<=";
                break;
            case 6:
                cover.sensorMode = 0;
                button.displayString = "=";
                break;
        }
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
        ItemStack grabbedItem = player.inventory.getHeldItemStack();
        if(mouseX >= pseudoSlotX - 1 && mouseX < pseudoSlotX + 16 + 1 && mouseY >= pseudoSlotY - 1 && mouseY < pseudoSlotY + 16 + 1){
            if(mouseButton == 0 && grabbedItem != null){
                ItemStack copy = grabbedItem.copy();
                copy.stackSize = 1;
                cover.sensorStack = copy;
            } else if (mouseButton == 1) {
                cover.sensorStack = null;
            }
            if(EnvironmentHelper.isClientWorld()){
                //NetworkHandler.sendToServer(new NetworkMessageSensorPipeSetFilter(new Vec3i(tile.x, tile.y, tile.z), cover.sensorStack, tile.getClass()));
            }
        }
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 2) {
            if(cover.sensorAmount > 0)
                cover.sensorAmount--;
        }
        if (button.id == 1) {
            cover.sensorAmount++;
        }
        if(button.id == 3){
            cover.sensorUseMeta = !cover.sensorUseMeta;
            button.displayString = cover.sensorUseMeta ? "M" : "!M";
        }
        if(button.id == 4){
            cover.sensorUseData = !cover.sensorUseData;
            button.displayString = cover.sensorUseData ? "D" : "!D";
        }
        if (button.id == 6) {
            if(cover.sensorSlot >= 0)
                cover.sensorSlot--;
        }
        if (button.id == 5) {
            if(cover.sensorSlot < tile.itemContents.length-1){
                cover.sensorSlot++;
            }
        }
        if(button.id == 0) {
            cover.sensorMode++;
            switch (cover.sensorMode){
                case 0:
                    button.displayString = "=";
                    break;
                case 1:
                    button.displayString = "!=";
                    break;
                case 2:
                    button.displayString = ">";
                    break;
                case 3:
                    button.displayString = "<";
                    break;
                case 4:
                    button.displayString = ">=";
                    break;
                case 5:
                    button.displayString = "<=";
                    break;
                case 6:
                    cover.sensorMode = 0;
                    button.displayString = "=";
                    break;
            }
        }
        if(EnvironmentHelper.isClientWorld()){
            NetworkHandler.sendToServer(new PacketScreenAction(button.id,0,TileEntityCoverable.CHANNEL_COVERS_START+cover.getDir().getSideNumber(),new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
        }
    }

}
