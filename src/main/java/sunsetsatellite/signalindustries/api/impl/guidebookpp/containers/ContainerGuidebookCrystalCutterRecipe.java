package sunsetsatellite.signalindustries.api.impl.guidebookpp.containers;


import net.minecraft.client.gui.GuiGuidebook;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeBase;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IContainerRecipeBase;
import sunsetsatellite.guidebookpp.recipes.RecipeBase;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.List;

public class ContainerGuidebookCrystalCutterRecipe extends ContainerGuidebookRecipeBase
    implements IContainerRecipeBase {

    ItemStack machine;

    public ContainerGuidebookCrystalCutterRecipe(ItemStack stack, RecipeFluid recipeFluid) {
        machine = stack;
        this.addSlot(new SlotGuidebook(0, 9, 10, recipeFluid.itemInputs.get(0), false));
        this.addSlot(new SlotGuidebook(0, -19, 10, new ItemStack(recipeFluid.fluidInputs.get(0).liquid,recipeFluid.fluidInputs.get(0).amount), false));
        this.addSlot(new SlotGuidebook(1, 69, 19,recipeFluid.itemOutputs.get(0), false));
        this.addSlot(new SlotGuidebook(2, 9, 45, new ItemStack(SignalIndustries.energyFlowing,recipeFluid.cost), false));
        this.addSlot(new SlotGuidebook(3,36,18,stack,true));
    }

    public void drawContainer(GuiGuidebook guidebook, int xSize, int ySize, int index, RecipeBase recipeBase){
        ItemEntityRenderer itemRenderer = new ItemEntityRenderer();
        int i = GuidebookPlusPlus.mc.renderEngine.getTexture("/assets/signalindustries/gui/generic_machine_double_recipe.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuidebookPlusPlus.mc.renderEngine.bindTexture(i);
        int j = (guidebook.width - xSize) / 2;
        int k = (guidebook.height - ySize) / 2;
        int xPos = j + 29 + 158 * (index / 3);
        int yPos = k + 30 + 62 * (index % 3);
        int yOffset = 0;
        guidebook.drawTexturedModalRect(xPos - 20, yPos, 138, yOffset, 121, 54);

    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }
}
