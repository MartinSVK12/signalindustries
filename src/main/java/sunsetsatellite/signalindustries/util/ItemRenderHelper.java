package sunsetsatellite.signalindustries.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRenderHelper {
	public static Minecraft mc;
	private static void initMC(){
		if (mc == null){
			mc = Minecraft.getMinecraft(ItemRenderHelper.class);
		}
	}
	public static void renderItemStack(@Nullable ItemStack itemStack, int x, int y, double scaleX, double scaleY, float brightness, float alpha){
		initMC();
		boolean render = true;
		GL11.glPushMatrix();
		Lighting.enableInventoryLight();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if (itemStack != null) {
			BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
			ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
			GL11.glTranslatef(x, y, 0);
			GL11.glScaled(scaleX,scaleY,1);
			try {
				itemModel.renderItemIntoGui(Tessellator.instance, mc.fontRenderer, mc.renderEngine, itemStack, 0, 0, brightness, alpha);
			} catch (NullPointerException npe){
				//fuck you
			}

		}

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		Lighting.disable();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}
}
