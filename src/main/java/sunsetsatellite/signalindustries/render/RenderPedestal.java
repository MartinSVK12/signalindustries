package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.render.model.ModelTome;
import sunsetsatellite.signalindustries.tiles.TileEntityPedestal;

public class RenderPedestal extends TileEntityRenderer<TileEntityPedestal> {

    private ModelTome tome = new ModelTome();

    @Override
    public void doRender(Tessellator tessellator, TileEntityPedestal tileEntity, double d, double e, double f, float g) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d + 0.5F, (float)e + 0.65F, (float)f + 0.5F);
        float ticks = tileEntity.ticks + g;
        GL11.glTranslatef(0.0F, 0.1F + MathHelper.sin(ticks * 0.1F) * 0.01F, 0.0F);
        float rot = tileEntity.bookRot2 - tileEntity.bookRotPrev;
        while (rot >= (float)Math.PI) {
            rot -= ((float) Math.PI * 2F);
        }
        while (rot < -(float)Math.PI)
        {
            rot += ((float)Math.PI * 2F);
        }
        float prevRot = tileEntity.bookRotPrev + rot * g;
        GL11.glRotatef(-prevRot * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        Texture tex = renderDispatcher.textureManager.loadTexture("/assets/signalindustries/textures/misc/tome.png");
        renderDispatcher.textureManager.bindTexture(tex);
        //todo: find out what in the crispy fuck this is supposed to do
        float flip = tileEntity.flipPrev + (tileEntity.flip - tileEntity.flipPrev) * g + 0.25F;
        float flip2 = tileEntity.flipPrev + (tileEntity.flip - tileEntity.flipPrev) * g + 0.75F;
        flip = (flip - (float)((double)flip + 1024.0D)-1024) * 1.6F - 0.3F;
        flip2 = (flip2 - (float)((double)flip2 + 1024.0D)-1024) * 1.6F - 0.3F;

        if (flip < 0.0F)
        {
            flip = 0.0F;
        }

        if (flip2 < 0.0F)
        {
            flip2 = 0.0F;
        }

        if (flip > 1.0F)
        {
            flip = 1.0F;
        }

        if (flip2 > 1.0F)
        {
            flip2 = 1.0F;
        }
        float spread = tileEntity.bookSpreadPrev + (tileEntity.bookSpread - tileEntity.bookSpreadPrev) * g;
        this.tome.render(ticks, flip, flip2, spread, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
