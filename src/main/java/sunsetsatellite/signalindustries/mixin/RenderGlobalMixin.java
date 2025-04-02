package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.worldtype.WorldTypeFX;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.util.phys.Vec3;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.model.IFullbright;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIWeather;
import sunsetsatellite.signalindustries.abilities.trigger.ScanAbility;

import java.util.ArrayList;

@Mixin(
        value = RenderGlobal.class,
        remap = false
)
public class RenderGlobalMixin {

    @Shadow @Final private Minecraft mc;

    @Shadow private WorldClient worldObj;

    @Shadow @Final private TextureManager textureManager;

    @Shadow @Final private int starGLCallList;

    @Inject(
            method = "drawSky",
            at = @At("HEAD")
    )
    public void eternitySky(float partialTick, CallbackInfo ci){
        if(this.mc.currentWorld.dimension == SIDimensions.ETERNITY){
            textureManager.loadTexture("/assets/signalindustries/textures/colormap/stars/default.png").bind();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glCallList(starGLCallList);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glPopMatrix();
            GL11.glColor3f(1.0F, 1.0F, 1.0F);

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
        }
    }

    @Inject(
            method = "drawSky",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 1, shift = At.Shift.AFTER)
    )
    public void renderBloodMoon(float partialTicks, CallbackInfo ci) {
        if(worldObj.getCurrentWeather() == SIWeather.weatherBloodMoon){
            GL11.glColor4f(1.0f,0.0f,0.0f,1.0f);
        }
    }

    @Inject(
            method = "drawSky",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 1, shift = At.Shift.AFTER)
    )
    public void renderMeteorShower(float partialTicks, CallbackInfo ci, @Local(name = "sunAlpha") LocalFloatRef f6) {
        if(worldObj.getCurrentWeather() == SIWeather.weatherMeteorShower){
            f6.set(1.5f);
            GL11.glColor4f( 1,1, 1,1.0f);
        }
    }

    @Inject(
            method = "drawSky",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/render/tessellator/Tessellator;draw()V", ordinal = 1, shift = At.Shift.AFTER)
    )
    public void renderSolar(float partialTick, CallbackInfo ci, @Local Tessellator t) {
        if(worldObj.getCurrentWeather() == SIWeather.weatherEclipse){
            float size = 30F;
            textureManager.loadTexture("/assets/signalindustries/misc/solar_eclipse.png").bind();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0f);
            t.startDrawingQuads();
            t.addVertexWithUV(-size, 100D, -size, 0.0D, 0.0D);
            t.addVertexWithUV(size, 100D, -size, 1.0D, 0.0D);
            t.addVertexWithUV(size, 100D, size, 1.0D, 1.0D);
            t.addVertexWithUV(-size, 100D, size, 0.0D, 1.0D);
            t.draw();
        }
    }

    @Inject(method = "renderEntities", at = @At("TAIL"))
    public void renderWorld(ICamera camera, float partialTick, CallbackInfo ci){
        double x = camera.getX(partialTick);
        double y = camera.getY(partialTick);
        double z = camera.getZ(partialTick);
        if(!ScanAbility.oreMap.isEmpty()){
            ArrayList<BlockInstance> list = new ArrayList<>();
            ScanAbility.oreMap.forEach((block, oreInfo)->{
                oreInfo.positions.forEach(position->{
                    list.add(new BlockInstance(block,position,null));
                });
            });
            blockRenderer = new RenderBlocks(new HologramWorld(list));
            ScanAbility.oreMap.forEach((block, oreInfo)->{
                oreInfo.positions.forEach(position->{
                    GL11.glPushMatrix();
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
                    GL11.glTranslated(position.x - x + 0.5f , position.y - y + 0.5f, position.z - z + 0.5f);
                    ((IFullbright)model).enableFullbright();
                    drawBlock(Tessellator.instance,
                            model
                    );
                    ((IFullbright)model).disableFullbright();
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glPopMatrix();
                });
            });
        }
    }

    @Unique
    private void drawBlock(Tessellator tessellator, BlockModel<?> model) {
        TextureRegistry.blockAtlas.bind();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        model.renderBlockOnInventory(tessellator, 0,1,null);
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    @Unique
    private RenderBlocks blockRenderer;

}
