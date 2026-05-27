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
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.util.MachineTextures;
import sunsetsatellite.signalindustries.util.VerticalMachineTextures;

import java.util.HashMap;

public class BlockModelVerticalMachine extends BlockModelMachine{
	public VerticalMachineTextures verticalTextures;

	public BlockModelVerticalMachine(Block<? extends BlockLogic> block) {
		super(block);
	}

	public BlockModelVerticalMachine(Block<? extends BlockLogic> block, MachineTextures textures) {
		super(block, textures);
	}

	public BlockModelVerticalMachine(Block<? extends BlockLogic> block, MachineTextures textures, VerticalMachineTextures verticalTextures) {
		super(block, textures);
		this.verticalTextures = verticalTextures;
	}

	public BlockModelMachine withVerticalTextures(VerticalMachineTextures machineTextures) {
		this.verticalTextures = machineTextures;
		return this;
	}

	@Override
	public IconCoordinate getFullbrightLayerTexture(@NonNull WorldSource world, @NonNull TilePosc tilePos, @NonNull Side side) {
		TileEntity tileEntity = world.getTileEntity(tilePos);
		if(tileEntity instanceof IActiveForm machine){
			if(machine.isBurning() && !machine.isDisabled()){
				int data = world.getBlockData(tilePos);
				boolean isVertical = data == 0 || data == 1;
				int index;
				if (isVertical) {
					index = VerticalMachineTextures.orientationLookUpVertical[6 * data + side.id];
				} else {
					index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.id];
				}
				if (index >= Sides.orientationLookUpHorizontal.length) return null;
				Side id = Side.fromId(index);
				return isVertical ?
					(verticalTextures.overbrightVerticalTextures.get(id) == null
					 	? null
					 	: TextureRegistry.getTexture(verticalTextures.overbrightVerticalTextures.get(id)))
					: (textures.overbrightTextures.get(id) == null
					   ? null
					   : TextureRegistry.getTexture(textures.overbrightTextures.get(id)));
			}
		}
		return null;
	}

	@Override
	public @Nullable IconCoordinate getBlockTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		HashMap<Side, String> usingTextures = textures.defaultTextures;
		TileEntity tileEntity = world.getTileEntity(tilePos);
		int data = world.getBlockData(tilePos);
		boolean isVertical = data == 0 || data == 1;
		if (isVertical) {
			usingTextures = verticalTextures.defaultVerticalTextures;
		}
		if(tileEntity instanceof IActiveForm machine){
			if(machine.isBurning() && !machine.isDisabled()){
				usingTextures = textures.activeTextures;
				if(isVertical) {
					usingTextures = verticalTextures.activeVerticalTextures;
				}
			}
		}

		int index;
		if (isVertical) {
			index = VerticalMachineTextures.orientationLookUpVertical[6 * data + side.id];
		} else {
			index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.id];
		}
		if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

		Side id = Side.fromId(index);

		return TextureRegistry.getTexture(usingTextures.get(id));
	}

	@Override
	public @Nullable IconCoordinate getBlockTextureFromSideAndMetadata(@NotNull Side side, int data) {
		boolean isVertical = data == 0 || data == 1;
		int index;
		if (isVertical) {
			index = VerticalMachineTextures.orientationLookUpVertical[6 * data + side.id];
		} else {
			index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.id];
		}

		if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

		Side id = Side.fromId(index);

		return isVertical ? TextureRegistry.getTexture(verticalTextures.defaultVerticalTextures.get(id)) : TextureRegistry.getTexture(textures.defaultTextures.get(id));
	}
}
