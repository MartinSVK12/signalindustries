package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.menus.MenuBlueprint;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;
import sunsetsatellite.signalindustries.util.FakeItemElement;

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
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/blueprint.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
    public void drawAfterSlotAndButtonRendering(int mx, int my, float partialTick) {
        if (structure == null) return;
        Minecraft mc = Minecraft.getMinecraft();
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(80,80,900f);
        //.glEnable(.GL_DEPTH_TEST);

        float size = 7;

        rotation += 0.1f;
        if (rotation > 360) {
            rotation = 0;
        }

		GLRenderer.modelM4f().scale(size, -size, size);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(30),1,0,0);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(45),0,1,0);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(rotation),0,1,0);
		Lighting.enableLight();
        RenderMultiblockInGUI r = new RenderMultiblockInGUI();
        ArrayList<BlockInstance> blocks = structure.getBlocks();
        if (structure.getOrigin() != null) {
            blocks.add(structure.getOrigin());
        }
        r.render(blocks, 1);
		GLRenderer.popFrame();
		Lighting.disable();

        int color = 0xFFFFFFFF;
        drawStringCenteredShadow(fontRenderer, Catalyst.translateNameKey("container.signalindustries.blueprint"), 128, 6, color);
		drawStringCenteredShadow(fontRenderer, blocks.size() + " blocks.", 128, 150, color);

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
		int cx = (this.width - this.xSize) / 2;
		int cy = (this.height - this.ySize) / 2;
		List<Vec2i> slots = new ArrayList<>();
		boolean showDescription = DescriptionPromptEnum.showDescription();
        for (int k = 0; k < blocksCondensed.size(); k++) {
			int x = 160 + 18 * (k % maxSlotsInRow);
			int y = 24 + 18 * (k / maxSlotsInRow);
			guiRenderFakeItem.render(blocksCondensed.get(k), x, y, mx >= cx+x && mx <= cx+x + 18 && my >= cy+y && my <= cy+y + 18, null, true, 1);
			slots.add(new Vec2i(x, y));
		}
		for (int i = 0; i < slots.size(); i++) {
			Vec2i slot = slots.get(i);
			if (mx >= cx + slot.x && mx <= cx + slot.x + 18 && my >= cy + slot.y && my <= cy + slot.y + 18) {
				tooltipElement.render(tooltipElement.getTooltipText(blocksCondensed.get(i), showDescription), mx - cx, my - cy, 8, 0);
			}
		}
    }
}
