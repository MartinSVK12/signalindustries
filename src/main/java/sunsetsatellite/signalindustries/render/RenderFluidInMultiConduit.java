package sunsetsatellite.signalindustries.render;


import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitBlock;
import sunsetsatellite.catalyst.core.util.model.IColorOverride;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicMultiConduit;
import sunsetsatellite.signalindustries.tiles.TileEntityMultiConduit;

import java.util.ArrayList;

public class RenderFluidInMultiConduit extends TileEntityRenderer<TileEntityMultiConduit> {
    @Override
    public void doRender(Tessellator tessellator, TileEntityMultiConduit tile, double x, double y, double z, float f) {
        Vec3i pos = new Vec3i(tile.x,tile.y,tile.z);
        boolean split = false;
        for (Direction dir : Direction.values()) {
            Block<?> connectedBlock = dir.getBlock(tile.worldObj, pos);
            if(connectedBlock != null && connectedBlock.getLogic() instanceof BlockLogicMultiConduit){
                Direction side = Direction.getDirectionFromSide(tile.worldObj.getBlockMetadata(tile.x, tile.y, tile.z));
                if(side != dir && side != dir.getOpposite()){
                    split = true;
                    break;
                }
            }
            if(connectedBlock != null && connectedBlock.getLogic() instanceof IConduitBlock){
                split = true;
                break;
            }
        }

        if(split) return;

        BlockModel<?>[] models = new BlockModel<?>[4];
        Vec3f[][] vecs = new Vec3f[][]{
                //X
                new Vec3f[]{
                        new Vec3f(0.5f,0.7f,0.7f),
                        new Vec3f(0.5f,0.7f,0.3f),
                        new Vec3f(0.5f,0.3f,0.7f),
                        new Vec3f(0.5f,0.3f,0.3f)
                },
                //Y
                new Vec3f[]{
                        new Vec3f(0.7f,0.5f,0.7f),
                        new Vec3f(0.3f,0.5f,0.7f),
                        new Vec3f(0.7f,0.5f,0.3f),
                        new Vec3f(0.3f,0.5f,0.3f)
                },
                //Z
                new Vec3f[]{
                        new Vec3f(0.7f,0.7f,0.5f), //1
                        new Vec3f(0.3f,0.7f,0.5f), //2
                        new Vec3f(0.7f,0.3f,0.5f), //3
                        new Vec3f(0.3f,0.3f,0.5f)  //4
                }

        };

        IConduitBlock[] conduits = tile.conduits;
        for (int i = 0; i < conduits.length; i++) {
            IConduitBlock conduit = conduits[i];
            if(conduit != null){
                if(conduit.getConduitCapability() == ConduitCapability.SIGNALUM || conduit.getConduitCapability() == ConduitCapability.FLUID){
                    if(tile.fluidContents[i] != null){
                        models[i] = BlockModelDispatcher.getInstance().getDispatch(tile.fluidContents[i].fluid.blocks.get(0));
                    } else {
                        models[i] = null;
                    }
                }
            }
        }

        for (int i = 0; i < models.length; i++) {
            BlockModel<?> model = models[i];
            if(model != null && tile.fluidContents[i] != null){
                ArrayList<BlockInstance> blockInstances = new ArrayList<>();
                for (FluidStack fluidStack : tile.fluidContents) {
                    if(fluidStack != null){
                        blockInstances.add(new BlockInstance(fluidStack.fluid.blocks.get(0),new Vec3i(i),null));
                    }
                }
                blockRenderer = new RenderBlocks(new HologramWorld(blockInstances));
                int amount = tile.fluidContents[i].amount;
                int maxAmount = tile.fluidCapacity[i];
                float ratio = ((float) amount / maxAmount);
                float mappedRatio = (float) Catalyst.map(ratio, 0.0d, 1.0d, 0.0d, 0.3d);
                Axis axis = Side.getSideById(tile.worldObj.getBlockMetadata(tile.x,tile.y,tile.z)).getAxis();
                int axisOrd = axis.ordinal();
                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glTranslated( x+vecs[axisOrd][i].x, y+vecs[axisOrd][i].y, z+vecs[axisOrd][i].z);
                switch (axis){
                    case X:
                        GL11.glScalef(0.98f,mappedRatio,0.3f);
                        break;
                    case Y:
                        GL11.glScalef(mappedRatio,0.98f,mappedRatio);
                        break;
                    case Z:
                        GL11.glScalef(0.3f,mappedRatio,0.98f);
                        break;
                }
                if(tile.fluidContents[i].fluid == Fluids.WATER){
                    ((IColorOverride)model).overrideColor(0,0.5f,1,0.75f);
                }
                drawBlock(tessellator,model,0);
                ((IColorOverride)model).overrideColor(1,1,1,1);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glPopMatrix();
            }
        }
    }

    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta) {
        TextureRegistry.blockAtlas.bind();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        model.renderBlockOnInventory(tessellator,meta,1,0.75f,null);
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private RenderBlocks blockRenderer;
}
