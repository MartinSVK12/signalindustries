package sunsetsatellite.signalindustries.gui.guidebook.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.achievement.AchievementList;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CentrifugePage
    extends GuidebookPage {
    public static final int RECIPES_PER_PAGE = 2; //used through reflection
    public List<RecipeEntryMachine> recipes;
    public List<SlotGuidebook> slots;
    public Map<RecipeEntryMachine,List<SlotGuidebook>> map;
    private final GuiTooltip guiTooltip;
    private final GuiRenderItem guiRenderItem;
    private static final Minecraft mc = Minecraft.getMinecraft(GuidebookPage.class);
    private static EntityPlayer player;
    private static long ticks = 0;
    public CentrifugePage(GuidebookSection section, ArrayList<RecipeEntryMachine> recipes) {
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
            List<RecipeSymbol> inputSymbol = new ArrayList<>();
            RecipeSymbol outputSymbol;
            for (RecipeExtendedSymbol symbol : recipe.getInput()) {
                if(symbol == null) inputSymbol.add(null);
                else if(symbol.getFluidStack() != null){
                    FluidStack fluidStack = symbol.getFluidStack();
                    inputSymbol.add(new RecipeSymbol(new ItemStack(fluidStack.liquid,fluidStack.amount)));
                } else {
                    inputSymbol.add(symbol.asNormalSymbol());
                }
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

            int spacing = 70;
            int offsetY = 52;

            outputSymbol = new RecipeSymbol(recipe.getOutput());
            recipeSlots.add(new SlotGuidebook(0, (width/2)-72, spacing*(map.size())+offsetY, inputSymbol.get(0), false,recipe));
            recipeSlots.add(new SlotGuidebook(1, (width/2)-52, spacing*(map.size())+offsetY-20, inputSymbol.get(1), false,recipe));
            recipeSlots.add(new SlotGuidebook(2, (width/2)-52, spacing*(map.size())+offsetY+20, inputSymbol.get(2), false,recipe));
            recipeSlots.add(new SlotGuidebook(3, (width/2)-32, spacing*(map.size())+offsetY, inputSymbol.get(3), false,recipe));
            recipeSlots.add(new SlotGuidebook(5,(width/2)+48, spacing*(map.size())+offsetY,new RecipeSymbol(acceptedMachines),false,recipe));
            recipeSlots.add(new SlotGuidebook(4, (width/2)+24, spacing*(map.size())+offsetY, outputSymbol, false,recipe));
            map.put(recipe,recipeSlots);
            slots.addAll(recipeSlots);
        }
    }

    @Override
    protected void renderForeground(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        drawStringCenteredNoShadow(fr, "Centrifuge", x+width - 158 / 2, y+5, 0xFF808080);
        if(recipes.isEmpty()){
            drawStringCenteredNoShadow(fr,"No recipes found :(" ,x+width/2,y+height/2,0xFF808080);
        }
        SlotGuidebook mouseOverSlot = null;
        ticks++;
        for (SlotGuidebook slot : slots) {
            if(slot.item != null){
                slot.discovered = mc.statsCounter.readStat(StatList.pickUpItemStats[slot.item.itemID]) > 0;
            }
            if(mc.thePlayer.gamemode == Gamemode.creative) slot.discovered = true;
            if(ticks > 100) {
                slot.showRandomItem();
                if(slots.get(slots.size()-1) == slot){
                    ticks = 0;
                }
            }
            if(slot.id != 5){
                drawSlot(re,x+slot.xDisplayPosition-1,y+slot.yDisplayPosition-1,0xFFFFFFFF);
            }
            if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) mouseOverSlot = slot;
            guiRenderItem.render(slot.getStack(),x+slot.xDisplayPosition,y+slot.yDisplayPosition,mouseOverSlot == slot,slot);
        }
        for (int i = 1; i <= recipes.size(); i++) {
            RecipeEntryMachine recipe = recipes.get(i-1);
            List<SlotGuidebook> list = map.get(recipe);
            drawStringCenteredNoShadow(fr,recipe.getData().ticks+"t | "+(recipe.getData().chance*100)+"%",x + list.get(list.size()-1).xDisplayPosition - 20, y +  list.get(list.size()-1).yDisplayPosition + 18,0xFF202020);
            drawStringCenteredNoShadow(fr,recipe.getData().cost+" sE",x + list.get(list.size()-1).xDisplayPosition - 20, y +  list.get(list.size()-1).yDisplayPosition + 26,0xFFCC0000);

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
