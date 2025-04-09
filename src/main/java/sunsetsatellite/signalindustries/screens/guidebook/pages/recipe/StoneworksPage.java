package sunsetsatellite.signalindustries.screens.guidebook.pages.recipe;

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

import net.minecraft.core.achievement.Achievements;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.slot.Slot;

import net.minecraft.core.util.helper.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import sunsetsatellite.catalyst.core.util.model.IColorOverride;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoneworksPage
    extends GuidebookPage {
    public static final int RECIPES_PER_PAGE = 5; //used through reflection
    public List<RecipeEntryMachine> recipes;
    public List<SlotGuidebook> slots;
    public Map<RecipeEntryMachine,List<SlotGuidebook>> map;
    private final TooltipElement guiTooltip;
    private final ItemElement guiRenderItem;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static Player player;
    private static long ticks = 0;
    public StoneworksPage(GuidebookSection section, ArrayList<RecipeEntryMachine> recipes) {
        super(section);
        this.recipes = recipes;
        this.slots = new ArrayList<>();
        this.map = new HashMap<>();
        player = mc.thePlayer;
        player.addStat(Achievements.OPEN_GUIDEBOOK,1);
        guiTooltip = new TooltipElement(mc);
        guiRenderItem = new ItemElement(mc);

        for (RecipeEntryMachine recipe : recipes) {
            List<SlotGuidebook> recipeSlots = new ArrayList<>();
            List<RecipeSymbol> inputSymbol = new ArrayList<>();
            RecipeSymbol outputSymbol;
            for (RecipeExtendedSymbol symbol : recipe.getInput()) {
                if(symbol.getFluidStack() != null){
                    FluidStack fluidStack = symbol.getFluidStack();
                    inputSymbol.add(new RecipeSymbol(fluidStack.toItemStack()));
                } else {
                    inputSymbol.add(symbol.asNormalSymbol());
                }
            }
            List<ItemStack> acceptedMachines = recipe.parent.getMachine().resolve().stream().filter((S)->{
                Block<?> block = Blocks.getBlock(S.itemID);
                if (block != null && block.getLogic() instanceof ITiered) {
                    if(recipe.getData().thisTierOnly){
                        return ((ITiered) block.getLogic()).getTier() == recipe.getData().tier;
                    } else {
                        return ((ITiered) block.getLogic()).getTier().ordinal() >= recipe.getData().tier.ordinal();
                    }
                }
                return false;
            }).collect(Collectors.toList());

            outputSymbol = new RecipeSymbol(recipe.getOutput());
            recipeSlots.add(new SlotGuidebook(0, (width/2)-52, 36*(map.size()+1)-16, inputSymbol.get(0), false,recipe));
            recipeSlots.add(new SlotGuidebook(1, (width/2)-32, 36*(map.size()+1)-16, inputSymbol.get(1), false,recipe));
            recipeSlots.add(new SlotGuidebook(3,(width/2)+48, 36*(map.size()+1)-16,new RecipeSymbol(acceptedMachines),false,recipe));
            recipeSlots.add(new SlotGuidebook(2, (width/2)+24, 36*(map.size()+1)-16, outputSymbol, false,recipe));
            map.put(recipe,recipeSlots);
            slots.addAll(recipeSlots);
        }
    }

    @Override
    protected void renderForeground(TextureManager re, Font fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        drawStringCenteredNoShadow(fr, "Stoneworks", x+width - 158 / 2, y+5, 0xFF808080);
        if(recipes.isEmpty()){
            drawStringCenteredNoShadow(fr,"No recipes found :(" ,x+width/2,y+height/2,0xFF808080);
        }
        SlotGuidebook mouseOverSlot = null;
        ticks++;
        for (SlotGuidebook slot : slots) {

            if(ticks > 100) {
                slot.showRandomItem();
                if(slots.get(slots.size()-1) == slot){
                    ticks = 0;
                }
            }
            if(slot.index != 3){
                drawSlot(x+slot.x-1,y+slot.y-1,0xFFFFFFFF);
            }
            if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) mouseOverSlot = slot;
            if(slot.item != null && slot.item.itemID < 16384 && (Blocks.getBlock(slot.item.itemID) == Blocks.FLUID_WATER_FLOWING || Blocks.getBlock(slot.item.itemID) == Blocks.FLUID_WATER_STILL) && mc.gameSettings.biomeWater.value){
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Blocks.getBlock(slot.item.itemID));
                ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(slot.getItemStack().getItem());
                int waterColor = BlockColorDispatcher.getInstance().getDispatch(Blocks.FLUID_WATER_FLOWING).getWorldColor(mc.currentWorld, (int) mc.thePlayer.x, (int) mc.thePlayer.y, (int) mc.thePlayer.z);
                Color c = new Color().setARGB(waterColor);
                c.setRGBA(c.getRed(),c.getGreen(),c.getBlue(),0x40);
                ((IColorOverride)blockModel).overrideColor(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
                
                guiRenderItem.render(slot.getItemStack(),x+slot.x,y+slot.y,mouseOverSlot == slot,slot);
                ((IColorOverride)blockModel).overrideColor(1,1,1,1);
                
            } else {
                guiRenderItem.render(slot.getItemStack(),x+slot.x,y+slot.y,mouseOverSlot == slot,slot);
            }
        }
        for (int i = 1; i <= recipes.size(); i++) {
            RecipeEntryMachine recipe = recipes.get(i-1);
            List<SlotGuidebook> list = map.get(recipe);
            drawStringCenteredNoShadow(fr,recipe.getData().ticks+"t | ID: "+recipe.getData().id,x + list.get(list.size()-1).x - 20, y +  list.get(list.size()-1).y + 18,0xFF202020);
            drawStringCenteredNoShadow(fr,(int)(recipe.getData().cost*((float)recipe.getData().ticks/200.0f))+" sE",x + list.get(list.size()-1).x - 20, y +  list.get(list.size()-1).y + 26,0xFFCC0000);

            //drawTexturedModalRect( 90, 35, 22, 15);
        }
    }

    public boolean getIsMouseOverSlot(final Slot slot, int x, int y, int mouseX, int mouseY)
    {
        return mouseX >= x+slot.x - 1 && mouseX < x+slot.x + 16 + 1 && mouseY >= y+slot.y - 1 && mouseY < y+slot.y + 16 + 1;
    }

    @Override
    public void render(TextureManager re, Font fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.render(re, fr, x, y, mouseX, mouseY, partialTicks);

    }

    @Override
    protected void renderBackground(TextureManager re, int x, int y) {
        super.renderBackground(re, x, y);
        re.bindTexture(re.loadTexture("/gui/crafting.png"));
        for (int i = 1; i <= recipes.size(); i++) {
            RecipeEntryMachine recipe = recipes.get(i-1);
            List<SlotGuidebook> list = map.get(recipe);
            drawTexturedModalRect(x + list.get(list.size()-1).x - 32, y +  list.get(list.size()-1).y, 90, 35, 22, 15);
        }
    }

    @Override
    public boolean keyTyped(char c, int key, int x, int y, int mouseX, int mouseY) {
        super.keyTyped(c, key, x, y, mouseX, mouseY);
        if(mc.gameSettings.keyShowRecipe.isKeyboardKey(key)){
            SlotGuidebook hoveringSlot= null;
            for (SlotGuidebook slot : slots) {
                if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) hoveringSlot = slot;
            }
            if(hoveringSlot != null){
                if(hoveringSlot.hasItem()){
                    String query = "r:"+hoveringSlot.getItemStack().getDisplayName()+"!";
                    GuidebookPageManager.searchQuery = SearchQuery.resolve(query);
                    GuidebookPageSearch.searchField.setText(query);
                    ScreenGuidebook.getPageManager().updatePages();
                    ScreenGuidebook.getPageManager().setCurrentPage(ScreenGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                    return true;
                }
            }
        } else if (mc.gameSettings.keyShowUsage.isKeyboardKey(key)) {
            SlotGuidebook hoveringSlot= null;
            for (SlotGuidebook slot : slots) {
                if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) hoveringSlot = slot;
            }
            if(hoveringSlot != null) {
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
            if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) mouseOverSlot = slot;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (mouseOverSlot != null && mouseOverSlot.hasItem())
            {
                boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
                String str = guiTooltip.getTooltipText(mouseOverSlot.getItemStack(), showDescription, mouseOverSlot);
                if(!str.isEmpty())
                {
                    guiTooltip.render(str, mouseX, mouseY, 8, -8);
                }
            }
        }
    }
}
