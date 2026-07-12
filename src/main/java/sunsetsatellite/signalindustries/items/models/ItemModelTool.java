package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.DisplayPos;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;

import java.awt.*;

import static net.minecraft.client.render.item.model.ItemModelDispatcher.*;

public class ItemModelTool extends ItemModelStandard {
	public ItemModelTool(@NotNull Item item) {
		super(item);
		setDisplayPos(DisplayPos.FIRST_PERSON_RIGHT_HAND, HANDHELD_FIRST_PERSON_RIGHT_HAND)
			.setDisplayPos(DisplayPos.FIRST_PERSON_LEFT_HAND, HANDHELD_FIRST_PERSON_LEFT_HAND)
			.setDisplayPos(DisplayPos.THIRD_PERSON_RIGHT_HAND, HANDHELD_THIRD_PERSON_RIGHT_HAND)
			.setDisplayPos(DisplayPos.THIRD_PERSON_LEFT_HAND, HANDHELD_THIRD_PERSON_LEFT_HAND);
	}

	@Override
	public void renderItemOverlayIntoGUI(@NotNull final TessellatorGeneral tessellator, @NotNull final FontRenderer fontRenderer, @NotNull final TextureManager textureManager, @NotNull final ItemStack itemstack, final int x, final int y, @Nullable final String override, final float alpha) {
		GLRenderer.pushFrame();
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		final int a = (int) (alpha * 255.0f);
		if (itemstack.getItem() instanceof IItemFluidContainer inv) {
			final int barWidth = (int) Math.round((inv.getFluidAmount(itemstack) * 13D) / inv.getCapacity(itemstack));
			final int progress = (int) Math.round((inv.getFluidAmount(itemstack) * 255D) / inv.getCapacity(itemstack));
			GLRenderer.pushFrame();
			GLRenderer.disableState(State.DEPTH_TEST);
			GLRenderer.setShader(Shaders.COLOR);

			final int colorFG;
			final int colorBG;
			colorFG = Color.HSBtoRGB(progress / 255F / 3F, 1f, 1f);
			colorBG = (0xFF - progress) / 4 << 16 | 0x3f00;

			tessellator.startDrawingQuads();
			renderColoredQuad(tessellator, x + 2, y + 13, 13, 2, 0, a);
			renderColoredQuad(tessellator, x + 2, y + 13, 12, 1, colorBG, a);
			renderColoredQuad(tessellator, x + 2, y + 13, barWidth, 1, colorFG, a);
			tessellator.draw();
			GLRenderer.popFrame();
		}
		GLRenderer.popFrame();
	}
}
