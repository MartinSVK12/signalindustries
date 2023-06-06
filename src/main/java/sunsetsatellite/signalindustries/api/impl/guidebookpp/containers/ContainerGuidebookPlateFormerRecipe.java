package sunsetsatellite.signalindustries.api.impl.guidebookpp.containers;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IContainerRecipeBase;
import sunsetsatellite.guidebookpp.recipes.RecipeBase;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;

import java.util.ArrayList;

public class ContainerGuidebookPlateFormerRecipe extends ContainerGuidebookRecipeBase
    implements IContainerRecipeBase {

    ItemStack machine;

    public ContainerGuidebookPlateFormerRecipe(ItemStack stack, RecipeFluid recipeFluid) {
        machine = stack;
        this.addSlot(new SlotGuidebook(0, 9, 10, recipeFluid.itemInputs.get(0), false));
        this.addSlot(new SlotGuidebook(1, 69, 19,recipeFluid.itemOutputs.get(0), false));
        this.addSlot(new SlotGuidebook(2, 9, 45, new ItemStack(SignalIndustries.energyFlowing, recipeFluid.cost), false));
        this.addSlot(new SlotGuidebook(3,36,18,stack,true));
    }

    public void drawContainer(GuiGuidebook guidebook, int xSize, int ySize, int index, RecipeBase recipeBase){
        RenderItem itemRenderer = new RenderItem();
        int i = GuidebookPlusPlus.mc.renderEngine.getTexture("/assets/signalindustries/gui/generic_machine_recipe.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuidebookPlusPlus.mc.renderEngine.bindTexture(i);
        int j = (guidebook.width - xSize) / 2;
        int k = (guidebook.height - ySize) / 2;
        int xPos = j + 29 + 158 * (index / 3);
        int yPos = k + 30 + 62 * (index % 3);
        int yOffset = 0;
        guidebook.drawTexturedModalRect(xPos, yPos, 158, yOffset, 98, 54);

    }

    @Override
    public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean bl, boolean bl2) {

    }
}
