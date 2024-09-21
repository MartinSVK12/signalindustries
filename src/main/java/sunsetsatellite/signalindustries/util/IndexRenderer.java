package sunsetsatellite.signalindustries.util;

import com.b100.utils.StringUtils;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.gui.guidebook.GuiGuidebook;
import net.minecraft.client.gui.guidebook.GuidebookSections;
import net.minecraft.client.gui.guidebook.PageManager;
import net.minecraft.client.gui.guidebook.SearchableGuidebookSection;
import net.minecraft.client.gui.guidebook.search.SearchPage;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import net.minecraft.core.util.collection.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import sunsetsatellite.catalyst.core.util.Vec2i;
import sunsetsatellite.signalindustries.api.impl.catalyst.multipart.SIMultipartIndexPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class IndexRenderer {

    public static int page = 0;
    public static int pageMax;
    public static int maxHorizonalItems = 9;
    public static int maxVerticalItems = 16;
    public static int maxItemsPerPage;
    public static GuiTooltip tooltip;
    private static final DrawUtil draw = new DrawUtil();
    private static int debounce = 0;
    public static HashMap<IdMetaPair,ItemStack> items = new HashMap<>();
    private static HashSet<Map.Entry<IdMetaPair, ItemStack>> sortedEntries;
    private static boolean initialized = false;

    public static void init() {
        for (ItemStack stack : ContainerPlayerCreative.creativeItems) {
            if(stack == null) continue;
            items.put(new IdMetaPair(stack.itemID,stack.getMetadata()),stack);
        }
        for (int id = 0; id < 32768; id++) {
            final int finalId = id;
            items.computeIfAbsent(new IdMetaPair(id,0),(P)-> Item.itemsList[finalId] == null ? null : Item.itemsList[finalId].getDefaultStack());
        }
        new SIMultipartIndexPlugin().add(items);
        sortedEntries = items.entrySet().stream().sorted((E1,E2)->{
            if(E1.getKey().id == E2.getKey().id){
                return Integer.compare(E1.getKey().meta,E2.getKey().meta);
            } else {
                return Integer.compare(E1.getKey().id,E2.getKey().id);
            }
        }).collect(Collectors.toCollection(LinkedHashSet::new));
        initialized = true;
    }

    public static void drawScreen(Minecraft mc, int mouseX, int mouseY, int width, int height, float partialTick){
        if(!initialized) {
            init();
            return;
        }
        if(debounce > 0) debounce--;
        boolean showNull = false;

        if(mc.thePlayer.gamemode == Gamemode.creative){
            maxHorizonalItems = 9;
        } else {
            maxHorizonalItems = 12;
        }

        maxItemsPerPage = maxHorizonalItems * maxVerticalItems;
        pageMax = Math.floorDiv(items.size(), maxItemsPerPage);

        if(tooltip == null){ tooltip = new GuiTooltip(mc); }

        int xOffset = 0;
        int yOffset = 1;

        int beginIndex = Math.max(0,Math.min(items.size(), page * maxItemsPerPage));
        int endIndex = Math.max(1,Math.min(items.size(), (page * maxItemsPerPage) + maxItemsPerPage));

        Set<Map.Entry<IdMetaPair, ItemStack>> itemEntries = sortedEntries.stream().skip(beginIndex).limit(maxItemsPerPage).collect(Collectors.toCollection(LinkedHashSet::new));
        //int[] i = items.entrySet().subSet(beginIndex,endIndex).stream().mapToInt((n)->n).toArray();
        List<Pair<Vec2i,ItemStack>> itemPositions = new ArrayList<>();

        int n = 0;
        for (Map.Entry<IdMetaPair, ItemStack> entry : itemEntries) {
            if (xOffset >= maxHorizonalItems) {
                yOffset++;
                xOffset = 0;
            }
            ItemStack conv = entry.getValue();
            if (conv != null) {
                int x;
                if(mc.thePlayer.gamemode == Gamemode.creative){
                   x = width - (width / 4) + (xOffset * 16) - 4 + (2 * xOffset);
                } else {
                   x = width - (width / 3) + (xOffset * 16) - 4 + (2 * xOffset);
                }
                int y = 8 + (yOffset * 16) + (2 * yOffset);;
                ItemRenderHelper.renderItemStack(conv, x, y, 1, 1, 1, 1);
                itemPositions.add(Pair.of(new Vec2i(x, y), entry.getValue()));
                xOffset++;
            } else {
                int x;
                if(mc.thePlayer.gamemode == Gamemode.creative){
                    x = width - (width / 4) + (xOffset * 16) - 4 + (2 * xOffset);
                } else {
                    x = width - (width / 3) + (xOffset * 16) - 4 + (2 * xOffset);
                }
                int y = 8 + (yOffset * 16) + (2 * yOffset);;
                ItemRenderHelper.renderItemStack(Block.pistonMoving.getDefaultStack(), x, y, 1, 1, 1, 1);
                itemPositions.add(Pair.of(new Vec2i(x, y), entry.getValue()));
                xOffset++;
            }
            n++;
        }

        for (Pair<Vec2i, ItemStack> pair : itemPositions) {
            ItemStack stack = pair.getRight();
            int x = pair.getLeft().x;
            int y = pair.getLeft().y;
            if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16) {
                boolean control = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
                String tooltipText;
                if(stack.getItem() != null){
                    try {
                        if(StringUtils.isStringEmpty(TextFormatting.removeAllFormatting(stack.getDisplayName()))){
                            tooltipText = "<empty string>" + tooltip.getTooltipText(stack, control);
                        } else {
                            tooltipText = tooltip.getTooltipText(stack, control);
                        }
                    } catch (NullPointerException npe){
                        tooltipText = "<tooltip error> #"+pair.getRight().itemID+":"+pair.getRight().getMetadata()+"\n"+TextFormatting.LIGHT_GRAY+stack.getItemKey();
                    }

                    if(mc.thePlayer.gamemode == Gamemode.creative && debounce <= 0){
                        if(Mouse.isButtonDown(0)){
                            debounce = 20;
                            mc.thePlayer.inventory.insertItem(stack.copy(),false);
                        } else if (Mouse.isButtonDown(1)) {
                            debounce = 20;
                            mc.thePlayer.inventory.insertItem(new ItemStack(stack.itemID,64,stack.getMetadata(),stack.getData()),false);
                        }
                    }
                    if(mc.gameSettings.keyShowRecipe.isPressed()){
                        SearchQuery oldQuery = PageManager.searchQuery;
                        String query = "r:"+stack.getDisplayName()+"!";
                        PageManager.searchQuery = SearchQuery.resolve(query);
                        SearchPage.searchField.setText(query);
                        GuiGuidebook.getPageManager().updatePages();
                        if (!((SearchableGuidebookSection) GuidebookSections.CRAFTING).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                        } else if (!((SearchableGuidebookSection) GuidebookSections.FURNACE).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.FURNACE), true);
                        } else if (!((SearchableGuidebookSection) GuidebookSections.BLAST_FURNACE).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.BLAST_FURNACE), true);
                        } else if (!((SearchableGuidebookSection) GuidebookSections.TROMMEL).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.TROMMEL), true);
                        }
                        if(((SearchableGuidebookSection) GuidebookSections.CRAFTING).searchPages(PageManager.searchQuery).isEmpty()
                                && ((SearchableGuidebookSection) GuidebookSections.FURNACE).searchPages(PageManager.searchQuery).isEmpty()
                                && ((SearchableGuidebookSection) GuidebookSections.BLAST_FURNACE).searchPages(PageManager.searchQuery).isEmpty()
                                && ((SearchableGuidebookSection) GuidebookSections.TROMMEL).searchPages(PageManager.searchQuery).isEmpty()
                                && !isModded(stack)
                        ) {
                            if(oldQuery != null) {
                                SearchPage.searchField.setText(oldQuery.rawQuery);
                                PageManager.searchQuery = oldQuery;
                                GuiGuidebook.getPageManager().updatePages();
                            }
                        } else {
                            mc.thePlayer.closeScreen();
                            mc.thePlayer.displayGUIGuidebook();
                        }
                    } else if (mc.gameSettings.keyShowUsage.isPressed()) {
                        SearchQuery oldQuery = PageManager.searchQuery;
                        String query = "u:"+stack.getDisplayName()+"!";
                        PageManager.searchQuery = SearchQuery.resolve(query);
                        SearchPage.searchField.setText(query);
                        GuiGuidebook.getPageManager().updatePages();
                        if (!((SearchableGuidebookSection) GuidebookSections.CRAFTING).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                        } else if (!((SearchableGuidebookSection) GuidebookSections.FURNACE).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.FURNACE), true);
                        } else if (!((SearchableGuidebookSection) GuidebookSections.BLAST_FURNACE).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.BLAST_FURNACE), true);
                        } else if (!((SearchableGuidebookSection) GuidebookSections.TROMMEL).searchPages(PageManager.searchQuery).isEmpty()) {
                            GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.TROMMEL), true);
                        }
                        if(((SearchableGuidebookSection) GuidebookSections.CRAFTING).searchPages(PageManager.searchQuery).isEmpty()
                                && ((SearchableGuidebookSection) GuidebookSections.FURNACE).searchPages(PageManager.searchQuery).isEmpty()
                                && ((SearchableGuidebookSection) GuidebookSections.BLAST_FURNACE).searchPages(PageManager.searchQuery).isEmpty()
                                && ((SearchableGuidebookSection) GuidebookSections.TROMMEL).searchPages(PageManager.searchQuery).isEmpty()
                                && !isModded(stack)
                        ) {
                            if(oldQuery != null){
                                SearchPage.searchField.setText(oldQuery.rawQuery);
                                PageManager.searchQuery = oldQuery;
                                GuiGuidebook.getPageManager().updatePages();
                            }
                        } else {
                            mc.thePlayer.closeScreen();
                            mc.thePlayer.displayGUIGuidebook();
                        }
                    }
                } else {
                    tooltipText = "<null "+(pair.getRight().itemID < 16384 ? "block>": "item>")+" #"+pair.getRight().itemID+":0";
                }
                if(pair.getRight().itemID == 0){
                    tooltipText = "Air";
                }

                draw.drawGradientRect(x,y,x+16,y+16,0x80FFFFFF,0x80FFFFFF);

                tooltip.render(tooltipText, mouseX, mouseY, 8, -8);
            }
        }

        int strX = mc.thePlayer.gamemode == Gamemode.creative ? width - (width / 8) : width - (width / 6);
        draw.drawStringCentered(mc.fontRenderer,String.format("Index | Page: %d/%d",page+1,pageMax+1),strX,8,0XFFFFFFFF);
    }

    public static void keyTyped(char c, int i, int mouseX, int mouseY) {
        if(c == Keyboard.KEY_ADD || c == Keyboard.KEY_RIGHT){
            if(page < pageMax) page++;
        } else if (c == Keyboard.KEY_SUBTRACT || c == Keyboard.KEY_LEFT) {
            if(page > 0) page--;
        }
    }

    public static boolean isModded(ItemStack stack){
        String key = stack.getItemKey();
        String[] strings = key.split("\\.");
        if(strings.length >= 2){
            String id = strings[1];
            for (ModContainer mod : FabricLoaderImpl.INSTANCE.getAllMods()) {
                if(mod.getMetadata().getId().contains(id)){
                    return true;
                }
            }
        }
        return false;
    }
}
