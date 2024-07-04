package sunsetsatellite.signalindustries.util;

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
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.collection.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import sunsetsatellite.catalyst.core.util.Vec2i;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IndexRenderer {

    public static int page = 0;
    public static int pageMax;
    public static int maxHorizonalItems = 9;
    public static int maxVerticalItems = 19;
    public static int maxItemsPerPage;
    public static GuiTooltip tooltip;
    private static final DrawUtil draw = new DrawUtil();
    private static int debounce = 0;

    public static void drawScreen(Minecraft mc, int mouseX, int mouseY, int width, int height, float partialTick){
        if(debounce > 0) debounce--;
        boolean showNull = false;

        if(mc.thePlayer.gamemode == Gamemode.creative){
            maxHorizonalItems = 9;
        } else {
            maxHorizonalItems = 12;
        }

        List<Integer> intList;

        if(!showNull){
            intList = IntStream.range(0,32768).filter((n)->{
                IItemConvertible conv;
                if(n < 16384){
                    conv = Block.getBlock(n);
                } else {
                    conv = Item.itemsList[n];
                }
                return conv != null;
            }).boxed().collect(Collectors.toList());
        } else {
            intList = IntStream.range(0,32768).boxed().collect(Collectors.toList());
        }

        maxItemsPerPage = maxHorizonalItems * maxVerticalItems;
        pageMax = Math.floorDiv(intList.size(), maxItemsPerPage);

        if(tooltip == null){ tooltip = new GuiTooltip(mc); }

        int xOffset = 0;
        int yOffset = 1;

        int beginIndex = Math.max(0,Math.min(intList.size(), page * maxItemsPerPage));
        int endIndex = Math.max(1,Math.min(intList.size(), (page * maxItemsPerPage) + maxItemsPerPage));

        int[] i = intList.subList(beginIndex,endIndex).stream().mapToInt((n)->n).toArray();
        List<Pair<Vec2i,Integer>> data = new ArrayList<>();

        for (int n : i) {
            if (xOffset >= maxHorizonalItems) {
                yOffset++;
                xOffset = 0;
            }
            IItemConvertible conv;
            if(n < 16384){
                conv = Block.getBlock(n);
            } else {
                conv = Item.itemsList[n];
            }
            if (conv != null) {
                int x;
                if(mc.thePlayer.gamemode == Gamemode.creative){
                   x = width - (width / 4) + (xOffset * 16) - 4 + (2 * xOffset);
                } else {
                   x = width - (width / 3) + (xOffset * 16) - 4 + (2 * xOffset);
                }
                int y = 8 + (yOffset * 16);
                ItemRenderHelper.renderItemStack(conv.getDefaultStack(), x, y, 1, 1, 1, 1);
                data.add(Pair.of(new Vec2i(x, y), n));
                xOffset++;
            } else {
                int x;
                if(mc.thePlayer.gamemode == Gamemode.creative){
                    x = width - (width / 4) + (xOffset * 16) - 4 + (2 * xOffset);
                } else {
                    x = width - (width / 3) + (xOffset * 16) - 4 + (2 * xOffset);
                }
                int y = 8 + (yOffset * 16);
                ItemRenderHelper.renderItemStack(Block.pistonMoving.getDefaultStack(), x, y, 1, 1, 1, 1);
                data.add(Pair.of(new Vec2i(x, y), n));
                xOffset++;
            }
        }

        for (Pair<Vec2i, Integer> pair : data) {
            IItemConvertible conv;
            if(pair.getRight() < 16384){
                conv = Block.getBlock(pair.getRight());
            } else {
                conv = Item.itemsList[pair.getRight()];
            }

            int x = pair.getLeft().x;
            int y = pair.getLeft().y;
            if (mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16) {
                boolean control = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
                String tooltipText;
                if(conv != null){
                    tooltipText = tooltip.getTooltipText(conv.getDefaultStack(), control);
                    if(mc.thePlayer.gamemode == Gamemode.creative && debounce <= 0){
                        if(Mouse.isButtonDown(0)){
                            debounce = 20;
                            mc.thePlayer.inventory.insertItem(conv.getDefaultStack(),false);
                        } else if (Mouse.isButtonDown(1)) {
                            debounce = 20;
                            mc.thePlayer.inventory.insertItem(new ItemStack(conv,64),false);
                        }
                    }
                    if(mc.gameSettings.keyShowRecipe.isPressed()){
                        SearchQuery oldQuery = PageManager.searchQuery;
                        String query = "r:"+conv.getDefaultStack().getDisplayName()+"!";
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
                                && !isModded(conv.getDefaultStack())
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
                        String query = "u:"+conv.getDefaultStack().getDisplayName()+"!";
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
                                && !isModded(conv.getDefaultStack())
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
                    tooltipText = "null "+(pair.getRight() < 16384 ? "block": "item")+" #"+pair.getRight()+":0";
                }
                if(pair.getRight() == 0){
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
        if(c == 43){
            if(page < pageMax) page++;
        } else if (c == 45) {
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
