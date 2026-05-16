package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.model.IColorOverride;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicFluidConduit;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderFluidInConduit extends TileEntityRenderer<TileEntity> {
    @Override
    public void doRender(Tessellator tessellator, TileEntity tileEntity1, double d2, double d4, double d6, float f8) {

        if (!((IKeybinds) Minecraft.getMinecraft().gameSettings).signalindustries$getRenderFluidInsideConduits().value) {
            return;
        }

        blockRenderer = new RenderBlocks(tileEntity1.worldObj);
        int i = tileEntity1.x;
        int j = tileEntity1.y;
        int k = tileEntity1.z;
        World blockAccess = tileEntity1.worldObj;
        Block block = SIBlocks.prototypeConduit;

        if (tileEntity1.getBlock() != null) {
            block = tileEntity1.getBlock();
        }

        float fluidAmount = 0;
        float fluidMaxAmount = 1;
        int fluidId = -1;

        if (((TileEntityFluidContainer) tileEntity1).fluidContents[0] != null) {
            if (((TileEntityFluidContainer) tileEntity1).fluidContents[0].fluid != null) {
                fluidMaxAmount = ((TileEntityFluidContainer) tileEntity1).getFluidCapacityForSlot(0);
                fluidAmount = ((TileEntityFluidContainer) tileEntity1).fluidContents[0].amount;
                fluidId = ((TileEntityFluidContainer) tileEntity1).fluidContents[0].fluid.getFirstId();
            }
        }

        BlockModel<?> model = null;
        if (fluidId != -1) {
            Block<?> fluidBlock = Blocks.getBlock(fluidId);
            blockRenderer = new RenderBlocks(new HologramWorld((ArrayList<BlockInstance>) Catalyst.listOf(new BlockInstance(fluidBlock, new Vec3i(), 0, null))));
            model = BlockModelDispatcher.getInstance().getDispatch(fluidBlock);
        }

        if (model == null) return;

        fluidAmount = Math.min(fluidAmount, fluidMaxAmount);

        HashMap<Direction, Boolean> states = new HashMap<>();
        for (Direction direction : Direction.values()) {
            boolean show = false;
            Vec3i offset = new Vec3i(i, j, k).add(direction.getVec());
            Block<?> neighbouringBlock = blockAccess.getBlock(offset.x, offset.y, offset.z);
            if (neighbouringBlock != null) {
                if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
                    show = true;
                } else if (!(neighbouringBlock.getLogic() instanceof BlockLogicConduit || neighbouringBlock.getLogic() instanceof BlockLogicFluidConduit)) {
                    if (neighbouringBlock.isEntityTile) {
                        TileEntity neighbouringTile = blockAccess.getTileEntity(offset.x, offset.y, offset.z);
                        if (neighbouringTile instanceof IFluidInventory) {
                            show = true;
                        }
                    } else if (neighbouringBlock.hasTag(SignalIndustries.SIGNALUM_CONDUITS_CONNECT) || neighbouringBlock.hasTag(SignalIndustries.FLUID_CONDUITS_CONNECT)) {
                        show = true;
                    }
                }
            }
            states.put(direction, show);
        }

        float amount = (fluidAmount / fluidMaxAmount);
        float mapped = (float) Catalyst.map(amount, 0.0d, 1.0d, 0.0d, 0.3d);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) d2 + 0.15f, (float) d4 + 0.15f, (float) d6 + 0.15f);
        GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.33F, 0.33f, 0.33f);
        if (!(states.get(Direction.Y_NEG) && states.get(Direction.Y_POS))) {
            GL11.glScalef(0.3f, mapped, 0.3f);
        } else {
            GL11.glScalef(mapped, 0.3f, mapped);
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        Block<?> fluidBlock = Blocks.getBlock(fluidId);
        if (fluidBlock == Blocks.FLUID_WATER_FLOWING || fluidBlock == Blocks.FLUID_WATER_STILL) {
            ((IColorOverride) model).overrideColor(0, 0.5f, 1, 0.75f);
        }

        drawBlock(tessellator, model, 0);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

        if (states.get(Direction.getFromName("EAST"))) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d2 + 0.15f, (float) d4 + 0.15f, (float) d6 + 0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.66F, 0.33f, 0.33f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if (states.get(Direction.getFromName("WEST"))) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d2 + 0.15f, (float) d4 + 0.15f, (float) d6 + 0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0f, 0.33f, 0.33f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if (states.get(Direction.getFromName("UP"))) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d2 + 0.15f, (float) d4 + 0.15f, (float) d6 + 0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.66f, 0.33f);
            GL11.glScalef(mapped, 0.3f, mapped);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if (states.get(Direction.getFromName("DOWN"))) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d2 + 0.15f, (float) d4 + 0.15f, (float) d6 + 0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.0f, 0.33f);
            GL11.glScalef(mapped, 0.3f, mapped);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if (states.get(Direction.getFromName("SOUTH"))) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d2 + 0.15f, (float) d4 + 0.15f, (float) d6 + 0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.33f, 0.66f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if (states.get(Direction.getFromName("NORTH"))) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d2 + 0.15f, (float) d4 + 0.15f, (float) d6 + 0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.33f, 0.0f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }

        ((IColorOverride) model).overrideColor(1, 1, 1, 1);
    }


    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta) {
        TextureRegistry.blockAtlas.bind();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        model.renderBlockOnInventory(tessellator, meta, 1, 0.75f, null);
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private RenderBlocks blockRenderer;
}
