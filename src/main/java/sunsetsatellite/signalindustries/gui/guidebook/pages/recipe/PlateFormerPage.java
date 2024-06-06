package sunsetsatellite.signalindustries.gui.guidebook.pages.recipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.gui.guidebook.*;
import net.minecraft.client.gui.guidebook.search.SearchPage;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.achievement.AchievementList;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;
import net.minecraft.core.util.helper.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.IColorOverride;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlateFormerPage
    extends GuidebookPage {
    public static final int RECIPES_PER_PAGE = 6;
    public List<RecipeEntryMachine> recipes;
    public List<SlotGuidebook> slots;
    public Map<RecipeEntryMachine,List<SlotGuidebook>> map;
    private final GuiTooltip guiTooltip;
    private final GuiRenderItem guiRenderItem;
    private static final Minecraft mc = Minecraft.getMinecraft(GuidebookPage.class);
    private static EntityPlayer player;
    private static long ticks = 0;
    public PlateFormerPage(GuidebookSection section, ArrayList<RecipeEntryMachine> recipes) {
        super(section);
        this.recipes = recipes;
        this.slots = new ArrayList<>();
        this.map = new HashMap<>();
        player = mc.thePlayer;
        player.addStat(AchievementList.OPEN_GUIDEBOOK,1);
        guiTooltip = new GuiTooltip(mc);
        guiRenderItem = new GuiRenderItem(mc);

        for (RecipeEntryMachine recipe : recipes) {
            List<SlotGuidebook> recipeSlots = new ArrayList<>();
            RecipeSymbol inputSymbol;
            RecipeSymbol outputSymbol;
            if(recipe.getInput()[0].getFluidStack() != null){
                FluidStack fluidStack = recipe.getInput()[0].getFluidStack();
                inputSymbol = new RecipeSymbol(new ItemStack(fluidStack.liquid,fluidStack.amount));
            } else {
                inputSymbol = recipe.getInput()[0].asNormalSymbol();
            }
            List<ItemStack> acceptedMachines = recipe.parent.getMachine().resolve().stream().filter((S)->{
                Block block = Block.getBlock(S.itemID);
                if(block instanceof ITiered){
                    if(recipe.getData().thisTierOnly){
                        return ((ITiered) block).getTier() == recipe.getData().tier;
                    } else {
                        return ((ITiered) block).getTier().ordinal() >= recipe.getData().tier.ordinal();
                    }
                }
                return false;
            }).collect(Collectors.toList());

            outputSymbol = new RecipeSymbol(recipe.getOutput());
            recipeSlots.add(new SlotGuidebook(0, (width/2)-32, 32*(map.size()+1)-16, inputSymbol, false,recipe));
            recipeSlots.add(new SlotGuidebook(2,(width/2)+48, 32*(map.size()+1)-16,new RecipeSymbol(acceptedMachines),false,recipe));
            recipeSlots.add(new SlotGuidebook(1, (width/2)+24, 32*(map.size()+1)-16, outputSymbol, false,recipe));
            map.put(recipe,recipeSlots);
            slots.addAll(recipeSlots);
        }
    }

    @Override
    protected void renderForeground(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        drawStringCenteredNoShadow(fr, "Plate Former", x+width - 158 / 2, y+5, 0xFF808080);
        if(recipes.isEmpty()){
            drawStringCenteredNoShadow(fr,"No recipes found :(" ,x+width/2,y+height/2,0xFF808080);
        }
        SlotGuidebook mouseOverSlot = null;
        ticks++;
        for (SlotGuidebook slot : slots) {
            if(slot.item != null){
                slot.setDiscovered(mc.statsCounter.readStat(StatList.pickUpItemStats[slot.item.itemID]) > 0);
            }
            if(mc.thePlayer.gamemode == Gamemode.creative) slot.setDiscovered(true);
            if(ticks > 100) {
                slot.showRandomItem();
                if(slots.get(slots.size()-1) == slot){
                    ticks = 0;
                }
            }
            if(slot.id != 2){
                drawSlot(re,x+slot.xDisplayPosition-1,y+slot.yDisplayPosition-1,0xFFFFFFFF);
            }
            if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) mouseOverSlot = slot;
            if(slot.item != null && slot.item.itemID < 16384 && (Block.getBlock(slot.item.itemID) == Block.fluidWaterFlowing || Block.getBlock(slot.item.itemID) == Block.fluidWaterStill) && mc.gameSettings.biomeWater.value){
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Block.getBlock(slot.item.itemID));
                ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(slot.getStack().getItem());
                int waterColor = BlockColorDispatcher.getInstance().getDispatch(Block.fluidWaterFlowing).getWorldColor(mc.theWorld, (int) mc.thePlayer.x, (int) mc.thePlayer.y, (int) mc.thePlayer.z);
                Color c = new Color().setARGB(waterColor);
                c.setRGBA(c.getRed(),c.getGreen(),c.getBlue(),0x40);
                ((IColorOverride)blockModel).overrideColor(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
                ((IColorOverride)itemModel).overrideColor(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
                guiRenderItem.render(slot.getStack(),x+slot.xDisplayPosition,y+slot.yDisplayPosition,mouseOverSlot == slot,slot);
                ((IColorOverride)blockModel).overrideColor(1,1,1,1);
                ((IColorOverride)itemModel).overrideColor(1,1,1,1);
            } else {
                guiRenderItem.render(slot.getStack(),x+slot.xDisplayPosition,y+slot.yDisplayPosition,mouseOverSlot == slot,slot);
            }
        }
        for (int i = 1; i <= recipes.size(); i++) {
            RecipeEntryMachine recipe = recipes.get(i-1);
            List<SlotGuidebook> list = map.get(recipe);
            drawStringCenteredNoShadow(fr,recipe.getData().ticks+"t",x + list.get(list.size()-1).xDisplayPosition - 76, y +  list.get(list.size()-1).yDisplayPosition,0xFF808080);
            drawStringCenteredNoShadow(fr,recipe.getData().cost+" sE",x + list.get(list.size()-1).xDisplayPosition - 76, y +  list.get(list.size()-1).yDisplayPosition + 8,0xFFCC0000);

            //drawTexturedModalRect( 90, 35, 22, 15);
        }
    }

    public boolean getIsMouseOverSlot(final Slot slot, int x, int y, int mouseX, int mouseY)
    {
        return mouseX >= x+slot.xDisplayPosition - 1 && mouseX < x+slot.xDisplayPosition + 16 + 1 && mouseY >= y+slot.yDisplayPosition - 1 && mouseY < y+slot.yDisplayPosition + 16 + 1;
    }

    @Override
    public void render(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.render(re, fr, x, y, mouseX, mouseY, partialTicks);

    }

    @Override
    protected void renderBackground(RenderEngine re, int x, int y) {
        super.renderBackground(re, x, y);
        re.bindTexture(re.getTexture("/gui/crafting.png"));
        for (int i = 1; i <= recipes.size(); i++) {
            RecipeEntryMachine recipe = recipes.get(i-1);
            List<SlotGuidebook> list = map.get(recipe);
            drawTexturedModalRect(x + list.get(list.size()-1).xDisplayPosition - 32, y +  list.get(list.size()-1).yDisplayPosition, 90, 35, 22, 15);
        }
    }

    @Override
    public void keyTyped(char c, int key, int x, int y, int mouseX, int mouseY) {
        super.keyTyped(c, key, x, y, mouseX, mouseY);
        if(mc.gameSettings.keyShowRecipe.isKeyboardKey(key)){
            SlotGuidebook hoveringSlot= null;
            for (SlotGuidebook slot : slots) {
                if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) hoveringSlot = slot;
            }
            if(hoveringSlot != null){
                if(hoveringSlot.hasStack()){
                    String query = "r:"+hoveringSlot.getStack().getDisplayName()+"!";
                    PageManager.searchQuery = SearchQuery.resolve(query);
                    SearchPage.searchField.setText(query);
                    GuiGuidebook.getPageManager().updatePages();
                    GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                }
            }
        } else if (mc.gameSettings.keyShowUsage.isKeyboardKey(key)) {
            SlotGuidebook hoveringSlot= null;
            for (SlotGuidebook slot : slots) {
                if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) hoveringSlot = slot;
            }
            if(hoveringSlot != null) {
                if (hoveringSlot.hasStack()) {
                    String query = "u:" + hoveringSlot.getStack().getDisplayName() + "!";
                    PageManager.searchQuery = SearchQuery.resolve(query);
                    SearchPage.searchField.setText(query);
                    GuiGuidebook.getPageManager().updatePages();
                    GuiGuidebook.getPageManager().setCurrentPage(GuiGuidebook.getPageManager().getSectionIndex(GuidebookSections.CRAFTING), true);
                }
            }
        }
    }

    @Override
    protected void renderOverlay(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.renderOverlay(re, fr, x, y, mouseX, mouseY, partialTicks);
        SlotGuidebook mouseOverSlot = null;
        for (SlotGuidebook slot : slots) {
            if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) mouseOverSlot = slot;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (mouseOverSlot != null && mouseOverSlot.hasStack())
            {
                boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
                String str = guiTooltip.getTooltipText(mouseOverSlot.getStack(), showDescription, mouseOverSlot);
                if(!str.isEmpty())
                {
                    guiTooltip.render(str, mouseX, mouseY, 8, -8);
                }
            }
        }
    }
}
