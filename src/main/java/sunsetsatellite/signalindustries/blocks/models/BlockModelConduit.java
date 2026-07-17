package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.data.block.BlockModelData;
import sunsetsatellite.catalyst.CatalystEnergy;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.energy.simple.api.IEnergyContainer;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicCatalystConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicFluidConduit;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;

import java.nio.channels.Pipe;
import java.util.HashMap;
import java.util.Map;

public class BlockModelConduit<T extends BlockLogic> extends BlockModelGeneric<T> {

	public HashMap<Direction, BlockModelData> models = new HashMap<>();
	public HashMap<Direction, BlockModelData> splitModels = new HashMap<>();
	public HashMap<Direction, BlockModelData> sensorOffModels = new HashMap<>();
	public HashMap<Direction, BlockModelData> sensorOnModels = new HashMap<>();
	public HashMap<Direction, BlockModelData> restrictModels = new HashMap<>();

	public ConduitCapability type;
	public Tier tier;
	public PipeType pipeType = null;

	public BlockModelConduit(@NotNull Block<T> block, ConduitCapability type, Tier tier, PipeType pipeType) {
		super(block, loadAllConduitModel(type, tier, pipeType));
		this.type = type;
		this.tier = tier;
		this.pipeType = pipeType;
		loadConduitModels();
	}

	@Override
	public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
		HashMap<Direction, Boolean> stateMap = getStateMap(worldSource, tilePos, block, worldSource.getBlockData(tilePos));
		for (Map.Entry<Direction, Boolean> entry : stateMap.entrySet()) {
			Direction dir = entry.getKey();
			Boolean show = entry.getValue();
			if (show) {
				if(type == ConduitCapability.ITEM){
					ItemConduitState itemConduitStateMap = getItemConduitStateMap(worldSource, tilePos, block, worldSource.getBlockData(tilePos));
					TileEntityItemConduit tile = (TileEntityItemConduit) worldSource.getTileEntity(tilePos);
					if(tile == null) continue;
					if(tile.noConnectDirections.get(dir)){
						continue;
					}
					switch (tile.type){
						case RESTRICT -> {
							if(itemConduitStateMap.restrictDirections.get(dir)){
								restrictModels.get(dir).asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
								continue;
							}
						}
						case SENSOR -> {
							if(itemConduitStateMap.sensorActive){
								sensorOnModels.get(dir).asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
							}else{
								sensorOffModels.get(dir).asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
							}
							continue;
						}
					}
					switch (tile.mode){
						case RANDOM -> {}
						case SPLIT -> splitModels.get(dir).asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
					}
				}
				models.get(dir).asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
			}
		}

		if(type == ConduitCapability.ITEM){
			TileEntityItemConduit tile = (TileEntityItemConduit) worldSource.getTileEntity(tilePos);
			if(tile != null){
				boolean changed = false;
				switch (tile.type){
					case NORMAL -> {}
					case RESTRICT -> {
						changed = true;
						loadConduitModel(type, tier, "restrict", "conduit_base").asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
					}
					case SENSOR -> {
						changed = true;
						if(tile.sensorActive){
							loadConduitModel(type, tier, "sensor/on", "conduit_base").asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);;
						} else {
							loadConduitModel(type, tier, "sensor/off", "conduit_base").asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);;
						}
					}
				}
				switch (tile.mode){
					case RANDOM -> {}
					case SPLIT -> {
						changed = true;
						loadConduitModel(type, tier, "split", "conduit_base").asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);;
					}
				}
				if(changed){
					return true;
				}
			}
		}

		return loadBaseConduitModel(worldSource, tilePos).asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0.0F, 0.0F, 0.0F, false, cullFaces, overrideTexture);
	}

	public ItemConduitState getItemConduitStateMap(WorldSource worldSource, TilePosc tilePos, Block<?> block, int meta) {
		HashMap<Direction, Boolean> restrictDirs = new HashMap<>();
		TileEntityItemConduit tile = (TileEntityItemConduit) worldSource.getTileEntity(tilePos);
		boolean sensorActive = false;
		PipeMode mode = PipeMode.RANDOM;
		if(tile != null && tile.type == PipeType.SENSOR){
			sensorActive = tile.sensorActive;
			mode = tile.mode;
		}
		for (Direction direction : Direction.values()) {
			/*if (tile != null && tile.noConnectDirections.get(direction)) {
				restrictDirs.put(direction.getName().toLowerCase(), String.valueOf(show));
				continue;
			}*/
			if (tile != null) {
				if (tile.type == PipeType.RESTRICT) {
					restrictDirs.put(direction, tile.restrictDirections.get(direction));
				}
			}
		}
		return new ItemConduitState(mode, sensorActive, restrictDirs);
	}

	public record ItemConduitState(PipeMode mode, boolean sensorActive, HashMap<Direction, Boolean> restrictDirections){}

	public HashMap<Direction, Boolean> getStateMap(WorldSource worldSource, TilePosc tilePos, Block<?> block, int meta) {
		HashMap<Direction, Boolean> states = new HashMap<>();
		for (Direction direction : Direction.values()) {
			boolean show = false;
			Vec3i offset = new Vec3i(tilePos).add(direction.getVec());
			Block<?> neighbouringBlock = offset.getBlock(worldSource);
			if (neighbouringBlock != null) {
				if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
					show = true;
				} else {
					switch (type){
						case SIGNALUM, FLUID -> {
							if (!(neighbouringBlock.getLogic() instanceof BlockLogicFluidConduit || neighbouringBlock.getLogic() instanceof BlockLogicConduit)) {
								if (neighbouringBlock.isEntityTile) {
									TileEntity neighbouringTile = offset.getTileEntity(worldSource);
									if (neighbouringTile instanceof IFluidInventory) {
										show = true;
									} else if (neighbouringBlock.hasTag(SignalIndustries.SIGNALUM_CONDUITS_CONNECT) || neighbouringBlock.hasTag(SignalIndustries.FLUID_CONDUITS_CONNECT)) {
										show = true;
									}
								} else if (neighbouringBlock.hasTag(SignalIndustries.SIGNALUM_CONDUITS_CONNECT) || neighbouringBlock.hasTag(SignalIndustries.FLUID_CONDUITS_CONNECT)) {
									show = true;
								}
							}
						}
						case CATALYST_ENERGY -> {
							if (!(neighbouringBlock.getLogic() instanceof BlockLogicCatalystConduit)) {
								if (neighbouringBlock.isEntityTile) {
									TileEntity neighbouringTile = offset.getTileEntity(worldSource);
									if (neighbouringTile instanceof IEnergyContainer) {
										show = true;
									} else if (neighbouringBlock.hasTag(CatalystEnergy.ENERGY_CONDUITS_CONNECT)) {
										show = true;
									}
								} else if (neighbouringBlock.hasTag(CatalystEnergy.ENERGY_CONDUITS_CONNECT)) {
									show = true;
								}
							}
						}
						case ITEM -> {
							if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
								TileEntity neighbouringTile = offset.getTileEntity(worldSource);
								if (neighbouringTile instanceof TileEntityItemConduit) {
									if (((TileEntityItemConduit) neighbouringTile).noConnectDirections.get(direction.getOpposite())) {
										states.put(direction, show);
										continue;
									}
								}
								show = true;
							} else {
								if (neighbouringBlock.isEntityTile) {
									TileEntity neighbouringTile = offset.getTileEntity(worldSource);
									if (neighbouringTile instanceof Container) {
										show = true;
									} else if (neighbouringBlock.hasTag(SignalIndustries.ITEM_CONDUITS_CONNECT)) {
										show = true;
									}
								} else if (neighbouringBlock.hasTag(SignalIndustries.ITEM_CONDUITS_CONNECT)) {
									show = true;
								}
							}
						}
					}
				}
			}
			states.put(direction, show);
		}
		return states;
	}

	public void loadConduitModels(){
		if(models.isEmpty()){
			for (Direction dir : Direction.values()) {
				models.put(dir, loadConduitModel(type, tier, null, String.format("conduit_%s",dir.getName().toLowerCase())));
				if(type == ConduitCapability.ITEM){
					splitModels.put(dir, loadConduitModel(type, tier, "split", String.format("conduit_%s",dir.getName().toLowerCase())));
					if(tier.ordinal() > 0){
						restrictModels.put(dir, loadConduitModel(type, tier, "restrict", String.format("conduit_%s",dir.getName().toLowerCase())));
						sensorOffModels.put(dir, loadConduitModel(type, tier, "sensor/off", String.format("conduit_%s",dir.getName().toLowerCase())));
						sensorOnModels.put(dir, loadConduitModel(type, tier, "sensor/on", String.format("conduit_%s",dir.getName().toLowerCase())));
					}
				}
			}
		}
	}

	public static BlockModelData loadConduitModel(ConduitCapability type, Tier tier, String additional, String base) {
		if(additional == null){
			return BlockModelDispatcher.loadDataModel(String.format("signalindustries:block/conduit/%s/%s/%s",type.name().toLowerCase(),tier.name().toLowerCase(),base));
		}
		return BlockModelDispatcher.loadDataModel(String.format("signalindustries:block/conduit/%s/%s/%s/%s",type.name().toLowerCase(),tier.name().toLowerCase(),additional,base));
	}

	public BlockModelData loadBaseConduitModel(@NotNull WorldSource world, @NotNull TilePosc tilePos) {
		return loadConduitModel(type, tier, null, "conduit_base");
	}

	public static BlockModelData loadAllConduitModel(ConduitCapability type, Tier tier, PipeType pipeType) {
		String pType = null;
		if(type == ConduitCapability.ITEM && pipeType != PipeType.NORMAL){
			pType = pipeType.name().toLowerCase();
			if(pipeType == PipeType.SENSOR){
				pType = "sensor/off";
			}
		}
		return loadConduitModel(type, tier, pType, "conduit_all");
	}
}
