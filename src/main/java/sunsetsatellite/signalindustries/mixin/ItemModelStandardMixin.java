package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = ItemModelStandard.class,remap = false)
public abstract class ItemModelStandardMixin extends ItemModel {


    @Shadow public abstract void renderFlat(Tessellator tessellator, IconCoordinate index);

    @Shadow protected boolean useColor;

    @Shadow public abstract int getColor(ItemStack stack);

    @Shadow protected boolean itemfullBright;

    private ItemModelStandardMixin(Item item) {
        super(item);
    }

    @Inject(method = "renderAsItemEntity",at = @At("HEAD"),cancellable = true)
    public void renderAsItemEntity(Tessellator tessellator, Entity entity, Random random, ItemStack itemstack, int renderCount, float yaw, float brightness, float partialTick, CallbackInfo ci) {
        if(entity == null){
            Minecraft mc = Minecraft.getMinecraft(this);
            if (mc.fullbright || this.itemfullBright) {
                brightness = 1.0F;
            }

            EntityRenderDispatcher renderDispatcher = EntityRenderDispatcher.instance;
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            IconCoordinate tex = this.getIcon(entity, itemstack);
            tex.parentAtlas.bindTexture();
            int i;
            float rOffX;
            float rOffY;
            float rOffZ;
            if (this.useColor) {
                i = this.getColor(itemstack);
                rOffX = (float)(i >> 16 & 255) / 255.0F;
                rOffY = (float)(i >> 8 & 255) / 255.0F;
                rOffZ = (float)(i & 255) / 255.0F;
                GL11.glColor4f(rOffX * brightness, rOffY * brightness, rOffZ * brightness, 1.0F);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, 1.0F);
            }

            if (mc.gameSettings.items3D.value) {
                GL11.glPushMatrix();
                GL11.glScaled(1.0, 1.0, 1.0);
                GL11.glRotated(yaw, 0.0, 1.0, 0.0);
                GL11.glTranslated(-0.5, -0.3, -0.05 * (double)(renderCount - 1));

                for(i = 0; i < renderCount; ++i) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(0.0, -0.3, 0.1 * (double)i);
                    this.renderItem(tessellator, renderDispatcher.itemRenderer, itemstack, entity, brightness, false);
                    GL11.glPopMatrix();
                }

                GL11.glPopMatrix();
            } else {
                for(i = 0; i < renderCount; ++i) {
                    GL11.glPushMatrix();

                    GL11.glRotatef(180.0F - renderDispatcher.viewLerpYaw, 0.0F, 1.0F, 0.0F);
                    this.renderFlat(tessellator, tex);
                    GL11.glPopMatrix();
                }
            }
            ci.cancel();
        }
    }
}
