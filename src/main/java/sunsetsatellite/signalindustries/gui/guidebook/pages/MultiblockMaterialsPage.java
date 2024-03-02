package sunsetsatellite.signalindustries.gui.guidebook.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;
import org.apache.logging.log4j.core.util.Integers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import turniplabs.halplibe.HalpLibe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiblockMaterialsPage extends GuidebookPage {
    public final Multiblock multiblock;
    private final GuiTooltip guiTooltip;
    private final GuiRenderItem guiRenderItem;
    private static final Minecraft mc = Minecraft.getMinecraft(GuidebookPage.class);
    public List<SlotGuidebook> slots = new ArrayList<>();
    private static long ticks = 0;


    public MultiblockMaterialsPage(GuidebookSection section, Multiblock multiblock) {
        super(section);
        this.multiblock = multiblock;
        guiTooltip = new GuiTooltip(mc);
        guiRenderItem = new GuiRenderItem(mc);

        List<ItemStack> blocksUncondensed = multiblock
                .getBlocks()
                .stream()
                .map((B) -> new ItemStack(B.block, 1, B.meta == -1 ? 0 : B.meta))
                .collect(Collectors.toList());
        List<ItemStack> blocks = SignalIndustries.condenseList(blocksUncondensed);
        blocks.add(new ItemStack(multiblock.getOrigin().block,1, multiblock.getOrigin().meta == -1 ? 0 : multiblock.getOrigin().meta));

        int i = 0;
        int maxSlotsInRow = 7;
        for (ItemStack block : blocks) {
            slots.add(new SlotGuidebook(i,18 + 18 * (i % maxSlotsInRow),24 + 18 * (i / maxSlotsInRow),new RecipeSymbol(block),false,null));
            i++;
        }

    }


    @Override
    protected void renderForeground(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if(multiblock != null){
            drawStringCenteredNoShadow(fr, I18n.getInstance().translateNameKey(multiblock.translateKey), x + 158 / 2, y + 10, 0x000000);
        } else {
            drawStringCenteredNoShadow(fr,"No results :(" ,x+width/2,y+height/2,0xFF808080);
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
            drawSlot(re,x+slot.xDisplayPosition-1,y+slot.yDisplayPosition-1,0xFFFFFFFF);
            if(getIsMouseOverSlot(slot,x,y,mouseX,mouseY)) mouseOverSlot = slot;
            guiRenderItem.render(slot.getStack(),x+slot.xDisplayPosition,y+slot.yDisplayPosition,mouseOverSlot == slot,slot);
        }
    }

    public boolean getIsMouseOverSlot(final Slot slot, int x, int y, int mouseX, int mouseY)
    {
        return mouseX >= x+slot.xDisplayPosition - 1 && mouseX < x+slot.xDisplayPosition + 16 + 1 && mouseY >= y+slot.yDisplayPosition - 1 && mouseY < y+slot.yDisplayPosition + 16 + 1;
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
