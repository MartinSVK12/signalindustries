package sunsetsatellite.signalindustries;

import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.*;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.entity.EntityRendererSprite;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicItemConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicStorageContainer;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicConduitBase;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachineBase;
import sunsetsatellite.signalindustries.blocks.models.*;
import sunsetsatellite.signalindustries.blocks.models.BlockModelConduit;
import sunsetsatellite.signalindustries.entities.ProjectileCrystal;
import sunsetsatellite.signalindustries.items.models.*;
import sunsetsatellite.signalindustries.render.*;
import sunsetsatellite.signalindustries.tiles.TileEntityStorageContainer;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityAssembler;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEnergyCell;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPump;
import sunsetsatellite.signalindustries.tiles.machines.TileEntitySIFluidTank;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityGreenhouse;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityInductionSmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityLaserDrill;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingPlateFormer;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;

import static sunsetsatellite.signalindustries.SIBlocks.*;
import static sunsetsatellite.signalindustries.SIItems.*;
import static sunsetsatellite.signalindustries.SIItems.fuelCell;
import static sunsetsatellite.signalindustries.SIItems.reinforcedMeteorTracker;
import static sunsetsatellite.signalindustries.SignalIndustriesClient.LOGGER;

public class SIModels {
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		LOGGER.info("Initializing block models...");
		dispatcher.addDispatch(dilithiumCrystalBlock,
			new BlockModelTransparent<>(dilithiumCrystalBlock, false)
				.setAllTextures(blockTextures.get(dilithiumCrystalBlock).defaultTextures.get(Side.TOP))
				.onRenderLayer(1)
		);

		dispatcher.addDispatch(awakenedSocketCasing, new BlockModelConnectedTextureExtra(awakenedSocketCasing, "signalindustries:block/awakened_socket_casing", "signalindustries:block/awakened_socket_casing_active", "signalindustries:block/awakened_socket_overlay", Catalyst.listOf(awakenedCasing)));
		dispatcher.addDispatch(awakenedCasing, new BlockModelConnectedTexture(awakenedCasing, "signalindustries:block/awakened_casing", Catalyst.listOf(awakenedSocketCasing)));
		dispatcher.addDispatch(awakenedCasing2, new BlockModelConnectedTextureExtra(awakenedCasing2, "signalindustries:block/awakened_casing_2", "signalindustries:block/awakened_casing_2_active"));
		dispatcher.addDispatch(basicCasing2, new BlockModelConnectedTexture(basicCasing2, "signalindustries:block/basic_casing_2"));
		dispatcher.addDispatch(reinforcedCasing2, new BlockModelConnectedTextureExtra(reinforcedCasing2, "signalindustries:block/reinforced_casing_2", "signalindustries:block/reinforced_casing_2_active"));
		dispatcher.addDispatch(reinforcedGlass, new BlockModelConnectedTexture(reinforcedGlass, "signalindustries:block/reinforced_glass", Catalyst.listOf(awakenedEnergyConnector)));

		dispatcher.addDispatch(ashenTreeSapling, new BlockModelCrossedSquares<>(ashenTreeSapling).setAllTextures(blockTextures.get(ashenTreeSapling).defaultTextures.get(Side.BOTTOM)));

		dispatcher.addDispatch(basicEnergyInjector, new BlockModelGeneric<>(basicEnergyInjector, BlockModelDispatcher.loadDataModel("signalindustries:block/basic_energy_injector")));

		dispatcher.addDispatch(lunarTotem, new BlockModelGeneric<>(lunarTotem, BlockModelDispatcher.loadDataModel("signalindustries:block/lunar_totem")));
		dispatcher.addDispatch(solarTotem, new BlockModelGeneric<>(solarTotem, BlockModelDispatcher.loadDataModel("signalindustries:block/solar_totem")));

		dispatcher.addDispatch(reinforcedIgnitor, new BlockModelIgnitor(reinforcedIgnitor));

		blockTextures.forEach((block, tex) -> {
			if (dispatcher.hasDispatch(block)) return;
			if (Block.hasLogicClass(block, BlockLogicMachineBase.class)) {
				if (((BlockLogicMachineBase) block.getLogic()).isVertical()) {
					dispatcher.addDispatch(block, new BlockModelVerticalMachine(block, tex, blockVerticalTextures.get(block)));
				} else {
					dispatcher.addDispatch(block, new BlockModelMachine(block, tex));
				}
			} else if (Block.hasLogicClass(block, BlockLogicFluid.class)) {
				dispatcher.addDispatch(block, new BlockModelFluid<>(((Block<BlockLogicFluid>) block), tex.defaultTextures.get(Side.TOP), tex.defaultTextures.get(Side.BOTTOM)));
			} else if(Block.hasLogicClass(block, BlockLogicStorageContainer.class)) {
				dispatcher.addDispatch(block, new BlockModelMachine(block, tex));
			} else if(Block.hasLogicClass(block, BlockLogicFullyRotatable.class)) {
				BlockModelFullyRotatable<? extends BlockLogic> model = new BlockModelFullyRotatable<>(block);
				tex.defaultTextures.forEach((side, text) -> model.setTex(text, side));
				dispatcher.addDispatch(block,model);
			} else if(Block.hasLogicClass(block, BlockLogicConduitBase.class)) {
				BlockLogicConduitBase conduit = (BlockLogicConduitBase) block.getLogic();
				PipeType pipeType = null;
				if(conduit instanceof BlockLogicItemConduit itemConduit){
					pipeType = itemConduit.type;
				}
				dispatcher.addDispatch(block, new BlockModelConduit<>(block, conduit.conduitCapability, conduit.tier, pipeType));
			} else {
				BlockModelFullbright model = new BlockModelFullbright(block);
				tex.defaultTextures.forEach((side, text) -> model.setTex(text, side));
				tex.overbrightTextures.forEach((side, text) -> model.fullbrightLayer.set(text, side));
				dispatcher.addDispatch(block,model);
			}
		});
	}

	public void initItemModels(ItemModelDispatcher dispatcher) {
		LOGGER.info("Initializing item models...");
		itemTextures.forEach((item, texture) -> {
			ItemModelStandard model = new ItemModelStandard(item, false);
			model.setIcon(SignalIndustries.id("item/"+texture));
			dispatcher.addDispatch(item, model);
		});

		dispatcher.addDispatch(signalumSaber, new ItemModelSaber(signalumSaber));
		dispatcher.addDispatch(fuelCell, new ItemModelFuelCell(fuelCell));
		dispatcher.addDispatch(nullTrigger, new ItemModelTrigger(nullTrigger));
		dispatcher.addDispatch(pulsar, new ItemModelPulsar(pulsar));
		dispatcher.addDispatch(meteorTracker, new ItemModelMeteorTracker(fuelCell, Tier.BASIC));
		dispatcher.addDispatch(reinforcedMeteorTracker, new ItemModelMeteorTracker(fuelCell, Tier.REINFORCED));
	}


	public void initEntityModels(EntityRendererDispatcher dispatcher) {
		LOGGER.info("Initializing entity models...");
		dispatcher.assignRenderer(ProjectileCrystal.class, new EntityRendererSprite<>(volatileSignalumCrystal));
	}

	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
		LOGGER.info("Initializing tile entity renderers...");
		dispatcher.assignRenderer(TileEntityDimensionalAnchor.class, new RenderMultiblock());
		dispatcher.assignRenderer(TileEntityWakingAlloySmelter.class, new RenderMultiblock());
		dispatcher.assignRenderer(TileEntityWakingPlateFormer.class, new RenderMultiblock());
		dispatcher.assignRenderer(TileEntityWakingCrusher.class, new RenderMultiblock());
		dispatcher.assignRenderer(TileEntityWakingInfuser.class, new RenderMultiblock());
		dispatcher.assignRenderer(TileEntityLaserDrill.class, new RenderMultiblock());
		dispatcher.assignRenderer(TileEntityInductionSmelter.class, new RenderMultiblock());
		dispatcher.assignRenderer(TileEntityGreenhouse.class, new RenderGreenhouse());
		dispatcher.assignRenderer(TileEntityAssembler.class, new RenderAssembler());
		dispatcher.assignRenderer(TileEntitySIFluidTank.class, new RenderFluidInBlock());
		dispatcher.assignRenderer(TileEntityEnergyCell.class, new RenderFluidInBlock());
		dispatcher.assignRenderer(TileEntityStorageContainer.class, new RenderStorageContainer());
		dispatcher.assignRenderer(TileEntityPump.class, new RenderPump());
	}

	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
