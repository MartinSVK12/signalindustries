package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.energyapi.interfaces.mixins.IGuiContainer;
import sunsetsatellite.energyapi.util.ICustomDescription;

@Mixin(
        value = GuiContainer.class,
        remap = false
)
public class GuiContainerMixin extends GuiScreen
    implements IGuiContainer
{

    @Shadow private static RenderItem itemRenderer;

    @Inject(
            method = "drawScreen",
            remap = false,
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/GuiContainer;formatDescription(Ljava/lang/String;I)Ljava/lang/String;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setDescription(int x, int y, float renderPartialTicks, CallbackInfo ci, int centerX, int centerY, Slot slot, InventoryPlayer inventoryplayer, StringTranslate trans, StringBuilder text, boolean multiLine, boolean control, boolean shift, boolean showDescription, boolean isCrafting, String itemName, String itemNick, boolean debug){
        ItemStack stack = slot.getStack();
        if(stack != null && stack.getItem() instanceof ItemBlock){
            Block block = Block.blocksList[stack.getItem().itemID];
            if(block instanceof ICustomDescription){
                text.append(((ICustomDescription) block).getDescription(stack)).append("\n");
            }
        }
        if(stack != null && stack.getItem() instanceof ICustomDescription){
            text.append(((ICustomDescription) stack.getItem()).getDescription(stack)).append("\n");
        }
    }

    public void drawItemStack(ItemStack stack, int x, int y) {
        if(stack != null) {
            GL11.glPushMatrix();
            GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, stack, x, y, 1.0F);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
    }

}
