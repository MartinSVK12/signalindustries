package sunsetsatellite.signalindustries.gui.guidebook.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;

import java.util.ArrayList;

public class MultiblockPage
        extends GuidebookPage {
    public final Multiblock multiblock;

	public float rotation = 0;


    public MultiblockPage(GuidebookSection section, Multiblock multiblock) {
        super(section);
        this.multiblock = multiblock;
    }

    @Override
    protected void renderBackground(TextureManager re, int x, int y) {
        super.renderBackground(re, x, y);
    }

    @Override
    protected void renderForeground(TextureManager re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (multiblock != null) {
            drawStringCenteredNoShadow(fr, Catalyst.translateNameKey(multiblock.translateKey), x + 158 / 2, y + 10, 0x000000);
        } else {
            drawStringCenteredNoShadow(fr, "No results :(", x + width / 2, y + height / 2, 0xFF808080);
        }

        renderMultiblock(re, fr, x, y, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onMouseDown(int x, int y, int mouseX, int mouseY, int button) {
        super.onMouseDown(x, y, mouseX, mouseY, button);
    }

    private void renderMultiblock(TextureManager re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (multiblock == null) return;

		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(x + 68, y + 44 + 62,900f);
		GLRenderer.enableState(State.DEPTH_TEST);
		//.glEnable(.GL_DEPTH_TEST);

		Vec3i size = multiblock.getSize();

		float renderSize = 12;
		if(size.x > 12 || size.z > 12) {
			renderSize = 8;
		}
		if(size.x > 15 || size.z > 15) {
			renderSize = 4;
		}

		rotation += 0.1f;
		if (rotation > 360) {
			rotation = 0;
		}

		GLRenderer.modelM4f().scale(renderSize, -renderSize, renderSize);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(30),1,0,0);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(45),0,1,0);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(rotation),0,1,0);

		Lighting.enableLight();
		RenderMultiblockInGUI r = new RenderMultiblockInGUI();
		ArrayList<BlockInstance> blocks = multiblock.getBlocks();
		if (multiblock.getOrigin() != null) {
			blocks.add(multiblock.getOrigin());
		}

		r.render(blocks, 1);
		GLRenderer.popFrame();
		Lighting.disable();
    }
}
