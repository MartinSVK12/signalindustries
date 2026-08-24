package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBdc;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEnergyInjector;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicInjector extends BlockLogicMachine {
	public BlockLogicInjector(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, String guiId) {
		super(block, material, tier, tileEntitySupplier, guiId);
	}

	@Override
	public @Nullable AABBdc getCollisionAABB(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return null;
	}

	@Override
	public void onEntityCollision(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Entity entity) {
		super.onEntityCollision(world, tilePos, entity);
		if(tier == Tier.REINFORCED){
			TileEntity tileEntity = world.getTileEntity(tilePos);
			if(tileEntity instanceof TileEntityEnergyInjector injector){
				injector.onEntityCollision(entity);
			}
		}
	}
}
