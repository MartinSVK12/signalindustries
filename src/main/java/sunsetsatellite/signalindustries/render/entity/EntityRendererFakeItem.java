package sunsetsatellite.signalindustries.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Random;

import static org.lwjgl.opengl.GL11.glPushMatrix;

public class EntityRendererFakeItem extends EntityRenderer<EntityItem>
{
	public final RenderBlocks renderBlocks;
	private final Random random;
	public boolean useColor;

	public EntityRendererFakeItem() {
		renderBlocks = new RenderBlocks();
		random = new Random();
		useColor = true;
		shadowSize = 0.15F;
		shadowOpacity = 0.75F;
	}

	public int getItemColorFromMetadata(Item item, int metadata) {
		if (item instanceof ItemBlock) {
			Block<?> block = ((ItemBlock<?>)item).getBlock();
			return BlockColorDispatcher.getInstance().getDispatch(block).getFallbackColor(metadata);
		}
		return 0xFFFFFF;
	}

	public void render(Tessellator tessellator, final EntityItem entity, final double x, final double y, final double z, final float yaw, final float partialTick) {
		float brightness = 1.0f;
		if(!LightmapHelper.isLightmapEnabled()) {
			brightness = entity.getBrightness(partialTick);
		}
		random.setSeed(187L);
		final ItemStack itemstack = entity.item;
		if(itemstack == null) {
			return;
		}
		Item item = itemstack.getItem();
		if(item == null) {
			return;
		}
		glPushMatrix();
		final float f3 = (float) Math.toDegrees(yaw / 20f);
		byte renderCount = 1;
		if (entity.item.stackSize > 1) {
			renderCount = 2;
		}
		if (entity.item.stackSize > 5) {
			renderCount = 3;
		}
		if (entity.item.stackSize > 20) {
			renderCount = 4;
		}
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		BlockModel.setRenderBlocks(renderBlocks);
		ItemModelDispatcher.getInstance().getDispatch(item).renderAsItemEntity(Tessellator.instance, entity, random, itemstack, renderCount, f3, brightness, partialTick);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	public void render(Tessellator tessellator, final ItemStack stack, final double x, final double y, final double z, final float yaw, final float partialTick) {
		float brightness = 1.0f;
		random.setSeed(187L);
		if(stack == null) {
			return;
		}
		Item item = stack.getItem();
        glPushMatrix();
		final float f3 = (float) Math.toDegrees(yaw / 20f);
		byte renderCount = 1;
		if (stack.stackSize > 1) {
			renderCount = 2;
		}
		if (stack.stackSize > 5) {
			renderCount = 3;
		}
		if (stack.stackSize > 20) {
			renderCount = 4;
		}
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		BlockModel.setRenderBlocks(renderBlocks);
		ItemModelDispatcher.getInstance().getDispatch(item).renderAsItemEntity(Tessellator.instance, new EntityItem(Minecraft.getMinecraft().currentWorld,x,y,z,stack), random, stack, renderCount, f3, brightness, partialTick);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
}
