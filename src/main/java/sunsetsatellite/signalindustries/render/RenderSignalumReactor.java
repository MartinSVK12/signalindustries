package sunsetsatellite.signalindustries.render;

import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.machines.TileEntitySignalumReactor;


public class RenderSignalumReactor extends RenderMultiblock {

    @Override
    public void doRender(TileEntity tileEntity, double d, double e, double f, float g) {
        TileEntitySignalumReactor reactor = (TileEntitySignalumReactor) tileEntity;
        super.doRender(tileEntity, d, e, f, g);
        float fluidAmount = reactor.getFuel() + reactor.getDepletedFuel();
        float fluidMaxAmount = 4000*9;
        int fluidId = SignalIndustries.energyFlowing.id;

        float amount = Math.max(Math.abs(fluidAmount / fluidMaxAmount),fluidAmount != 0 ? 0.14f : 0f);
        float depletedAmount = Math.min(Math.abs(reactor.getDepletedFuel() / fluidAmount),0.9f);
        if (fluidId != 0) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d, (float)e, (float)f);
            GL11.glScalef(2.90F, 7f, 2.90F);
            GL11.glTranslatef(0.70F, -0.42F, -0.34F);
            GL11.glColor4f(1-depletedAmount,1-depletedAmount,1-depletedAmount,amount);
            GL11.glDisable(2896);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable( GL11.GL_BLEND );
            this.drawBlock(this.getFontRenderer(), this.renderDispatcher.renderEngine.mc.renderEngine, fluidId, 0, 0, 0, 0, tileEntity);
            GL11.glEnable(2896);
            GL11.glPopMatrix();
        }
    }
}
