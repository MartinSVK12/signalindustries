package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.containers.ContainerRecipeMaker;
import sunsetsatellite.signalindustries.inventories.TileEntityRecipeMaker;

import java.util.Objects;

public class GuiRecipeMaker extends GuiContainer {

    public String name = "Recipe Maker";
    public EntityPlayer entityplayer;
    public TileEntityRecipeMaker tile;
    public GuiTextField recipeName;
    public GuiRecipeMaker(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerRecipeMaker(inventoryPlayer, (TileEntityRecipeMaker) tile));
        this.entityplayer = inventoryPlayer.player;
        this.tile = (TileEntityRecipeMaker) tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/gui/crafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        recipeName.drawTextBox();
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString(name, 64, 6, 0xFF404040);
    }


    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                tile.makeRecipe();
                break;
            case 1:
                tile.deleteContents();
                break;
            case 2:
                tile.shaped = !tile.shaped;
                guibutton.displayString = tile.shaped ? "S" : "!S";
                break;
            case 3:
                tile.useContainers = !tile.useContainers;
                guibutton.displayString = tile.useContainers ? "C" : "!C";
                break;
            default:
                break;
        }
    }


    @Override
    public void keyTyped(char c, int i, int mouseX, int mouseY) {
        if(recipeName.isFocused) {
            Keyboard.enableRepeatEvents(true);
            if (c == Keyboard.KEY_ESCAPE) {
                Keyboard.enableRepeatEvents(false);
                recipeName.setFocused(false);
            } else recipeName.textboxKeyTyped(c, i);
            tile.currentRecipeName = recipeName.getText();
        } else{
            super.keyTyped(c,i,mouseX,mouseY);
        }
    }

    @Override
    public void mouseClicked(int i1, int i2, int i3) {
        recipeName.mouseClicked(i1, i2, i3);
        super.mouseClicked(i1, i2, i3);
    }


    @Override
    public void init()
    {
        recipeName = new GuiTextField(this, fontRenderer, Math.round((float) width / 2),Math.round((float) height/2 - 26),80,20, tile.currentRecipeName,"Recipe name..");
        controlList.add(new GuiButton(0, Math.round(width / 2) - 80, Math.round(height / 2) - 80, 20, 20, "+"));
        controlList.add(new GuiButton(1, Math.round(width / 2) - 80, Math.round(height / 2) - 55, 20, 20, "X"));
        controlList.add(new GuiButton(2, Math.round(width / 2) - 80, Math.round(height / 2) - 30, 20, 20, tile.shaped ? "S" : "!S"));
        controlList.add(new GuiButton(3, Math.round(width / 2) + 60, Math.round(height / 2) - 55, 20, 20, tile.useContainers ? "C" : "!C"));
        super.init();
    }
}

