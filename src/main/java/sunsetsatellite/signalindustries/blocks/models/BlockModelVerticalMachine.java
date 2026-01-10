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
import sunsetsatellite.signalindustries.util.MachineTextures;
import sunsetsatellite.signalindustries.util.VerticalMachineTextures;

import java.util.HashMap;

public class BlockModelVerticalMachine extends BlockModelMachine {

    protected VerticalMachineTextures verticalMachineTextures = new VerticalMachineTextures();

    public BlockModelVerticalMachine(Block<? extends BlockLogic> block) {
        super(block);
    }

    public BlockModelVerticalMachine(Block<? extends BlockLogic> block, MachineTextures machineTextures) {
        super(block, machineTextures);
    }

    public BlockModelVerticalMachine(Block<? extends BlockLogic> block, MachineTextures machineTextures, VerticalMachineTextures verticalMachineTextures) {
        super(block, machineTextures);
        this.verticalMachineTextures = verticalMachineTextures;
    }

    public BlockModelMachine withVerticalTextures(VerticalMachineTextures machineTextures) {
        this.verticalMachineTextures = machineTextures;
        return this;
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof IActiveForm) {
            if (((IActiveForm) tileEntity).isBurning()) {
                int data = blockAccess.getBlockMetadata(x, y, z);
                boolean isVertical = data == 0 || data == 1;
                int index;
                if (isVertical) {
                    index = VerticalMachineTextures.orientationLookUpVertical[6 * data + side];
                } else {
                    index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side];
                }
                if (index >= Sides.orientationLookUpHorizontal.length) return null;

                Side id = Side.getSideById(index);

                return isVertical ? (verticalMachineTextures.overbrightVerticalTextures.get(id) == null ? null : TextureRegistry.getTexture(verticalMachineTextures.overbrightVerticalTextures.get(id))) : (machineTextures.overbrightTextures.get(id) == null ? null : TextureRegistry.getTexture(machineTextures.overbrightTextures.get(id)));
            }
        }
        return null;
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int data = blockAccess.getBlockMetadata(x, y, z);
        boolean isVertical = data == 0 || data == 1;
        HashMap<Side, String> usingTextures = machineTextures.defaultTextures;
        if (isVertical) {
            usingTextures = verticalMachineTextures.defaultVerticalTextures;
        }
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof IActiveForm) {
            if (((IActiveForm) tileEntity).isBurning()) {
                usingTextures = machineTextures.activeTextures;
                if (isVertical) {
                    usingTextures = verticalMachineTextures.activeVerticalTextures;
                }
            }
        }

        int index;
        if (isVertical) {
            index = VerticalMachineTextures.orientationLookUpVertical[6 * data + side.getId()];
        } else {
            index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        }
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return TextureRegistry.getTexture(usingTextures.get(id));
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        boolean isVertical = data == 0 || data == 1;
        int index;
        if (isVertical) {
            index = VerticalMachineTextures.orientationLookUpVertical[6 * data + side.getId()];
        } else {
            index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        }

        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return isVertical ? TextureRegistry.getTexture(verticalMachineTextures.defaultVerticalTextures.get(id)) : TextureRegistry.getTexture(machineTextures.defaultTextures.get(id));
    }

    public VerticalMachineTextures verticalTextures() {
        return verticalMachineTextures;
    }

    public MachineTextures textures() {
        return machineTextures;
    }
}
