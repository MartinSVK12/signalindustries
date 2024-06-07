package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.HashMap;

public class BlockModelVerticalMachine extends BlockModelMachine{

    protected HashMap<Side, IconCoordinate> defaultVerticalTextures = (HashMap<Side, IconCoordinate>) SignalIndustries.mapOf(Side.values(),SignalIndustries.arrayFill(new IconCoordinate[Side.values().length],BLOCK_TEXTURE_UNASSIGNED));
    protected HashMap<Side, IconCoordinate> activeVerticalTextures = (HashMap<Side, IconCoordinate>) SignalIndustries.mapOf(Side.values(),SignalIndustries.arrayFill(new IconCoordinate[Side.values().length],BLOCK_TEXTURE_UNASSIGNED));
    protected HashMap<Side, IconCoordinate> overbrightVerticalTextures = (HashMap<Side, IconCoordinate>) SignalIndustries.mapOf(Side.values(),SignalIndustries.arrayFill(new IconCoordinate[Side.values().length],BLOCK_TEXTURE_UNASSIGNED));
    public static int[] orientationLookUpVertical = new int[]{1, 0, 2, 3, 4, 5,  0, 1, 2, 3, 4, 5};


    public BlockModelVerticalMachine(Block block) {
        super(block);
    }

    public BlockModelVerticalMachine(Block block, Tier tier) {
        super(block, tier);
        switch (tier){
            case PROTOTYPE:
                withVerticalDefaultTexture("prototype_blank");
                withVerticalActiveTexture("prototype_blank");
                break;
            case BASIC:
                withVerticalDefaultTexture("basic_blank");
                withVerticalActiveTexture("basic_blank");
                break;
            case REINFORCED:
                withVerticalDefaultTexture("reinforced_blank");
                withVerticalActiveTexture("reinforced_blank");
                break;
            case AWAKENED:
                withVerticalDefaultTexture("awakened_blank");
                withVerticalActiveTexture("awakened_blank");
                break;
        }
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getBlockTileEntity(x,y,z);
        if(tileEntity instanceof IActiveForm){
            if(((IActiveForm) tileEntity).isBurning()){
                int data = blockAccess.getBlockMetadata(x, y, z);
                boolean isVertical = data == 0 || data == 1;
                int index;
                if(isVertical) {
                    index = orientationLookUpVertical[6 * data + side];
                } else {
                    index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side];
                }
                if (index >= Sides.orientationLookUpHorizontal.length) return null;

                Side id = Side.getSideById(index);

                return isVertical ? overbrightVerticalTextures.get(id) : overbrightTextures.get(id);
            }
        }
        return null;
    }

    @Override
    public IconCoordinate getBlockOverbrightTextureFromSideAndMeta(Side side, int data) {
        /*int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return null;

        Side id = Side.getSideById(index);*/

        return null; //overbrightTextures.get(id);
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int data = blockAccess.getBlockMetadata(x, y, z);
        boolean isVertical = data == 0 || data == 1;
        HashMap<Side, IconCoordinate> usingTextures = defaultTextures;
        if(isVertical){
            usingTextures = defaultVerticalTextures;
        }
        TileEntity tileEntity = blockAccess.getBlockTileEntity(x,y,z);
        if(tileEntity instanceof IActiveForm){
            if(((IActiveForm) tileEntity).isBurning()){
                usingTextures = activeTextures;
                if(isVertical){
                    usingTextures = activeVerticalTextures;
                }
            }
        }

        int index;
        if(isVertical) {
            index = orientationLookUpVertical[6 * data + side.getId()];
        } else {
            index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        }
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return usingTextures.get(id);
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        boolean isVertical = data == 0 || data == 1;
        int index;
        if(isVertical) {
            index = orientationLookUpVertical[6 * data + side.getId()];
        } else {
            index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        }

        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return isVertical ? defaultVerticalTextures.get(id) : defaultTextures.get(id);
    }

    public BlockModelVerticalMachine withVerticalDefaultTexture(String texture) {
        defaultVerticalTextures.replaceAll((S,I)-> TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveTexture(String texture) {
        activeVerticalTextures.replaceAll((S,I)-> TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightTexture(String texture) {
        overbrightVerticalTextures.replaceAll((S,I)-> TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultSideTextures(String texture) {
        defaultVerticalTextures.replaceAll((S,I)-> {
            if(S.isHorizontal()){
                return TextureRegistry.getTexture("signalindustries:block/"+texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveSideTextures(String texture) {
        activeVerticalTextures.replaceAll((S,I)-> {
            if(S.isHorizontal()){
                return TextureRegistry.getTexture("signalindustries:block/"+texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightSideTextures(String texture) {
        overbrightVerticalTextures.replaceAll((S,I)-> {
            if(S.isHorizontal()){
                return TextureRegistry.getTexture("signalindustries:block/"+texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultTopBottomTextures(String texture) {
        defaultVerticalTextures.replaceAll((S,I)-> {
            if(S.isVertical()){
                return TextureRegistry.getTexture("signalindustries:block/"+texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveTopBottomTextures(String texture) {
        activeVerticalTextures.replaceAll((S,I)-> {
            if(S.isVertical()){
                return TextureRegistry.getTexture("signalindustries:block/"+texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightTopBottomTextures(String texture) {
        overbrightVerticalTextures.replaceAll((S,I)-> {
            if(S.isVertical()){
                return TextureRegistry.getTexture("signalindustries:block/"+texture);
            }
            return I;
        });
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultTopTexture(String texture) {
        defaultVerticalTextures.replace(Side.TOP,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveTopTexture(String texture) {
        activeVerticalTextures.replace(Side.TOP,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightTopTexture(String texture) {
        overbrightVerticalTextures.replace(Side.TOP,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultBottomTexture(String texture) {
        defaultVerticalTextures.replace(Side.BOTTOM,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveBottomTexture(String texture) {
        activeVerticalTextures.replace(Side.BOTTOM,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightBottomTexture(String texture) {
        overbrightVerticalTextures.replace(Side.BOTTOM,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultNorthTexture(String texture) {
        defaultVerticalTextures.replace(Side.NORTH,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveNorthTexture(String texture) {
        activeVerticalTextures.replace(Side.NORTH,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightNorthTexture(String texture) {
        overbrightVerticalTextures.replace(Side.NORTH,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultSouthTexture(String texture) {
        defaultVerticalTextures.replace(Side.SOUTH,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveSouthTexture(String texture) {
        activeVerticalTextures.replace(Side.SOUTH,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightSouthTexture(String texture) {
        overbrightVerticalTextures.replace(Side.SOUTH,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultWestTexture(String texture) {
        defaultVerticalTextures.replace(Side.WEST,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveWestTexture(String texture) {
        activeVerticalTextures.replace(Side.WEST,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightWestTexture(String texture) {
        overbrightVerticalTextures.replace(Side.WEST,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalDefaultEastTexture(String texture) {
        defaultVerticalTextures.replace(Side.EAST,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalActiveEastTexture(String texture) {
        activeVerticalTextures.replace(Side.EAST,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }

    public BlockModelVerticalMachine withVerticalOverbrightEastTexture(String texture) {
        overbrightVerticalTextures.replace(Side.EAST,TextureRegistry.getTexture("signalindustries:block/"+texture));
        return this;
    }


    @Override
    public BlockModelVerticalMachine withDefaultTexture(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveTexture(String texture) {
        return (BlockModelVerticalMachine) super.withActiveTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightTexture(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultSideTextures(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultSideTextures(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveSideTextures(String texture) {
        return (BlockModelVerticalMachine) super.withActiveSideTextures(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightSideTextures(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightSideTextures(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultTopBottomTextures(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultTopBottomTextures(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveTopBottomTextures(String texture) {
        return (BlockModelVerticalMachine) super.withActiveTopBottomTextures(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightTopBottomTextures(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightTopBottomTextures(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultTopTexture(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultTopTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveTopTexture(String texture) {
        return (BlockModelVerticalMachine) super.withActiveTopTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightTopTexture(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightTopTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultBottomTexture(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultBottomTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveBottomTexture(String texture) {
        return (BlockModelVerticalMachine) super.withActiveBottomTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightBottomTexture(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightBottomTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultNorthTexture(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultNorthTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveNorthTexture(String texture) {
        return (BlockModelVerticalMachine) super.withActiveNorthTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightNorthTexture(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightNorthTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultSouthTexture(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultSouthTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveSouthTexture(String texture) {
        return (BlockModelVerticalMachine) super.withActiveSouthTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightSouthTexture(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightSouthTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultWestTexture(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultWestTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveWestTexture(String texture) {
        return (BlockModelVerticalMachine) super.withActiveWestTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightWestTexture(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightWestTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withDefaultEastTexture(String texture) {
        return (BlockModelVerticalMachine) super.withDefaultEastTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withActiveEastTexture(String texture) {
        return (BlockModelVerticalMachine) super.withActiveEastTexture(texture);
    }

    @Override
    public BlockModelVerticalMachine withOverbrightEastTexture(String texture) {
        return (BlockModelVerticalMachine) super.withOverbrightEastTexture(texture);
    }
}
