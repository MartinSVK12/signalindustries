package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.menus.MenuBlueprint;
import sunsetsatellite.signalindustries.render.FakeItemElement;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScreenBlueprint extends ScreenFluid implements IExtendedScreenDraw {

    public int backpackSlotIndex;
    public boolean isArmor;
    public ItemStack blueprint;
    public Player player;
    public Structure structure;

    public float rotation = 0;

    public ScreenBlueprint(ContainerInventory inventoryPlayer, int slotIndex, boolean isArmor) {
        super(new MenuBlueprint(inventoryPlayer, slotIndex, isArmor));
        this.backpackSlotIndex = slotIndex;
        this.isArmor = isArmor;
        this.player = inventoryPlayer.player;
        this.blueprint = inventoryPlayer.getItem(slotIndex);
        ySize = 168;
        xSize = 256;
        this.structure = SignalIndustries.getStructureFromBlueprint(blueprint, inventoryPlayer.player.world);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/blueprint.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        if (structure == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(80, 80, 900F);

        double size = 7;

        rotation += 0.1f;
        if (rotation > 360) {
            rotation = 0;
        }

        GL11.glScaled(size, -size, size);
        GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        Lighting.enableLight();
        GL11.glTranslatef(0.0F, 0, 0.0F);
        RenderMultiblockInGUI r = new RenderMultiblockInGUI();
        ArrayList<BlockInstance> blocks = structure.getBlocks();
        if (structure.getOrigin() != null) {
            blocks.add(structure.getOrigin());
        }
        r.doRender(blocks, mc.textureManager, mc.font, 0, 0, 0, 1);
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        int color = 0xFFFFFFFF;
        font.drawCenteredString(I18n.getInstance().translateNameKey("container.signalindustries.blueprint"), 128, 6, color);
        font.drawCenteredString(blocks.size() + " blocks.", 128, 150, color);

        FakeItemElement guiRenderFakeItem = new FakeItemElement(mc);
        List<ItemStack> blocksUncondensed = blocks
                .stream()
                .map((B) -> {
                    ItemStack stack = new ItemStack(B.block, 1, B.meta == -1 ? 0 : B.meta);
                    if (!stack.getHasSubtypes()) {
                        stack.setMetadata(0);
                    }
                    return stack;
                })
                .collect(Collectors.toList());
        List<ItemStack> blocksCondensed = Catalyst.condenseItemList(blocksUncondensed);

        int maxSlotsInRow = 5;
        for (int k = 0; k < blocksCondensed.size(); k++) {
            guiRenderFakeItem.render(blocksCondensed.get(k), 160 + 18 * (k % maxSlotsInRow), 24 + 18 * (k / maxSlotsInRow), false, null, true, 1);
        }
    }
}
