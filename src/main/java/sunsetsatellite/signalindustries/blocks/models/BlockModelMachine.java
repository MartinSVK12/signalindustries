package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.MachineTextures;

import java.util.HashMap;

public class BlockModelMachine extends BlockModelCoverable {
	public MachineTextures textures;

	public BlockModelMachine(Block<? extends BlockLogic> block) {
		super(block);
	}

	public BlockModelMachine(Block<? extends BlockLogic> block, MachineTextures textures) {
		super(block);
		this.textures = textures;
	}

	public BlockModelMachine withTextures(MachineTextures machineTextures) {
		this.textures = machineTextures;
		return this;
	}

	@Override
	public IconCoordinate getFullbrightLayerTexture(@NonNull WorldSource world, @NonNull TilePosc tilePos, @NonNull Side side) {
		TileEntity tileEntity = world.getTileEntity(tilePos);
		if(tileEntity instanceof IHasIOPreview ioPreview){
			if(ioPreview.getPreview() != IOPreview.NONE){
				return super.getFullbrightLayerTexture(world, tilePos, side);
			}
		}
		if(tileEntity instanceof IActiveForm machine){
			if(machine.isBurning() && !machine.isDisabled()){
				int data = world.getBlockData(tilePos);
				int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.id];
				if (index >= Sides.orientationLookUpHorizontal.length) return null;

				Side id = Side.fromId(index);

				return textures.overbrightTextures.get(id) == null ? null : TextureRegistry.getTexture(textures.overbrightTextures.get(id));
			}
		}
		return null;
	}

	@Override
	public @Nullable IconCoordinate getBlockTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		HashMap<Side, String> usingTextures = textures.defaultTextures;
		TileEntity tileEntity = world.getTileEntity(tilePos);
		if(tileEntity instanceof IActiveForm machine){
			if(machine.isBurning() && !machine.isDisabled()){
				usingTextures = textures.activeTextures;
			}
		}

		int data = world.getBlockData(tilePos);
		int index = Sides.orientationLookUpHorizontal[6 * Math.min(data, 5) + side.id];
		if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

		Side id = Side.fromId(index);

		return TextureRegistry.getTexture(usingTextures.get(id));
	}

	@Override
	public @Nullable IconCoordinate getBlockTextureFromSideAndMetadata(@NotNull Side side, int data) {
		int index = Sides.orientationLookUpHorizontal[6 * 2 + side.id];
		if (index >= Sides.orientationLookUpHorizontal.length) return BLOCK_TEXTURE_UNASSIGNED;

		Side id = Side.fromId(index);

		return TextureRegistry.getTexture(textures.defaultTextures.get(id));
	}
}
