package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockConduit;
import sunsetsatellite.signalindustries.blocks.BlockFluidConduit;

import java.util.HashMap;

public class RenderFluidInConduit extends TileEntityRenderer<TileEntity> {
    @Override
    public void doRender(Tessellator tessellator, TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
        blockRenderer = new RenderBlocks(tileEntity1.worldObj);
        int i = tileEntity1.x;
        int j = tileEntity1.y;
        int k = tileEntity1.z;
        World blockAccess = this.renderDispatcher.renderEngine.mc.theWorld;
        Block block = SIBlocks.prototypeConduit;

        if(tileEntity1.getBlockType() != null){
            block = tileEntity1.getBlockType();
        }

        GL11.glPushMatrix();

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
                    } else if (neighbouringBlock.hasTag(SignalIndustries.ENERGY_CONDUITS_CONNECT) || neighbouringBlock.hasTag(SignalIndustries.FLUID_CONDUITS_CONNECT)) {
                        show = true;
                    }
                }
            }
            states.put(direction, show);
        }

        float amount = (fluidAmount / fluidMaxAmount);
        float mapped = (float) CatalystFluids.map(amount,0.0d,1.0d,0.0d,0.3d);
        
        GL11.glTranslatef((float)d2, (float)d4, (float)d6);
        GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.33F, 0.33f , 0.33f);
        if(!(states.get(Direction.Y_NEG) && states.get(Direction.Y_POS))){
            GL11.glScalef(0.3f, mapped,0.3f);
        } else {
            GL11.glScalef(mapped,0.3f,mapped);
        }

        GL11.glDisable(GL11.GL_LIGHTING);

        if(fluidId != -1) {
            drawBlock(tessellator, this.renderDispatcher.renderEngine, fluidId, tileEntity1);
        }
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();

        if(states.get(Direction.getFromName("EAST"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.66F, 0.33f , 0.33f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            if(fluidId != -1)
                drawBlock(tessellator, this.renderDispatcher.renderEngine, fluidId,  tileEntity1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("WEST"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0f, 0.33f , 0.33f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            if(fluidId != -1)
                drawBlock(tessellator, this.renderDispatcher.renderEngine, fluidId,  tileEntity1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("UP"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.66f , 0.33f);
            GL11.glScalef(mapped, 0.3f,mapped);
            GL11.glDisable(GL11.GL_LIGHTING);
            if(fluidId != -1)
                drawBlock(tessellator, this.renderDispatcher.renderEngine, fluidId,  tileEntity1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("DOWN"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.0f , 0.33f);
            GL11.glScalef(mapped, 0.3f,mapped);
            GL11.glDisable(GL11.GL_LIGHTING);
            if(fluidId != -1)
                drawBlock(tessellator, this.renderDispatcher.renderEngine, fluidId,  tileEntity1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("SOUTH"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.33f , 0.66f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            if(fluidId != -1)
                drawBlock(tessellator, this.renderDispatcher.renderEngine, fluidId,  tileEntity1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
        if(states.get(Direction.getFromName("NORTH"))){
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0f, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.33F, 0.33f , 0.0f);
            GL11.glScalef(0.3f, mapped, 0.3f);
            GL11.glDisable(GL11.GL_LIGHTING);
            if(fluidId != -1)
                drawBlock(tessellator, this.renderDispatcher.renderEngine, fluidId, tileEntity1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }


    public void drawBlock(Tessellator tessellator, RenderEngine renderengine, int i, TileEntity tile) {
        Block block = Block.blocksList[i];
        GL11.glPushMatrix();
        this.blockRenderer.renderStandardBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(block),block,tile.x,tile.y,tile.z);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private RenderBlocks blockRenderer;
}
