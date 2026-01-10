//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sunsetsatellite.signalindustries.screens.guidebook.pages.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.guidebook.*;
import net.minecraft.client.gui.guidebook.search.GuidebookPageSearch;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryTrommel;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BonsaiPotPage extends RecipePage<RecipeEntryMachineMultiOutput> {
    public static final int RECIPES_PER_PAGE = 3;
    public List<SlotGuidebook> slots;
    public Map<RecipeEntryMachineMultiOutput, List<SlotGuidebook>> map;
    private final TooltipElement tooltipElement;
    private final ItemElement itemElement;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static long ticks = 0L;
    private static long ticks2 = 0L;

    public BonsaiPotPage(GuidebookSection section, ArrayList<RecipeEntryMachineMultiOutput> recipes) {
        super(section);
        this.recipes = recipes;
        this.slots = new ArrayList();
        this.map = new HashMap();
        this.tooltipElement = new TooltipElement(mc);
        this.itemElement = new ItemElement(mc);
        int recipeAmount = 0;
        int yOffset = 20;

        for(RecipeEntryMachineMultiOutput recipe : recipes) {
            List<SlotGuidebook> recipeSlots = new ArrayList();
            int slotsAmount = Math.min(4,recipe.getOutput().length);

            for(int i = 0; i < slotsAmount; ++i) {
                recipeSlots.add((new SlotGuidebook(i, 81 + 20 * (i % 2), 1 + 20 * (i / 2 + recipeAmount * 2) + yOffset, recipe.getOutput()[i].asSymbol(), false, recipe)).setAsOutput());
                /*if (((WeightedRandomBag)recipe.getOutput()).getEntries().size() > i) {
                    ItemStack stack = ((WeightedRandomLootObject)((WeightedRandomBag)recipe.getOutput()).getEntries().get(i)).getItemStack();
                    recipeSlots.add((new SlotGuidebook(i, 81 + 20 * (i % 3), 1 + 20 * (i / 3 + recipeAmount * 3) + yOffset, new RecipeSymbol(stack), false, recipe)).setAsOutput());
                } else {
                    recipeSlots.add((new SlotGuidebook(i, 81 + 20 * (i % 3), 1 + 20 * (i / 3 + recipeAmount * 3) + yOffset, null, false, recipe)).setAsOutput());
                }*/
            }

            int centerY = (recipeSlots.get(recipeSlots.size() - 1).y + recipeSlots.get(0).y) / 2;
            recipeSlots.add(new SlotGuidebook(slotsAmount, 19, centerY, recipe.getInput()[0].asNormalSymbol(), false, recipe));
            if(recipe.getInput().length > 1){
                recipeSlots.add(new SlotGuidebook(slotsAmount+1, 19, centerY + 20, recipe.getInput()[1].asNormalSymbol(), false, recipe));
            }
            this.map.put(recipe, recipeSlots);
            this.slots.addAll(recipeSlots);
            yOffset += 4;
            ++recipeAmount;
        }

    }

    public void onTick() {
        ++ticks;

        for(SlotGuidebook slot : this.slots) {
            if (ticks > 20L) {
                slot.showRandomItem();
                if (this.slots.get(this.slots.size() - 1) == slot) {
                    ticks = 0L;
                }
            }
        }

        ++ticks2;
        if (ticks2 > 25L) {
            ticks2 = 0L;

            for(SlotGuidebook slot : this.slots) {
                if (slot.recipe instanceof RecipeEntryTrommel) {
                    RecipeSymbol input = (RecipeSymbol)slot.recipe.getInput();
                    if (!input.matches(slot.getItemStack()) && slot.recipeAmount > 8) {
                        int recipeIndexMax = Math.round((float)slot.recipeAmount / 9.0F);
                        if (slot.recipeIndex >= recipeIndexMax) {
                            slot.recipeIndex = 0;
                        } else {
                            ++slot.recipeIndex;
                        }

                        WeightedRandomBag<WeightedRandomLootObject> loot = (WeightedRandomBag)slot.recipe.getOutput();
                        int index = slot.index + 9 * slot.recipeIndex;
                        if (index > slot.recipeAmount) {
                            slot.item = null;
                        } else {
                            slot.item = loot.getEntries().get(index).getItemStack();
                        }
                    }
                }
            }
        }

    }

    protected void renderForeground(TextureManager re, Font fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        drawStringCenteredNoShadow(fr, "Bonsai Pot", x+width - 158 / 2, y+5, 0xFF808080);

        if (this.recipes.isEmpty()) {
            this.drawStringCenteredNoShadow(fr, I18n.getInstance().translateKey("guidebook.section.search.error.no_recipes"), x + 79, y + 110, -8355712);
        }

        SlotGuidebook mouseOverSlot = null;

        for(SlotGuidebook slot : this.slots) {
            this.drawSlot(x + slot.x - 1, y + slot.y - 1, -1);
            if (this.getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) {
                mouseOverSlot = slot;
            }

            this.itemElement.render(slot.getItemStack(), x + slot.x, y + slot.y, mouseOverSlot == slot, slot);
        }

    }

    public boolean getIsMouseOverSlot(Slot slot, int x, int y, int mouseX, int mouseY) {
        return mouseX >= x + slot.x - 1 && mouseX < x + slot.x + 16 + 1 && mouseY >= y + slot.y - 1 && mouseY < y + slot.y + 16 + 1;
    }

    public boolean keyTyped(char c, int key, int x, int y, int mouseX, int mouseY) {
        super.keyTyped(c, key, x, y, mouseX, mouseY);
        if (mc.gameSettings.keyShowRecipe.isKeyboardKey(key)) {
            SlotGuidebook hoveringSlot = null;

            for(SlotGuidebook slot : this.slots) {
                if (this.getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) {
                    hoveringSlot = slot;
                }
            }

            if (hoveringSlot != null && hoveringSlot.hasItem()) {
                String query = "r:" + hoveringSlot.getItemStack().getDisplayName() + "!";
                GuidebookPageManager.searchQuery = SearchQuery.resolve(query);
                GuidebookPageSearch.searchField.setText(query);
                ScreenGuidebook.getPageManager().updatePages();
                ScreenGuidebook.getPageManager().setCurrentPage(ScreenGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                return true;
            }
        } else if (mc.gameSettings.keyShowUsage.isKeyboardKey(key)) {
            SlotGuidebook hoveringSlot = null;

            for(SlotGuidebook slot : this.slots) {
                if (this.getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) {
                    hoveringSlot = slot;
                }
            }

            if (hoveringSlot != null && hoveringSlot.hasItem()) {
                String query = "u:" + hoveringSlot.getItemStack().getDisplayName() + "!";
                GuidebookPageManager.searchQuery = SearchQuery.resolve(query);
                GuidebookPageSearch.searchField.setText(query);
                ScreenGuidebook.getPageManager().updatePages();
                ScreenGuidebook.getPageManager().setCurrentPage(ScreenGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                return true;
            }
        }

        return false;
    }

    public void render(TextureManager re, Font fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.render(re, fr, x, y, mouseX, mouseY, partialTicks);
    }

    protected void renderBackground(TextureManager re, int x, int y) {
        super.renderBackground(re, x, y);
        re.bindTexture(re.loadTexture("/assets/minecraft/textures/gui/container/guidebook/guidebook.png"));

        for(int i = 1; i <= this.recipes.size(); ++i) {
            RecipeEntryMachineMultiOutput recipe = this.recipes.get(i - 1);
            List<SlotGuidebook> list = this.map.get(recipe);
            this.drawTexturedModalRect(x + list.get(list.size() - 1).x + 25, y + list.get(list.size() - 1).y, 234, 0, 22, 15);
        }

    }

    protected void renderOverlay(TextureManager re, Font fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.renderOverlay(re, fr, x, y, mouseX, mouseY, partialTicks);
        SlotGuidebook mouseOverSlot = null;

        for(SlotGuidebook slot : this.slots) {
            if (this.getIsMouseOverSlot(slot, x, y, mouseX, mouseY)) {
                mouseOverSlot = slot;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (mouseOverSlot != null && mouseOverSlot.hasItem()) {
                boolean showDescription = DescriptionPromptEnum.showDescription(mc);
                String str = this.tooltipElement.getTooltipText(mouseOverSlot.getItemStack(), showDescription, mouseOverSlot);
                if (!str.isEmpty()) {
                    this.tooltipElement.render(str, mouseX, mouseY, 8, -8);
                }
            }
        }

    }
}
