package sunsetsatellite.signalindustries.api.impl.guidebookpp.containers;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IContainerRecipeBase;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.ArrayList;

public class ContainerGuidebookInfuserRecipe extends ContainerGuidebookRecipeBase
    implements IContainerRecipeBase {

    ItemStack machine;

    public ContainerGuidebookInfuserRecipe(ItemStack stack, ArrayList<ItemStack> itemInputs, ArrayList<FluidStack> fluidInputs, ArrayList<ItemStack> itemOutputs, ArrayList<FluidStack> fluidOutputs) {
        machine = stack;
        this.addSlot(new SlotGuidebook(0, 29, 2, itemInputs.get(0), false));
        this.addSlot(new SlotGuidebook(1, 29, 36,itemInputs.size() == 1 ? null : itemInputs.get(1), false));
        this.addSlot(new SlotGuidebook(2, 9, 20,fluidInputs.size() > 0 ? new ItemStack(fluidInputs.get(0).liquid,fluidInputs.get(0).amount) : null, false));
        this.addSlot(new SlotGuidebook(3, 59, 21,itemOutputs.get(0), false));
        this.addSlot(new SlotGuidebook(4, -18, 34,new ItemStack(SignalIndustries.energyFlowing,320), false));
        this.addSlot(new SlotGuidebook(5,30,19,stack,true));
    }

    public void drawContainer(GuiGuidebook guidebook, int xSize, int ySize, int index){
        RenderItem itemRenderer = new RenderItem();
        int i = GuidebookPlusPlus.mc.renderEngine.getTexture("/assets/signalindustries/gui/infuser_recipe.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuidebookPlusPlus.mc.renderEngine.bindTexture(i);
        int j = (guidebook.width - xSize) / 2;
        int k = (guidebook.height - ySize) / 2;
        int xPos = j + 29 + 158 * (index / 3);
        int yPos = k + 30 + 62 * (index % 3);
        int yOffset = 0;
        guidebook.drawTexturedModalRect(xPos - 20, yPos, 138, yOffset, 121, 54);

    };

    @Override
    public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean bl, boolean bl2) {

    }
}