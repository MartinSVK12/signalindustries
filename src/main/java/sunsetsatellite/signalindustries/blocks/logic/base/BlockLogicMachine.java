package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIModels;
import sunsetsatellite.signalindustries.SIMultiblocks;
import sunsetsatellite.signalindustries.util.SIMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class BlockLogicMachine extends BlockLogicMachineBase {
	public String guiId;

	public BlockLogicMachine(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, String guiId) {
		super(block, material, tier, tileEntitySupplier);
		this.guiId = guiId;
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if(world.isClientSide) return true;
		if(super.onInteracted(world, tilePos, player, side, xHit, yHit)) return true;
		TileEntity tile = world.getTileEntity(tilePos);
		if(tile != null && guiId != null) {
			if(tile instanceof IMultiblock multiblock){
				if(multiblock.getMultiblock() != null && multiblock.getMultiblock().isValid()){
					if(multiblock.getMultiblock().data == SIMultiblocks.dimAnchorMultiblock) {
						player.triggerAchievement(SIAchievements.ANCHOR);
					}
					if(multiblock.getMultiblock().data == SIMultiblocks.wakingCrusher) {
						player.triggerAchievement(SIAchievements.WAKING1);
					}
					if(multiblock.getMultiblock().data == SIMultiblocks.wakingPlateFormer) {
						player.triggerAchievement(SIAchievements.WAKING2);
					}
					if(multiblock.getMultiblock().data == SIMultiblocks.wakingInfuser) {
						player.triggerAchievement(SIAchievements.WAKING3);
					}
					if(multiblock.getMultiblock().data == SIMultiblocks.wakingAlloySmelter) {
						player.triggerAchievement(SIAchievements.WAKING4);
					}
					if(multiblock.getMultiblock().data == SIMultiblocks.warpGate) {
						player.triggerAchievement(SIAchievements.GATE);
					}
					if(multiblock.getMultiblock().data == SIMultiblocks.megaTrommel) {
						player.triggerAchievement(SIAchievements.MEGA_TROMMEL);
					}
					if(multiblock.getMultiblock().data instanceof SIMultiblock siMultiblock){
						if(siMultiblock.tier.ordinal() >= Tier.REINFORCED.ordinal()){
							player.triggerAchievement(SIAchievements.HORIZONS);
						}
					}
					Catalyst.displayGui(player, tile, key("gui/" + guiId));
					return true;
				} else {
					player.sendTranslatedChatMessage("event.signalindustries.invalidMultiblock");
					return true;
				}
			}
			Catalyst.displayGui(player, tile, key("gui/" + guiId));
		} else {
			return false;
		}
		return true;
	}
}
