package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.model.IFullbright;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.render.entity.EntityRendererFakeItem;
import sunsetsatellite.signalindustries.tiles.TileEntityItemConduit;

import java.util.Random;

public class RenderItemsInConduit extends TileEntityRenderer<TileEntityItemConduit> {

    private final Random random = new Random();
    private final EntityRendererFakeItem itemRenderer = new EntityRendererFakeItem();

    @Override
    public void doRender(Tessellator tessellator, TileEntityItemConduit tileEntity, double x, double y, double z, float g) {
        for (TileEntityItemConduit.PipeItem content : tileEntity.getContents()) {
            Direction begin = content.getEntry();
            Direction end = content.getExit();
            Vec3f beginVec = content.getEntry().getVecF();
            Axis beginAxis = begin.getAxis();
            Vec3f endVec = content.getExit().getVecF();
            Axis endAxis = end.getAxis();
            double v = 0;
            boolean positive = (begin == Direction.Z_POS || begin == Direction.Y_POS || begin == Direction.X_POS);
            v = Catalyst.map(content.getTicks() + g, TileEntityItemConduit.TRANSFER_TICKS, 0, 1, -1);
            Vec3f base = new Vec3f(0.5d);
            Vec3f pos = new Vec3f(x, y, z);
            float lerped = 0;
            Vec3f offset = new Vec3f(0);
            switch (beginAxis) {
                case X:
                    lerped = MathHelper.lerp((float) beginVec.x, (float) base.x, (float) v);
                    if (!positive) {
                        lerped = (float) Catalyst.map(lerped, -2.5f, 0.5f, -0.5f, 0.5f);
                    }
                    offset.x += lerped;
                    offset.y += base.y;
                    offset.z += base.z;
                    break;
                case Y:
                    lerped = MathHelper.lerp((float) beginVec.y, (float) base.y, (float) v);
                    if (!positive) {
                        lerped = (float) Catalyst.map(lerped, -2.5f, 0.5f, -0.5f, 0.5f);
                    }
                    offset.x += base.x;
                    offset.y += lerped;
                    offset.z += base.z;
                    break;
                case Z:
                    lerped = MathHelper.lerp((float) beginVec.z, (float) base.z, (float) v);
                    if (!positive) {
                        lerped = (float) Catalyst.map(lerped, -2.5f, 0.5f, -0.5f, 0.5f);
                    }
                    offset.x += base.x;
                    offset.y += base.y;
                    offset.z += lerped;
                    break;
                default:
                    break;
            }
            Vec3f p = pos.copy().add(offset);
            ItemModel model = ItemModelDispatcher.getInstance().getDispatch(content.getStack());
            ((IFullbright) model).enableFullbright();
            if (model instanceof ItemModelBlock) {
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[content.getStack().itemID]);
                ((IFullbright) blockModel).enableFullbright();
            }
            GL11.glPushMatrix();
            if (content.getStack().itemID > 16384) {
                p.y -= 0.25f;
            }
            itemRenderer.render(tessellator, content.getStack(), p.x, p.y, p.z, 0, g);
            ((IFullbright) model).disableFullbright();
            if (model instanceof ItemModelBlock) {
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Blocks.blocksList[content.getStack().itemID]);
                ((IFullbright) blockModel).disableFullbright();
            }
            GL11.glPopMatrix();
        }
    }
}
