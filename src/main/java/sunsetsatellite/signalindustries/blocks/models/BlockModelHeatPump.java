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
import sunsetsatellite.signalindustries.tiles.machines.TileEntityHeatPump;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityThermalChamber;
import sunsetsatellite.signalindustries.util.MachineTextures;

import java.util.HashMap;

public class BlockModelHeatPump extends BlockModelMachine {

    public MachineTextures freezingTextures = new MachineTextures();

    public BlockModelHeatPump(Block<? extends BlockLogic> block) {
        super(block);
    }

    public BlockModelHeatPump(Block<? extends BlockLogic> block, MachineTextures meltingTextures, MachineTextures freezingTextures) {
        super(block, meltingTextures);
        this.freezingTextures = freezingTextures;
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        HashMap<Side, String> usingTextures = machineTextures.defaultTextures;
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof IActiveForm) {
            if (((IActiveForm) tileEntity).isBurning() && !((IActiveForm) tileEntity).isDisabled()) {
                TileEntityHeatPump chamber = (TileEntityHeatPump) tileEntity;
                if(chamber.currentRecipe != null){
                    if(chamber.currentRecipe.getData().auxData.get("mode").equals("freezing")){
                        usingTextures = freezingTextures.activeTextures;
                    } else {
                        usingTextures = machineTextures.activeTextures;
                    }
                }
            }
        }

        int data = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.getId()];
        if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

        Side id = Side.getSideById(index);

        return TextureRegistry.getTexture(usingTextures.get(id));
    }

}
