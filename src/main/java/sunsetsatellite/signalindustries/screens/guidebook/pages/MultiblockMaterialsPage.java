package sunsetsatellite.signalindustries.screens.guidebook.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.guidebook.*;
import net.minecraft.client.gui.guidebook.search.GuidebookPageSearch;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.util.helper.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.model.IColorOverride;
import sunsetsatellite.catalyst.multiblocks.Multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiblockMaterialsPage extends GuidebookPage {
    public final Multiblock multiblock;
    private final TooltipElement guiTooltip;
    private final ItemElement guiRenderItem;
    private static final Minecraft mc = Minecraft.getMinecraft();
    public List<SlotGuidebook> slots = new ArrayList<>();
    private static long ticks = 0;


    public MultiblockMaterialsPage(GuidebookSection section, Multiblock multiblock) {
        super(section);
        this.multiblock = multiblock;
        guiTooltip = new TooltipElement(mc);
        guiRenderItem = new ItemElement(mc);

        List<ItemStack> blocksUncondensed = multiblock
                .getBlocks()
                .stream()
                .map((B) -> new ItemStack(B.block, 1, B.meta == -1 ? 0 : B.meta))
                .collect(Collectors.toList());
        List<ItemStack> blocks = Catalyst.condenseItemList(blocksUncondensed);
        ItemStack origin = new ItemStack(multiblock.getOrigin().block, 1, multiblock.getOrigin().meta == -1 ? 0 : multiblock.getOrigin().meta);

        // Annoying special case for when the origin isn't that special
        boolean matched = false;
        for (ItemStack is : blocks) {
            if (origin.isItemEqual(is)) {
                is.stackSize += 1;
                matched = true;
                break;
            }
        }
        if (!matched) {
            blocks.add(origin);
        }

        int i = 0;
        int maxSlotsInRow = 7;
        for (ItemStack block : blocks) {
            slots.add(new SlotGuidebook(i, 18 + 18 * (i % maxSlotsInRow), 24 + 18 * (i / maxSlotsInRow), new RecipeSymbol(block), false, null));
            i++;
        }

    }


    @Override
    protected void renderForeground(TextureManager re, Font fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (multiblock != null) {
            drawStringCenteredNoShadow(fr, I18n.getInstance().translateNameKey(multiblock.translateKey), x + 158 / 2, y + 10, 0x000000);
        } else {
            drawStringCenteredNoShadow(fr, "No results :(", x + width / 2, y + height / 2, 0xFF808080);
        }
        SlotGuidebook mouseOverSlot = null;
        ticks++;
        for (SlotGuidebook slot : slots) {
            if (ticks > 100) {
                slot.showRandomItem();
                if (slots.get(slots.size() - 1) == slot) {
                    ticks = 0;
                }
            }
            drawSlot(x + slot.x - 1, y + slot.y - 1, 0xFFFFFFFF);
            if (getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) mouseOverSlot = slot;
            if (slot.item != null && slot.item.itemID < 16384 && (Blocks.getBlock(slot.item.itemID) == Blocks.FLUID_WATER_FLOWING || Blocks.getBlock(slot.item.itemID) == Blocks.FLUID_WATER_STILL) && mc.gameSettings.biomeWater.value) {
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Blocks.getBlock(slot.item.itemID));
                ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(slot.getItemStack().getItem());
                int waterColor = BlockColorDispatcher.getInstance().getDispatch(Blocks.FLUID_WATER_FLOWING).getWorldColor(mc.currentWorld, (int) mc.thePlayer.x, (int) mc.thePlayer.y, (int) mc.thePlayer.z);
                Color c = new Color().setARGB(waterColor);
                c.setRGBA(c.getRed(), c.getGreen(), c.getBlue(), 0x40);
                ((IColorOverride) blockModel).overrideColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

                guiRenderItem.render(slot.getItemStack(), x + slot.x, y + slot.y, mouseOverSlot == slot, slot);
                ((IColorOverride) blockModel).overrideColor(1, 1, 1, 1);

            } else {
                guiRenderItem.render(slot.getItemStack(), x + slot.x, y + slot.y, mouseOverSlot == slot, slot);
            }
        }
    }

    public boolean getIsMouseOverSlot(final Slot slot, int x, int y, int mouseX, int mouseY) {
        return mouseX >= x + slot.x - 1 && mouseX < x + slot.x + 16 + 1 && mouseY >= y + slot.y - 1 && mouseY < y + slot.y + 16 + 1;
    }

    @Override
    public boolean keyTyped(char c, int key, int x, int y, int mouseX, int mouseY) {
        super.keyTyped(c, key, x, y, mouseX, mouseY);
        if (mc.gameSettings.keyShowRecipe.isKeyboardKey(key)) {
            SlotGuidebook hoveringSlot = null;
            for (SlotGuidebook slot : slots) {
                if (getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) hoveringSlot = slot;
            }
            if (hoveringSlot != null) {
                if (hoveringSlot.hasItem()) {
                    String query = "r:" + hoveringSlot.getItemStack().getDisplayName() + "!";
                    GuidebookPageManager.searchQuery = SearchQuery.resolve(query);
                    GuidebookPageSearch.searchField.setText(query);
                    ScreenGuidebook.getPageManager().updatePages();
                    ScreenGuidebook.getPageManager().setCurrentPage(ScreenGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                    return true;
                }
            }
        } else if (mc.gameSettings.keyShowUsage.isKeyboardKey(key)) {
            SlotGuidebook hoveringSlot = null;
            for (SlotGuidebook slot : slots) {
                if (getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) hoveringSlot = slot;
            }
            if (hoveringSlot != null) {
                if (hoveringSlot.hasItem()) {
                    String query = "u:" + hoveringSlot.getItemStack().getDisplayName() + "!";
                    GuidebookPageManager.searchQuery = SearchQuery.resolve(query);
                    GuidebookPageSearch.searchField.setText(query);
                    ScreenGuidebook.getPageManager().updatePages();
                    ScreenGuidebook.getPageManager().setCurrentPage(ScreenGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void renderOverlay(TextureManager re, Font fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.renderOverlay(re, fr, x, y, mouseX, mouseY, partialTicks);
        SlotGuidebook mouseOverSlot = null;
        for (SlotGuidebook slot : slots) {
            if (getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) mouseOverSlot = slot;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
                String str = guiTooltip.getTooltipText(mouseOverSlot.getItemStack(), showDescription, mouseOverSlot);
                if (!str.isEmpty()) {
                    guiTooltip.render(str, mouseX, mouseY, 8, -8);
                }
            }
        }
    }


}
