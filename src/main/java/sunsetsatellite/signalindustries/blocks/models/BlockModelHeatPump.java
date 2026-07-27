package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityHeatPump;
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
	public @Nullable IconCoordinate getBlockTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		HashMap<Side, String> usingTextures = textures.defaultTextures;
		TileEntity tileEntity = world.getTileEntity(tilePos);
		if (tileEntity instanceof IActiveForm) {
			if (((IActiveForm) tileEntity).isBurning() && !((IActiveForm) tileEntity).isDisabled()) {
				TileEntityHeatPump chamber = (TileEntityHeatPump) tileEntity;
				if(chamber.currentRecipe != null){
					if(chamber.currentRecipe.getData().auxData.get("mode").equals("freezing")){
						usingTextures = freezingTextures.activeTextures;
					} else {
						usingTextures = textures.activeTextures;
					}
				}
			}
		}

		int data = world.getBlockData(tilePos);
		int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.id];
		if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

		Side id = Side.fromId(index);

		return TextureRegistry.getTexture(usingTextures.get(id));
	}
}
