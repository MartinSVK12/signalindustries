package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = ItemModelBlock.class,remap = false)
public abstract class ItemModelBlockMixin extends ItemModelStandard {
    @Shadow private BlockModel<Block> blockModel;

    private ItemModelBlockMixin(Item item, String namespace) {
        super(item, namespace);
    }

    @Inject(method = "renderAsItemEntity",at = @At("HEAD"),cancellable = true)
    public void renderAsItemEntity(Tessellator tessellator, Entity entity, Random random, ItemStack itemstack, int renderCount, float yaw, float brightness, float partialTick, CallbackInfo ci) {
        if(entity == null){
            if (Block.blocksList[itemstack.itemID] != null && ((BlockModel<?>) BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemstack.itemID])).shouldItemRender3d()) {
                GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
                TextureRegistry.blockAtlas.bindTexture();
                float itemSize = this.blockModel.getItemRenderScale();
                GL11.glScalef(itemSize, itemSize, itemSize);

                for(int i = 0; i < renderCount; ++i) {
                    GL11.glPushMatrix();

                    if (LightmapHelper.isLightmapEnabled()) {
                        brightness = 1.0F;
                        LightmapHelper.setLightmapCoord(LightmapHelper.getLightmapCoord(15,15));
                    }

                    if (Global.accessor.isFullbrightEnabled() || this.itemfullBright) {
                        brightness = 1.0F;
                    }

                    this.blockModel.renderBlockOnInventory(tessellator, itemstack.getMetadata(), brightness, null);
                    GL11.glPopMatrix();
                }
            } else {
                super.renderAsItemEntity(tessellator, entity, random, itemstack, renderCount, yaw, brightness, partialTick);
            }
            ci.cancel();
        }
    }
}
