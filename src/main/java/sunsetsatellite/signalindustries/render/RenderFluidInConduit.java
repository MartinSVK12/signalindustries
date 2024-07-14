package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IColorOverride;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.multiblocks.HologramWorld;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockConduit;
import sunsetsatellite.signalindustries.blocks.BlockFluidConduit;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderFluidInConduit extends TileEntityRenderer<TileEntity> {
    @Override
    public void doRender(Tessellator tessellator, TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
        blockRenderer = new RenderBlocks(tileEntity1.worldObj);
        int i = tileEntity1.x;
        int j = tileEntity1.y;
        int k = tileEntity1.z;
        World blockAccess = tileEntity1.worldObj;
        Block block = SIBlocks.prototypeConduit;

        if(tileEntity1.getBlockType() != null){
            block = tileEntity1.getBlockType();
        }

        float fluidAmount = 0;
        float fluidMaxAmount = 1;
        int fluidId = -1;

        if(Minecraft.getMinecraft(Minecraft.class).theWorld.isClientSide){
            if(((TileEntityFluidContainer) tileEntity1).shownFluid != null){
                fluidId = ((TileEntityFluidContainer) tileEntity1).shownFluid.getLiquid().id;
                fluidAmount = ((TileEntityFluidContainer) tileEntity1).shownFluid.amount;
                fluidMaxAmount = ((TileEntityFluidContainer) tileEntity1).shownMaxAmount;
            }
        } else {
            if(((TileEntityFluidContainer) tileEntity1).fluidContents[0] != null){
                if(((TileEntityFluidContainer) tileEntity1).fluidContents[0].getLiquid() != null){
                    fluidMaxAmount = ((TileEntityFluidContainer) tileEntity1).getFluidCapacityForSlot(0);
                    fluidAmount = ((TileEntityFluidContainer) tileEntity1).fluidContents[0].amount;
                    fluidId = ((TileEntityFluidContainer) tileEntity1).fluidContents[0].getLiquid().id;
                }
            }
        }

        BlockFluid fluidBlock = (BlockFluid) Block.getBlock(fluidId);

        BlockModel<?> model = null;
        if(fluidId != -1){
            blockRenderer = new RenderBlocks(new HologramWorld((ArrayList<BlockInstance>) Catalyst.listOf(new BlockInstance(fluidBlock,new Vec3i(),0,null))));
            model = BlockModelDispatcher.getInstance().getDispatch(fluidBlock);
        }

        if(model == null) return;

        fluidAmount = Math.min(fluidAmount,fluidMaxAmount);

        HashMap<Direction, Boolean> states = new HashMap<>();
        for (Direction direction : Direction.values()) {
            boolean show = false;
            Vec3i offset = new Vec3i(i,j,k).add(direction.getVec());
            Block neighbouringBlock = blockAccess.getBlock(offset.x, offset.y, offset.z);
            if(neighbouringBlock != null) {
                if(block.getClass().isAssignableFrom(neighbouringBlock.getClass())){
                    show = true;
                } else if(!(neighbouringBlock instanceof BlockConduit || neighbouringBlock instanceof BlockFluidConduit)) {
                    if(neighbouringBlock instanceof BlockTileEntity){
                        TileEntity neighbouringTile = blockAccess.getBlockTileEntity(offset.x, offset.y, offset.z);
                        if(neighbouringTile instanceof IFluidInventory){
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
        float mapped = (float) CatalystFluids.map(amount,0.0d,1.0d,0.0d,0.3d);

        GL11.glPushMatrix();
        GL11.glTranslatef((float)d2+0.15f, (float)d4+0.15f, (float)d6+0.15f);
        GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.33F, 0.33f , 0.33f);
        if(!(states.get(Direction.Y_NEG) && states.get(Direction.Y_POS))){
            GL11.glScalef(0.3f, mapped,0.3f);
        } else {
            GL11.glScalef(mapped,0.3f,mapped);
        }

        GL11.glDisable(GL11.GL_LIGHTING);

        if(fluidBlock == Block.fluidWaterFlowing || fluidBlock == Block.fluidWaterStill){
            ((IColorOverride)model).overrideColor(0,0.5f,1,0.75f);
        }

        drawBlock(tessellator, model, 0);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

        if(states.get(Direction.getFromName("EAST"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2+0.15f, (float)d4+0.15f, (float)d6+0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.66F, 0.33f , 0.33f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model,0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("WEST"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2+0.15f, (float)d4+0.15f, (float)d6+0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0f, 0.33f , 0.33f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model,0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("UP"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2+0.15f, (float)d4+0.15f, (float)d6+0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.66f , 0.33f);
            GL11.glScalef(mapped, 0.3f,mapped);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model,0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("DOWN"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2+0.15f, (float)d4+0.15f, (float)d6+0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.0f , 0.33f);
            GL11.glScalef(mapped, 0.3f,mapped);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model,0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("SOUTH"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2+0.15f, (float)d4+0.15f, (float)d6+0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.33f , 0.66f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model,0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("NORTH"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2+0.15f, (float)d4+0.15f, (float)d6+0.15f);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.33f , 0.0f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            drawBlock(tessellator, model,0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }

        ((IColorOverride) model).overrideColor(1, 1, 1, 1);
    }


    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta) {
        TextureRegistry.blockAtlas.bindTexture();
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
