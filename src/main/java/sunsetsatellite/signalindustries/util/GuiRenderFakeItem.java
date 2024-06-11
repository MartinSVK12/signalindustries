package sunsetsatellite.signalindustries.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryTrommel;
import net.minecraft.core.item.ItemStack;

import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.core.util.IColorOverride;
import sunsetsatellite.catalyst.core.util.IFullbright;


public class GuiRenderFakeItem extends Gui
{

    //FIXME
    Minecraft mc;

    public GuiRenderFakeItem(Minecraft mc)
    {
        this.mc = mc;
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot)
    {
        boolean hasDrawnSlotBackground = false;
        boolean discovered = true;
        Lighting.enableInventoryLight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        if (slot != null) {
            discovered = slot.getIsDiscovered(this.mc.thePlayer);
            IconCoordinate iconIndex = TextureRegistry.getTexture(slot.getBackgroundIconId());
            if (iconIndex != null && itemStack == null) {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedIcon(x, y, 16, 16, iconIndex);
                GL11.glEnable(GL11.GL_LIGHTING);
                hasDrawnSlotBackground = true;
            }
        }

        if (!hasDrawnSlotBackground) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (itemStack != null) {
                BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
                ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
                if(itemModel instanceof ItemModelBlock){
                    BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemStack.itemID]);
                    ((IFullbright)blockModel).enableFullbright();
                    ((IColorOverride)blockModel).overrideColor(1,1,1,0.5f);
                }
                ((IFullbright)itemModel).enableFullbright();
                ((IColorOverride)itemModel).overrideColor(1,1,1,0.5f);
                itemModel.renderItemIntoGui(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? 1.0F : 0.0F, 1.0F);
                if (slot instanceof SlotGuidebook) {
                    if (((SlotGuidebook)slot).recipe instanceof RecipeEntryTrommel && ((SlotGuidebook)slot).item != null && ((SlotGuidebook)slot).isOutput) {
                        WeightedRandomBag<WeightedRandomLootObject> loot = ((RecipeEntryTrommel)((SlotGuidebook)slot).recipe).getOutput();
                        int index = slot.id;
                        if (((SlotGuidebook)slot).recipeAmount > 8) {
                            index = slot.id + 9 * ((SlotGuidebook)slot).recipeIndex;
                        }
                        WeightedRandomLootObject lootObject = loot.getEntries().get(index);
                        String s;
                        if (lootObject.isRandomYield()) {
                            s = String.format("%d-%d", lootObject.getMinYield(), lootObject.getMaxYield());
                        } else {
                            s = String.valueOf(lootObject.getFixedYield());
                        }
                        //itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? s : "?", 1.0F);
                    } else {
                        //itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? null : "?", 1.0F);
                    }

                } else {
                    //itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? null : "?", 1.0F);
                }
                ((IFullbright)itemModel).disableFullbright();
                ((IColorOverride)itemModel).overrideColor(1,1,1,1f);
                if(itemModel instanceof ItemModelBlock){
                    BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemStack.itemID]);
                    ((IFullbright)blockModel).disableFullbright();
                    ((IColorOverride)blockModel).overrideColor(1,1,1,1f);
                }
            }

            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        if (isSelected) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            this.drawRect(x, y, x + 16, y + 16, 0x80FFFFFF);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        Lighting.disable();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected)
    {
        render(itemStack, x, y, isSelected, null);
    }

    public void render(ItemStack itemStack, int x, int y)
    {
        render(itemStack, x, y, false);
    }
}
