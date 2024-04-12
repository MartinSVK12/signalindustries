package sunsetsatellite.signalindustries.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.TextureFX;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryTrommel;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;
import org.lwjgl.opengl.GL11;

public class GuiRenderItemNoOverlay extends Gui {
    static ItemEntityRenderer itemRenderer = new ItemEntityRenderer();
    Minecraft mc;

    public GuiRenderItemNoOverlay(Minecraft mc) {
        this.mc = mc;
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot) {
        boolean hasDrawnSlotBackground = false;
        boolean discovered = true;
        Lighting.enableInventoryLight();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        if (slot != null) {
            int iconIndex = slot.getBackgroundIconIndex();
            if (iconIndex >= 0 && itemStack == null) {
                GL11.glDisable(2896);
                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                this.drawTexturedModalRect(x, y, iconIndex % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthItems, iconIndex / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthItems, 16, 16, TextureFX.tileWidthItems, 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthItems));
                GL11.glEnable(2896);
                hasDrawnSlotBackground = true;
            }
        }

        if (!hasDrawnSlotBackground) {
            GL11.glEnable(2929);
            itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? 1.0F : 0.0F, 1.0F);
            /*if (slot instanceof SlotGuidebook) {
                if (((SlotGuidebook)slot).recipe instanceof RecipeEntryTrommel && ((SlotGuidebook)slot).item != null && ((SlotGuidebook)slot).isOutput) {
                    WeightedRandomBag<WeightedRandomLootObject> loot = (WeightedRandomBag)((RecipeEntryTrommel)((SlotGuidebook)slot).recipe).getOutput();
                    int index = slot.id;
                    if (((SlotGuidebook)slot).recipeAmount > 8) {
                        index = slot.id + 9 * ((SlotGuidebook)slot).recipeIndex;
                    }

                    WeightedRandomLootObject lootObject = (WeightedRandomLootObject)loot.getEntries().get(index);
                    String s;
                    if (lootObject.isRandom()) {
                        s = String.format("%d-%d", lootObject.getMinYield(), lootObject.getMaxYield());
                    } else {
                        s = String.valueOf(lootObject.getFixedYield());
                    }

                    itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? s : "?", 1.0F);
                } else {
                    itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? null : "?", 1.0F);
                }
            } else {
                itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? null : "?", 1.0F);
            }*/

            GL11.glDisable(2929);
        }

        if (isSelected) {
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + 16, y + 16, -2130706433);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }

        GL11.glDisable(32826);
        Lighting.disable();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected) {
        this.render(itemStack, x, y, isSelected, (Slot)null);
    }

    public void render(ItemStack itemStack, int x, int y) {
        this.render(itemStack, x, y, false);
    }
}
