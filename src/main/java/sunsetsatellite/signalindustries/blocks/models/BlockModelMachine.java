package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.MachineTextures;

import java.util.HashMap;

public class BlockModelMachine extends BlockModelCoverable {

    protected MachineTextures machineTextures = new MachineTextures();

    public BlockModelMachine(Block<? extends BlockLogic> block) {
        super(block);
    }

    public BlockModelMachine(Block<? extends BlockLogic> block, MachineTextures machineTextures) {
        super(block);
        this.machineTextures = machineTextures;
    }

    public BlockModelMachine withTextures(MachineTextures machineTextures) {
        this.machineTextures = machineTextures;
        return this;
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof IHasIOPreview) {
            if (((IHasIOPreview) tileEntity).getPreview() != IOPreview.NONE) {
                return super.getBlockOverbrightTexture(blockAccess, x, y, z, side);
            }
        }
        if (tileEntity instanceof IActiveForm) {
            if (((IActiveForm) tileEntity).isBurning() && !((IActiveForm) tileEntity).isDisabled()) {
                int data = blockAccess.getBlockMetadata(x, y, z);
                int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side];
                if (index >= Sides.orientationLookUpHorizontal.length) return null;

                Side id = Side.getSideById(index);

                return machineTextures.overbrightTextures.get(id) == null ? null : TextureRegistry.getTexture(machineTextures.overbrightTextures.get(id));
            }
        }
        return null;
    }

    @Override
    public IconCoordinate getBlockOverbrightTextureFromSideAndMeta(Side side, int data) {
       /* int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return null;

        Side id = Side.getSideById(index);*/

        return super.getBlockOverbrightTextureFromSideAndMeta(side,data); //overbrightTextures.get(id);
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        HashMap<Side, String> usingTextures = machineTextures.defaultTextures;
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof IActiveForm) {
            if (((IActiveForm) tileEntity).isBurning() && !((IActiveForm) tileEntity).isDisabled()) {
                usingTextures = machineTextures.activeTextures;
            }
        }

        int data = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return TextureRegistry.getTexture(usingTextures.get(id));
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return TextureRegistry.getTexture(machineTextures.defaultTextures.get(id));
    }
}
