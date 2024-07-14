package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.IColorOverride;
import sunsetsatellite.catalyst.core.util.IFullbright;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.multiblocks.HologramWorld;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.machines.TileEntitySignalumReactor;

import java.util.ArrayList;


public class RenderSignalumReactor extends RenderMultiblock {

    @Override
    public void doRender(Tessellator tessellator, TileEntity tileEntity, double d, double e, double f, float g) {
        TileEntitySignalumReactor reactor = (TileEntitySignalumReactor) tileEntity;
        super.doRender(tessellator, tileEntity, d, e, f, g);
        float fluidAmount = reactor.getFuel() + reactor.getDepletedFuel();
        float fluidMaxAmount = 4000*9;
        int fluidId = SIBlocks.energyFlowing.id;

        float amount = Math.max(Math.abs(fluidAmount / fluidMaxAmount),fluidAmount != 0 ? 0.14f : 0f);
        float depletedAmount = Math.min(Math.abs(reactor.getDepletedFuel() / fluidAmount),0.9f);
        if (fluidId != 0) {
            BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[fluidId]);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d, (float)e, (float)f);
            GL11.glScalef(2.90F, 7f, 2.90F);
            GL11.glTranslatef(1.2f, 0.08f, 0.16f);
            ((IColorOverride)model).overrideColor(1-depletedAmount,1-depletedAmount,1-depletedAmount,amount);
            ((IFullbright) model).enableFullbright();
            GL11.glDisable(2896);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable( GL11.GL_BLEND );
            //blockRenderer = new RenderBlocks(new HologramWorld((ArrayList<BlockInstance>) Catalyst.listOf(new BlockInstance(Block.blocksList[fluidId],new Vec3i(),null))));
            this.drawBlock(tessellator, model, 0);
            ((IFullbright) model).disableFullbright();
            ((IColorOverride)model).overrideColor(1,1,1,1);
            GL11.glEnable(2896);
            GL11.glPopMatrix();
        }
    }
}
