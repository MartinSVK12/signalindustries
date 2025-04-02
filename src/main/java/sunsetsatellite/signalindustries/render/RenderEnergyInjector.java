package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.entity.EntityItem;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEnergyInjector;

import java.util.Random;

public class RenderEnergyInjector extends TileEntityRenderer<TileEntityEnergyInjector> {

    private final Random random = new Random();
    private final EntityRendererFakeItem itemRenderer = new EntityRendererFakeItem();
    private int counter = 0;

    @Override
    public void doRender(Tessellator tessellator, TileEntityEnergyInjector tileEntity, double x, double y, double z, float g) {
        counter++;
        if(counter >= 360){
            counter = 0;
        }

        if(tileEntity.getItem(0) != null){
            EntityItem entityItem = new EntityItem(tileEntity.worldObj, tileEntity.x, tileEntity.y, tileEntity.z, tileEntity.getItem(0));
            itemRenderer.render(tessellator, entityItem,x+0.5,y+0.5,z+0.5, counter, g);
        }

    }
}
