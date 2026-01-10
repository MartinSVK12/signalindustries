package sunsetsatellite.signalindustries.screens;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.popup.PopupBuilder;
import net.minecraft.client.gui.popup.PopupScreen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.sound.SoundCategory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.covers.VoidCover;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

//TODO: make this work in mp
public class ScreenVoidCoverConfig extends Screen {
    public int xSize = 176;
    public int ySize = 90;

    public VoidCover cover;
    public Player player;
    public TileEntityCoverable tile;

    public ScreenVoidCoverConfig(ContainerInventory playerInv, TileEntity tile, CompoundTag data) {
        this.player = playerInv.player;
        this.tile = (TileEntityCoverable) tile;
        this.cover = (VoidCover) ((TileEntityCoverable) tile).getCovers().get(Direction.values()[data.getInteger("side")]);
    }

    @Override
    public void render(int mouseX, int mouseY, float renderPartialTicks) {
        this.renderBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(renderPartialTicks);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.enableInventoryLight();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) centerX, (float) centerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        GL11.glDisable(32826);
        Lighting.disable();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.render(mouseX, mouseY, renderPartialTicks);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2929);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/signalindustries/gui/config.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Configure: Voiding", 45, 6, 0xFF404040);
        font.drawString("Item Slot", xSize / 2 - 50, 23, 0xFF404040);
        font.drawString("Fluid Slot", xSize / 2 + 10, 23, 0xFF404040);
        font.drawString("Activate", xSize / 2 - 20, 60, 0xFF404040);
    }

    @Override
    public void init() {
        String voidSlotItemStr;
        String voidSlotFluidStr;
        if (cover.voidingItemSlot == -2) {
            voidSlotItemStr = "*";
        } else if (cover.voidingItemSlot == -1) {
            voidSlotItemStr = "X";
        } else {
            voidSlotItemStr = String.valueOf(cover.voidingItemSlot);
        }
        if (cover.voidingFluidSlot == -2) {
            voidSlotFluidStr = "*";
        } else if (cover.voidingFluidSlot == -1) {
            voidSlotFluidStr = "X";
        } else {
            voidSlotFluidStr = String.valueOf(cover.voidingFluidSlot);
        }
        buttons.add(new ButtonElement(1, Math.round((float) width / 2) - 40, (height / 2) - 10, 20, 20, voidSlotItemStr));
        buttons.add(new ButtonElement(2, Math.round((float) width / 2) + 20, (height / 2) - 10, 20, 20, voidSlotFluidStr));

        buttons.add(new ButtonElement(0, (width / 2) - 15, (height / 2) - 10, 30, 20, cover.active ? "ON" : "OFF"));

        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (tile != null) {
            switch (button.id) {
                case 0: {
                    cover.active = !cover.active;
                    button.displayString = cover.active ? "ON" : "OFF";
                    break;
                }
                case 1: {
                    int max = tile.getContainerSize() - 1;
                    if (cover.voidingItemSlot < max) {
                        cover.voidingItemSlot++;
                    }
                    String voidSlotItemStr;
                    if (cover.voidingItemSlot == -2) {
                        voidSlotItemStr = "*";
                    } else if (cover.voidingItemSlot == -1) {
                        voidSlotItemStr = "X";
                    } else {
                        voidSlotItemStr = String.valueOf(cover.voidingItemSlot);
                    }
                    button.displayString = voidSlotItemStr;
                    break;
                }
                case 2: {
                    int max = tile.getFluidInventorySize() - 1;
                    if (cover.voidingFluidSlot < max) {
                        cover.voidingFluidSlot++;
                    }
                    String voidSlotFluidStr;
                    if (cover.voidingFluidSlot == -2) {
                        voidSlotFluidStr = "*";
                    } else if (cover.voidingFluidSlot == -1) {
                        voidSlotFluidStr = "X";
                    } else {
                        voidSlotFluidStr = String.valueOf(cover.voidingFluidSlot);
                    }
                    button.displayString = voidSlotFluidStr;
                    break;
                }
            }
        }
        super.buttonClicked(button);
        if (EnvironmentHelper.isClientWorld()) {
            NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, TileEntityCoverable.CHANNEL_COVERS_START + cover.getDir().getSideNumber(), new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
        }
    }

    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        if (button == 1) {
            for (ButtonElement guibutton : this.buttons) {
                if (guibutton.mouseClicked(this.mc, x, y)) {
                    this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
                    action2Performed(guibutton);
                }
            }
        }
    }

    private void action2Performed(ButtonElement guibutton) {
        if (tile != null) {
            switch (guibutton.id) {
                case 1: {
                    if (cover.voidingItemSlot > -2) {
                        cover.voidingItemSlot--;
                        if (cover.voidingItemSlot == -2) {
                            PopupScreen popup = new PopupBuilder(this, 246)
                                    .withLabel("signalindustries.warning")
                                    .withMessageBox("warning", 128, "Setting a voiding slot to \"*\" will\n" + TextFormatting.RED + ">> REMOVE EVERY ITEM <<\n" + TextFormatting.WHITE + "in the inventory of the machine this cover\nis attached to.\n\nClick outside this popup to continue.", 44)
                                    .closeOnClickOut(0)
                                    .closeOnEnter(0)
                                    .closeOnEsc(0)
                                    .build();
                            Minecraft.getMinecraft().displayScreen(popup);
                            cover.active = false;
                        }
                    }
                    String voidSlotItemStr;
                    if (cover.voidingItemSlot == -2) {
                        voidSlotItemStr = "*";
                    } else if (cover.voidingItemSlot == -1) {
                        voidSlotItemStr = "X";
                    } else {
                        voidSlotItemStr = String.valueOf(cover.voidingItemSlot);
                    }
                    guibutton.displayString = voidSlotItemStr;
                    break;
                }
                case 2: {
                    if (cover.voidingFluidSlot > -2) {
                        cover.voidingFluidSlot--;
                        if (cover.voidingFluidSlot == -2) {
                            PopupScreen popup = new PopupBuilder(this, 246)
                                    .withLabel("signalindustries.warning")
                                    .withMessageBox("warning", 128, "Setting a voiding slot to \"*\" will\n" + TextFormatting.RED + ">> REMOVE EVERY FLUID <<\n" + TextFormatting.WHITE + "in the inventory of the machine this cover\nis attached to.\n\nClick outside this popup to continue.", 44)
                                    .closeOnClickOut(0)
                                    .closeOnEnter(0)
                                    .closeOnEsc(0)
                                    .build();
                            Minecraft.getMinecraft().displayScreen(popup);
                            cover.active = false;
                        }
                    }
                    String voidSlotFluidStr;
                    if (cover.voidingFluidSlot == -2) {
                        voidSlotFluidStr = "*";
                    } else if (cover.voidingFluidSlot == -1) {
                        voidSlotFluidStr = "X";
                    } else {
                        voidSlotFluidStr = String.valueOf(cover.voidingFluidSlot);
                    }
                    guibutton.displayString = voidSlotFluidStr;
                    break;
                }
            }
            if (EnvironmentHelper.isClientWorld()) {
                NetworkHandler.sendToServer(new PacketScreenAction(guibutton.id, 1, TileEntityCoverable.CHANNEL_COVERS_START + cover.getDir().getSideNumber(), new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
            }
        }
    }
}
