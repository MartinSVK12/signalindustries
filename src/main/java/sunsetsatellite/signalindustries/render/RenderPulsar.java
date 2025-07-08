package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.BlockLogic;
import org.lwjgl.opengl.GL11;
import org.useless.DragonFly;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.dragonfly.models.block.BlockModelDFJava;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.blocks.states.RotatableStateInterpreter;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;

import static sunsetsatellite.signalindustries.SIBlocks.pulsarBlock;

public class RenderPulsar extends TileEntityRenderer<TileEntityPulsar> {

    @Override
    public void doRender(Tessellator tessellator, TileEntityPulsar tile, double x, double y, double z, float partialTick) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.25f, (float) z + 0.5f);
        if(tile.isBurning()){
            GL11.glRotatef(tile.orbRotation * 20 + partialTick,0,1,0);
        } else {
            GL11.glTranslatef(0f, -1f, 0f);
        }
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar.png").bind();
        if(tile.getItem(0) != null && tile.getItem(0).getItem() instanceof ItemWarpOrb){
            Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar_warp.png").bind();
        }
        StaticEntityModel item = DragonFly.loadEntityModel("geometry.signalindustries.pulsar_item", 0);
        StaticEntityModel innerCore = DragonFly.loadEntityModel("geometry.signalindustries.pulsar_inner_core", 0);
        StaticEntityModel outerCore = DragonFly.loadEntityModel("geometry.signalindustries.pulsar_outer_core", 0);
        GL11.glScalef(0.0625f, 0.0625f, -0.0625f);
        if(tile.fuelBurnTicks <= 0){
            item.render(tessellator);
        }
        if(tile.progressTicks > tile.progressMaxTicks/2){
            innerCore.render(tessellator);
        }
        if (tile.progressTicks >= tile.progressMaxTicks) {
            outerCore.render(tessellator);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
