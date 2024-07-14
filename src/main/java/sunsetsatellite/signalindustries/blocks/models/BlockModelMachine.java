package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.HashMap;

public class BlockModelMachine extends BlockModelCoverable {

    protected HashMap<Side, IconCoordinate> defaultTextures = (HashMap<Side, IconCoordinate>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new IconCoordinate[Side.values().length], BLOCK_TEXTURE_UNASSIGNED));
    protected HashMap<Side, IconCoordinate> activeTextures = (HashMap<Side, IconCoordinate>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new IconCoordinate[Side.values().length], BLOCK_TEXTURE_UNASSIGNED));
    protected HashMap<Side, IconCoordinate> overbrightTextures = (HashMap<Side, IconCoordinate>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new IconCoordinate[Side.values().length], null));

    public BlockModelMachine(Block block) {
        super(block);
        hasOverbright = true;

    }

    public BlockModelMachine(Block block, Tier tier) {
        super(block);

        switch (tier) {
            case PROTOTYPE:
                withDefaultTexture("prototype_blank");
                withActiveTexture("prototype_blank");
                break;
            case BASIC:
                withDefaultTexture("basic_blank");
                withActiveTexture("basic_blank");
                break;
            case REINFORCED:
                withDefaultTexture("reinforced_blank");
                withActiveTexture("reinforced_blank");
                break;
            case AWAKENED:
                withDefaultTexture("awakened_blank");
                withActiveTexture("awakened_blank");
                break;
        }
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getBlockTileEntity(x, y, z);
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

                return overbrightTextures.get(id);
            }
        }
        return null;
    }

    @Override
    public IconCoordinate getBlockOverbrightTextureFromSideAndMeta(Side side, int data) {
       /* int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return null;

        Side id = Side.getSideById(index);*/

        return null; //overbrightTextures.get(id);
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        HashMap<Side, IconCoordinate> usingTextures = defaultTextures;
        TileEntity tileEntity = blockAccess.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof IActiveForm) {
            if (((IActiveForm) tileEntity).isBurning() && !((IActiveForm) tileEntity).isDisabled()) {
                usingTextures = activeTextures;
            }
        }

        int data = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return usingTextures.get(id);
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return defaultTextures.get(id);
    }

    public BlockModelMachine withDefaultTexture(String texture) {
        defaultTextures.replaceAll((S, I) -> TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withActiveTexture(String texture) {
        activeTextures.replaceAll((S, I) -> TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withOverbrightTexture(String texture) {
        overbrightTextures.replaceAll((S, I) -> TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withDefaultSideTextures(String texture) {
        defaultTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return TextureRegistry.getTexture("signalindustries:block/" + texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelMachine withActiveSideTextures(String texture) {
        activeTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return TextureRegistry.getTexture("signalindustries:block/" + texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelMachine withOverbrightSideTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return TextureRegistry.getTexture("signalindustries:block/" + texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelMachine withDefaultTopBottomTextures(String texture) {
        defaultTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return TextureRegistry.getTexture("signalindustries:block/" + texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelMachine withActiveTopBottomTextures(String texture) {
        activeTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return TextureRegistry.getTexture("signalindustries:block/" + texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelMachine withOverbrightTopBottomTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return TextureRegistry.getTexture("signalindustries:block/" + texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelMachine withDefaultTopTexture(String texture) {
        defaultTextures.replace(Side.TOP, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withActiveTopTexture(String texture) {
        activeTextures.replace(Side.TOP, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withOverbrightTopTexture(String texture) {
        overbrightTextures.replace(Side.TOP, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withDefaultBottomTexture(String texture) {
        defaultTextures.replace(Side.BOTTOM, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withActiveBottomTexture(String texture) {
        activeTextures.replace(Side.BOTTOM, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withOverbrightBottomTexture(String texture) {
        overbrightTextures.replace(Side.BOTTOM, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withDefaultNorthTexture(String texture) {
        defaultTextures.replace(Side.NORTH, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withActiveNorthTexture(String texture) {
        activeTextures.replace(Side.NORTH, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withOverbrightNorthTexture(String texture) {
        overbrightTextures.replace(Side.NORTH, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withDefaultSouthTexture(String texture) {
        defaultTextures.replace(Side.SOUTH, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withActiveSouthTexture(String texture) {
        activeTextures.replace(Side.SOUTH, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withOverbrightSouthTexture(String texture) {
        overbrightTextures.replace(Side.SOUTH, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withDefaultWestTexture(String texture) {
        defaultTextures.replace(Side.WEST, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withActiveWestTexture(String texture) {
        activeTextures.replace(Side.WEST, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withOverbrightWestTexture(String texture) {
        overbrightTextures.replace(Side.WEST, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withDefaultEastTexture(String texture) {
        defaultTextures.replace(Side.EAST, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withActiveEastTexture(String texture) {
        activeTextures.replace(Side.EAST, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }

    public BlockModelMachine withOverbrightEastTexture(String texture) {
        overbrightTextures.replace(Side.EAST, TextureRegistry.getTexture("signalindustries:block/" + texture));
        return this;
    }


}
