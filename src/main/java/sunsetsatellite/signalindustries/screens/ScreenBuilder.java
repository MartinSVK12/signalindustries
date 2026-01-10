package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.menus.MenuBuilder;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageBuilderConfig;
import sunsetsatellite.signalindustries.render.FakeItemElement;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBuilder;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScreenBuilder extends ScreenFluid {

    public Player player;
    public TileEntityBuilder tile;

    public ScreenBuilder(ContainerInventory inv, TileEntity tile) {
        super(new MenuBuilder(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityBuilder) tile;
        this.player = inv.player;
        ySize = 247;
    }

    @Override
    public void render(int mx, int my, float partialTick) {
        super.render(mx, my, partialTick);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        FakeItemElement guiRenderFakeItem = new FakeItemElement(mc);
        if(tile.itemContents[0] != null && tile.itemContents[0].getItem() instanceof ItemBlueprint) {
            Structure multiblock = SignalIndustries.getStructureFromBlueprint(tile.itemContents[0], tile.worldObj);
            if(multiblock != null) {
                List<ItemStack> blocksUncondensed = tile.buildingBlocks
                        .stream()
                        .map((B) -> {
                            ItemStack stack = new ItemStack(B.block, 1, B.meta == -1 ? 0 : B.meta);
                            if(!stack.getHasSubtypes()) {
                                stack.setMetadata(0);
                            }
                            return stack;
                        })
                        .collect(Collectors.toList());
                List<ItemStack> blocks = Catalyst.condenseItemList(blocksUncondensed);

                for (int k = 1; k < Math.min(tile.getContainerSize() + 1, blocks.size() + 1); k++) {
                    Slot slot = inventorySlots.getSlot(k);
                    if (slot != null && slot.getItemStack() == null) {
                        guiRenderFakeItem.render(blocks.get(k - 1), i + slot.x, j + slot.y, false, null, true);
                    }
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/reinforced_builder_gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int counter;
        if(this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 153, y + 18 + 12 - counter, 176, 28 - counter, 14, counter + 2);
        }
        if(this.tile.speedMultiplier > 1){
            this.drawStringCentered(font, this.tile.speedMultiplier+"x",x + xSize - 16, (int) (y + (ySize/1.5f) - 10),tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (tile.tier){
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
        font.drawCenteredString(I18n.getInstance().translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
        font.drawCenteredString("Offset", 42, 12, 0xFFFFFFFF);
        font.drawCenteredString("Rotation", 108, 35, 0xFFFFFFFF);

        font.drawString(String.valueOf(tile.offset.x),35,30,0xFFFFFFFF,true);
        font.drawString(String.valueOf(tile.offset.y),35,55,0xFFFFFFFF,true);
        font.drawString(String.valueOf(tile.offset.z),35,80,0xFFFFFFFF,true);

        font.drawString(String.valueOf(tile.rotation.getName().charAt(0)),105,55,0xFFFFFFFF,true);
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round(width / 2f) + 20, Math.round(height / 2f) - 47, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round(width / 2f) + 40, Math.round(height / 2f) - 47, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;

        buttons.add(new ButtonElement(2, Math.round(width / 2f) + 63, Math.round(height / 2f) - 69, 20, 20, tile.workTimer.isPaused() ? "OFF" : "ON"));
        buttons.add(new ButtonElement(3, Math.round(width / 2f) - 75, Math.round(height / 2f) - 100, 20, 20, "+"));
        buttons.add(new ButtonElement(4, Math.round(width / 2f) - 35, Math.round(height / 2f) - 100, 20, 20, "-"));
        buttons.add(new ButtonElement(5, Math.round(width / 2f) - 75, Math.round(height / 2f) - 75, 20, 20, "+"));
        buttons.add(new ButtonElement(6, Math.round(width / 2f) - 35, Math.round(height / 2f) - 75, 20, 20, "-"));
        buttons.add(new ButtonElement(7, Math.round(width / 2f) - 75, Math.round(height / 2f) - 50, 20, 20, "+"));
        buttons.add(new ButtonElement(8, Math.round(width / 2f) - 35, Math.round(height / 2f) - 50, 20, 20, "-"));
        buttons.add(new ButtonElement(9, Math.round(width / 2f) - 10, Math.round(height / 2f) - 75, 20, 20, "<"));
        buttons.add(new ButtonElement(10, Math.round(width / 2f) + 30, Math.round(height / 2f) - 75, 20, 20, ">"));

        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if(!button.enabled) return;

        if(button == itemIoButton){
            mc.displayScreen(new ScreenItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if(button == fluidIoButton){
            mc.displayScreen(new ScreenFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }

        if(EnvironmentHelper.isClientWorld()){
            switch (button.id){
                case 2:
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, !tile.workTimer.isPaused(), tile.getClass()));
                    button.displayString = tile.workTimer.isPaused() ? "OFF" : "ON";
                    break;
                case 3:
                    tile.offset.x += 1;
                    tile.reset();
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
                    break;
                case 5:
                    tile.offset.y += 1;
                    tile.reset();
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
                    break;
                case 7:
                    tile.offset.z += 1;
                    tile.reset();
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
                    break;
                case 4:
                    tile.offset.x -= 1;
                    tile.reset();
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
                    break;
                case 6:
                    tile.offset.y -= 1;
                    tile.reset();
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
                    break;
                case 8:
                    tile.offset.z -= 1;
                    tile.reset();
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
                    break;
                case 9: {
                    int i = tile.rotation.getSideNumber();
                    i+=1;
                    if (i > 5){
                        i = 2;
                    }
                    tile.rotation = Direction.getDirectionFromSide(i);
                    tile.reset();
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
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
                    NetworkHandler.sendToServer(new NetworkMessageBuilderConfig(tile.offset, tile.rotation, tile.workTimer.isPaused(), tile.getClass()));
                    break;
                }
                default:
                    break;
            }
        } else {
            switch (button.id){
                case 2:
                    if(tile.workTimer.isPaused() && (tile.fluidContents[0] != null && tile.itemContents[0] != null && tile.itemContents[0].getItem() instanceof ItemBlueprint)){
                        tile.workTimer.unpause();
                        tile.setStructureToBuild();
                        for (BlockInstance block : new ArrayList<>(tile.buildingBlocks)) {
                            if (tile.worldObj != null && block.exists(tile.worldObj)) {
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
                    button.displayString = tile.workTimer.isPaused() ? "OFF" : "ON";

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


        super.buttonClicked(button);
    }
}
