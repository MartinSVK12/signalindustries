package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;

import java.awt.*;

public class ItemModelTool extends ItemModelStandard {
    public ItemModelTool(Item item, String namespace) {
        super(item, namespace);
    }

    @Override
    public void renderItemOverlayIntoGUI(Tessellator tessellator, Font font, TextureManager textureManager, ItemStack itemstack, int x, int y, String override, float alpha) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if(itemstack != null && itemstack.getItem() instanceof IItemFluidContainer){
            int a = (int)(alpha * 255.0F);
            IItemFluidContainer inv = (IItemFluidContainer) itemstack.getItem();
            int capacity = inv.getCapacity(itemstack);
            int amount = inv.getFluidAmount(itemstack);
            int barWidth = (int)Math.round((double)amount * (double)13.0F / (double)capacity);
            int progress = (int)Math.round((double)amount * (double)255.0F / (double)capacity);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            int colorFG = Color.HSBtoRGB((float)progress / 255.0F / 3.0F, 1.0F, 1.0F);
            int colorBG = (255 - progress) / 4 << 16 | 16128;
            this.renderColoredQuad(tessellator, x + 2, y + 13, 13, 2, 0, a);
            this.renderColoredQuad(tessellator, x + 2, y + 13, 12, 1, colorBG, a);
            this.renderColoredQuad(tessellator, x + 2, y + 13, barWidth, 1, colorFG, a);
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042);
        }
    }
}
