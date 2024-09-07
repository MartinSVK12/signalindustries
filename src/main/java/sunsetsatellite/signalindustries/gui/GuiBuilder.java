package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerBuilder;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBuilder;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.util.GuiRenderFakeItem;
import sunsetsatellite.signalindustries.util.SIMultiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GuiBuilder extends GuiFluid {
    public String name = "Builder";
    public EntityPlayer entityplayer;
    public TileEntityBuilder tile;


    public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerBuilder(inventoryPlayer, (TileEntityFluidItemContainer) tile),inventoryPlayer);
        this.tile = (TileEntityBuilder) tile;
        this.entityplayer = inventoryPlayer.player;
        ySize = 247;
    }

    @Override
    public void drawScreen(int i1, int i2, float f3) {
        super.drawScreen(i1, i2, f3);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        GuiRenderFakeItem guiRenderFakeItem = new GuiRenderFakeItem(mc);
        if(tile.itemContents[0] != null && tile.itemContents[0].getItem() instanceof ItemBlueprint) {
            String key = tile.itemContents[0].getData().getStringOrDefault("multiblock", "");
            if (Objects.equals(key, "")) {
                return;
            }
            SIMultiblock multiblock = (SIMultiblock) Multiblock.multiblocks.get(key.replace("multiblock.signalindustries.", ""));
            if (multiblock == null) {
                return;
            }
            List<ItemStack> blocksUncondensed = tile.buildingBlocks
                    .stream()
                    .map((B) -> new ItemStack(B.block, 1, B.meta == -1 ? 0 : B.meta))
                    .collect(Collectors.toList());
            List<ItemStack> blocks = SignalIndustries.condenseList(blocksUncondensed);

            for (int k = 1; k < Math.min(tile.getSizeInventory()+1, blocks.size()+1); k++) {
                Slot slot = inventorySlots.getSlot(k);
                if(slot != null && slot.getStack() == null){
                    guiRenderFakeItem.render(blocks.get(k-1),i+slot.xDisplayPosition,j+slot.yDisplayPosition, false, null, true);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        if(x > i+80 && x < i+94) {
            if (y > j + 40 && y < j + 46) {
                I18n translator = I18n.getInstance();
                String name = translator.translateKey(tile.getBlockType().getLanguageKey(0)+".name");
                ////GuidebookPlusPlus.nameFocus = ">"+ name;
                if(entityplayer instanceof EntityPlayerSP){
                    ((EntityPlayerSP)entityplayer).displayGUIGuidebook();
                } else if (entityplayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)entityplayer).displayGUIGuidebook();
                }
            }
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/reinforced_builder_gui.png");
        /*switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/generic_prototype_machine_double.png");
                break;
            case BASIC:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/generic_basic_machine_double.png");
                break;
            case REINFORCED:
            case AWAKENED:
                break;
        }*/
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
        int i5;
        if(this.tile.isBurning()) {
            i5 = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(i3 + 153, i4 + 18 + 12 - i5, 176, 28 - i5, 14, i5 + 2);
        }

        //i5 = this.tile.getProgressScaled(24);
        //this.drawTexturedModalRect(i3 + 85, i4 + 35, 176, 0, i5 + 1, 16);
        if(this.tile.speedMultiplier > 1){
            this.drawString(fontRenderer, this.tile.speedMultiplier+"x",i3 + 120,i4 + 6,tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }

        controlList.get(2).displayString = tile.workTimer.isPaused() ? "OFF" : "ON";

        //this.drawString(fontRenderer,tile.recipe == null ? "null" : tile.recipe.toString(),i3+2,i4+120,0xFFFF0000);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                break;
            case BASIC:
                color = 0xFFFF8080;
                break;
            case REINFORCED:
                color = 0xFFFF0000;
                break;
            case AWAKENED:
                color = 0xFFFFA500;
                break;
        }
        fontRenderer.drawCenteredString(name, 90, 6, color);
        fontRenderer.drawCenteredString("Offset", 42, 12, 0xFFFFFFFF);
        fontRenderer.drawCenteredString("Rotation", 108, 35, 0xFFFFFFFF);

        fontRenderer.drawString(String.valueOf(tile.offset.x),35,30,0xFFFFFFFF,true);
        fontRenderer.drawString(String.valueOf(tile.offset.y),35,55,0xFFFFFFFF,true);
        fontRenderer.drawString(String.valueOf(tile.offset.z),35,80,0xFFFFFFFF,true);

        fontRenderer.drawString(String.valueOf(tile.rotation.getName().charAt(0)),105,55,0xFFFFFFFF,true);
    }
    protected void buttonPressed(GuiButton guibutton) {
        //TODO: add callbacks later cause oh my god this is stupid
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                SignalIndustries.displayGui(entityplayer, () -> new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 1:
                SignalIndustries.displayGui(entityplayer, () -> new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 2:
                if(tile.workTimer.isPaused() && (tile.fluidContents[0] != null && tile.itemContents[0] != null && tile.itemContents[0].getItem() instanceof ItemBlueprint)){
                    tile.workTimer.unpause();
                    tile.setStructureToBuild();
                    for (BlockInstance block : new ArrayList<>(tile.buildingBlocks)) {
                        if(block.exists(tile.worldObj)){
                            tile.buildingBlocks.remove(block);
                            tile.builtBlocks++;
                        }
                    }
                    if(tile.buildingBlockIndex >= tile.buildingBlocks.size()){
                        tile.buildingBlockIndex = 0;
                    }
                } else {
                    tile.workTimer.pause();
                }
                guibutton.displayString = tile.workTimer.isPaused() ? "OFF" : "ON";
                break;
            case 3:
                tile.offset.x += 1;
                tile.reset();
                break;
            case 5:
                tile.offset.y += 1;
                tile.reset();
                break;
            case 7:
                tile.offset.z += 1;
                tile.reset();
                break;
            case 4:
                tile.offset.x -= 1;
                tile.reset();
                break;
            case 6:
                tile.offset.y -= 1;
                tile.reset();
                break;
            case 8:
                tile.offset.z -= 1;
                tile.reset();
                break;
            case 9: {
                int i = tile.rotation.getSideNumber();
                i+=1;
                if (i > 5){
                    i = 2;
                }
                tile.rotation = Direction.getDirectionFromSide(i);
                tile.reset();
                break;
            }
            case 10: {
                int i = tile.rotation.getSideNumber();
                i-=1;
                if (i < 2){
                    i = 5;
                }
                tile.rotation = Direction.getDirectionFromSide(i);
                tile.reset();
                break;
            }
            default:
                break;
        }
    }

    public void init()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) + 20, Math.round(height / 2) - 47, 20, 20, "F"));
        controlList.add(new GuiButton(1, Math.round(width / 2) + 40, Math.round(height / 2) - 47, 20, 20, "I"));
        controlList.add(new GuiButton(2, Math.round(width / 2) + 63, Math.round(height / 2) - 69, 20, 20, tile.workTimer.isPaused() ? "OFF" : "ON"));
        controlList.add(new GuiButton(3, Math.round(width / 2) - 75, Math.round(height / 2) - 100, 20, 20, "+"));
        controlList.add(new GuiButton(4, Math.round(width / 2) - 35, Math.round(height / 2) - 100, 20, 20, "-"));
        controlList.add(new GuiButton(5, Math.round(width / 2) - 75, Math.round(height / 2) - 75, 20, 20, "+"));
        controlList.add(new GuiButton(6, Math.round(width / 2) - 35, Math.round(height / 2) - 75, 20, 20, "-"));
        controlList.add(new GuiButton(7, Math.round(width / 2) - 75, Math.round(height / 2) - 50, 20, 20, "+"));
        controlList.add(new GuiButton(8, Math.round(width / 2) - 35, Math.round(height / 2) - 50, 20, 20, "-"));
        controlList.add(new GuiButton(9, Math.round(width / 2) - 10, Math.round(height / 2) - 75, 20, 20, "<"));
        controlList.add(new GuiButton(10, Math.round(width / 2) + 30, Math.round(height / 2) - 75, 20, 20, ">"));
        super.init();
    }
}
