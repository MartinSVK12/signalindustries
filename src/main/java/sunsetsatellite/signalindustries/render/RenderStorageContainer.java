package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.signalindustries.tiles.TileEntityStorageContainer;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityAssembler;
import sunsetsatellite.signalindustries.util.FakeItemElement;

public class RenderStorageContainer extends TileEntityRenderer<TileEntityStorageContainer> {

	private final FakeItemElement itemRenderer = new FakeItemElement(Minecraft.getMinecraft());
	private final Gui gui = new Gui();

	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntityStorageContainer tileEntity, double x, double y, double z, float partialTick) {
		GLRenderer.pushFrame();
		float scale = 0.6666667F;
		float rot;
		int rotMeta = tileEntity.getBlockMeta();
		rot = 0.0F;
		if (rotMeta == 2) {
			rot = 180.0F;
		}

		if (rotMeta == 4) {
			rot = 90.0F;
		}

		if (rotMeta == 5) {
			rot = -90.0F;
		}

		GLRenderer.modelM4f().translate((float) x + 0.5F, (float) y + 0.75F * scale, (float) z + 0.5F);
		GLRenderer.modelM4f().rotate(-(float) Math.toRadians(rot), 0.0F, 1.0F, 0.0F);
		GLRenderer.modelM4f().translate(0.0F, -0.3125F, -0.4375F);

		GLRenderer.pushFrame();
		GLRenderer.modelM4f().scale(scale, -scale, -scale);
		GLRenderer.popFrame();
		rot = 0.016666668F * scale;
		GLRenderer.modelM4f().translate(0.0F, 0.5F * scale, 0.07F * scale);
		GLRenderer.modelM4f().scale(rot, -rot, rot);
		GLRenderer.setDepthMask(false);
		Lighting.disable();

		int color = 0xFFFFFFFF;
		ItemStack stack = null;

		if(tileEntity.contents != null){
			stack = tileEntity.contents;
		}
		String s = "";
		if(tileEntity.locked){
			s = "Locked";
		}

		int offset = -6;
		GLRenderer.modelM4f().translate(0, 28, 82);
		gui.drawStringShadow(getFont(), s, -getFont().stringWidth(s) / 2, (offset * 10 - 5), 0xFFFFFF00);
		GLRenderer.modelM4f().translate(0, -28, -82);
		GLRenderer.modelM4f().scale(2.5f, 2.5f, 0.3f);
		GLRenderer.modelM4f().translate(0, 0, 240);
		drawItemStack(stack, -8, -8);
		GLRenderer.enableState(State.DEPTH_TEST);
		Lighting.enableLight();
		GLRenderer.setDepthMask(true);
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GLRenderer.popFrame();
	}


	private void drawItemStack(ItemStack stack, int x, int y) {
		if (stack != null) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().rotate(120.0F, 1.0F, 0.0F, 0.0F);
			Lighting.disable();
			GLRenderer.popFrame();
			GLRenderer.pushFrame();
			GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GLRenderer.modelM4f().translate(0.0F, 0.0F, 32.0F);
			itemRenderer.render(stack, x, y, NumberUtil.format(stack.stackSize));
			GLRenderer.disableState(State.DEPTH_TEST);
			GLRenderer.popFrame();
		}
	}
}
