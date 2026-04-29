package sunsetsatellite.signalindustries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.*;
import net.minecraft.client.render.entity.EntityRendererSprite;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.*;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.collection.NamespaceID;
import org.useless.DragonFly;
import org.useless.dragonfly.models.block.BlockModelDFJava;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.catalyst.multipart.block.model.BlockModelMultipart;
import sunsetsatellite.catalyst.multipart.block.model.MultipartBlockModelBuilder;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicStorageContainer;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachineBase;
import sunsetsatellite.signalindustries.blocks.models.*;
import sunsetsatellite.signalindustries.blocks.states.*;
import sunsetsatellite.signalindustries.entities.*;
import sunsetsatellite.signalindustries.items.models.*;
import sunsetsatellite.signalindustries.render.*;
import sunsetsatellite.signalindustries.render.entity.FallingMeteorRenderer;
import sunsetsatellite.signalindustries.render.entity.RealityTearRenderer;
import sunsetsatellite.signalindustries.render.entity.ShockwaveRenderer;
import sunsetsatellite.signalindustries.render.entity.SunbeamRenderer;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingPlateFormer;
import sunsetsatellite.signalindustries.util.MachineTextures;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static sunsetsatellite.signalindustries.SIBlocks.*;
import static sunsetsatellite.signalindustries.SIItems.*;
import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;
import static sunsetsatellite.signalindustries.SignalIndustriesClient.LOGGER;

@Environment(EnvType.CLIENT)
public class SIModels implements ModelEntrypoint {

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        LOGGER.info("Initializing block models...");

        ModelHelper.setBlockModel(dilithiumCrystalBlock, () -> new BlockModelTransparent<>(dilithiumCrystalBlock, false).setAllTextures(0, "signalindustries:block/dilithium_crystal_block").onRenderLayer(1));
        ModelHelper.setBlockModel(dimensionalCrystalBlock, () -> new BlockModelTransparent<>(dimensionalCrystalBlock, false).setAllTextures(0, "signalindustries:block/dimensional_crystal_block").onRenderLayer(1));
        ModelHelper.setBlockModel(awakenedSocketCasing, () -> new BlockModelConnectedTextureExtra(awakenedSocketCasing, "signalindustries:block/awakened_socket_casing", "signalindustries:block/awakened_socket_casing_active", "signalindustries:block/awakened_socket_overlay", Catalyst.listOf(awakenedCasing)));
        ModelHelper.setBlockModel(awakenedCasing, () -> new BlockModelConnectedTexture(awakenedCasing, "signalindustries:block/awakened_casing", Catalyst.listOf(awakenedSocketCasing)));
        ModelHelper.setBlockModel(awakenedCasing2, () -> new BlockModelConnectedTextureExtra(awakenedCasing2, "signalindustries:block/awakened_casing_2", "signalindustries:block/awakened_casing_2_active"));
        ModelHelper.setBlockModel(basicCasing2, () -> new BlockModelConnectedTexture(basicCasing2, "signalindustries:block/basic_casing_2"));
        ModelHelper.setBlockModel(reinforcedCasing2, () -> new BlockModelConnectedTextureExtra(reinforcedCasing2, "signalindustries:block/reinforced_casing_2", "signalindustries:block/reinforced_casing_2_active"));
        ModelHelper.setBlockModel(reinforcedGlass, () -> new BlockModelConnectedTexture(reinforcedGlass, "signalindustries:block/reinforced_glass", Catalyst.listOf(awakenedEnergyConnector)));
        ModelHelper.setBlockModel(reinforcedIgnitor, () -> new BlockModelIgnitor(reinforcedIgnitor));
        ModelHelper.setBlockModel(basicEnergyInjector, () -> new BlockModelDFJava<>(basicEnergyInjector, DragonFly.loadBlockModel("signalindustries:basic_energy_injector")));
        ModelHelper.setBlockModel(ashenTreeSapling, () -> new BlockModelCrossedSquares<>(ashenTreeSapling).setAllTextures(0, "signalindustries:block/ashen_tree_sapling"));
        ModelHelper.setBlockModel(basicHeatPump, () -> new BlockModelHeatPump(basicHeatPump, blockTextures.get(basicHeatPump),
                blockTextures.get(basicHeatPump).copy()
                        .withActiveTopTexture("basic_heat_pump_top_freezing_active")
                        .withActiveNorthTexture("basic_heat_pump_freezing_active_side")
        ));
        /*ModelHelper.setBlockModel(reinforcedThermalChamber, () -> new BlockModelThermalChamber(reinforcedThermalChamber, blockTextures.get(reinforcedThermalChamber),
                blockTextures.get(reinforcedThermalChamber).copy()
                        .withActiveNorthTexture("reinforced_thermal_chamber_freezing_active_side")
                        .withActiveTopTexture("reinforced_thermal_chamber_top_freezing_active")
        ));*/

        ModelHelper.setBlockModel(pedestal, () ->
                new BlockModelDFJava<>(pedestal, DragonFly.loadBlockModel("signalindustries:pedestal"))
                        .setStateInterpreter(new RotatableStateInterpreter())
                        .setStateData("signalindustries:pedestal")
        );
        ModelHelper.setBlockModel(basicProgrammer, () ->
                new BlockModelDFJava<>(basicProgrammer, DragonFly.loadBlockModel("signalindustries:eeprom_programmer"))
                        .setStateInterpreter(new EEPROMProgrammerStateInterpreter())
                        .setStateData("signalindustries:eeprom_programmer")
        );
        /*ModelHelper.setBlockModel(reinforcedProgrammer, ()->
                new BlockModelDFJava<>(reinforcedProgrammer, DragonFly.loadBlockModel("signalindustries:flash_reprogrammer"))
                        .setStateInterpreter(new NANDProgrammerStateInterpreter())
                        .setStateData("signalindustries:nand_programmer")
        );*/
        ModelHelper.setBlockModel(basicSignalumDynamo, () ->
                new BlockModelDFJava<>(basicSignalumDynamo, DragonFly.loadBlockModel("signalindustries:signalum_dynamo"))
                        .setStateInterpreter(new RotatableStateInterpreter())
                        .setStateData("signalindustries:dynamo")
        );
        ModelHelper.setBlockModel(pulsarBlock, () ->
                new BlockModelDFJava<>(pulsarBlock, DragonFly.loadBlockModel("signalindustries:pulsar/inactive"))
                        .setStateInterpreter(new PulsarStateInterpreter())
                        .setStateData("signalindustries:pulsar")
        );
        ModelHelper.setBlockModel(externalIo, () -> new BlockModelExternalIO((Block<BlockLogicMachine>) externalIo));
        ModelHelper.setBlockModel(reinforcedExternalIo, () -> new BlockModelExternalIO((Block<BlockLogicMachine>) reinforcedExternalIo));

        ModelHelper.setBlockModel(basicBonsai, () ->
                new BlockModelBonsaiPot(basicBonsai, DragonFly.loadBlockModel("signalindustries:bonsai_pot"))
        );
        ModelHelper.setBlockModel(reinforcedBonsai, () ->
                new BlockModelBonsaiPot(reinforcedBonsai, DragonFly.loadBlockModel("signalindustries:reinforced_bonsai_pot"))
        );

        ModelHelper.setBlockModel(lunarTotem, () ->
                new BlockModelDFJava<>(lunarTotem, DragonFly.loadBlockModel("signalindustries:lunar_totem"))
        );

        ModelHelper.setBlockModel(solarTotem, () ->
                new BlockModelDFJava<>(solarTotem, DragonFly.loadBlockModel("signalindustries:solar_totem"))
        );

        ModelHelper.setBlockModel(eternalTreeLog, () ->
                new BlockModelEternalLog(eternalTreeLog)
        );

        ModelHelper.setBlockModel(reinforcedPump, () ->
                new BlockModelPump(reinforcedPump, blockTextures.get(reinforcedPump))
        );

        ModelHelper.setBlockModel(prototypeFluidConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(prototypeFluidConduit, DragonFly.loadBlockModel("signalindustries:conduit/fluid/prototype/conduit_all"))
                            .setStateInterpreter(new ConduitStateInterpreter())
                            .setStateData("signalindustries:prototype_fluid_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(basicFluidConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(basicFluidConduit, DragonFly.loadBlockModel("signalindustries:conduit/fluid/basic/conduit_all"))
                            .setStateInterpreter(new ConduitStateInterpreter())
                            .setStateData("signalindustries:basic_fluid_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(reinforcedFluidConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(reinforcedFluidConduit, DragonFly.loadBlockModel("signalindustries:conduit/fluid/reinforced/conduit_all"))
                            .setStateInterpreter(new ConduitStateInterpreter())
                            .setStateData("signalindustries:reinforced_fluid_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(prototypeConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(prototypeConduit, DragonFly.loadBlockModel("signalindustries:conduit/energy/prototype/conduit_all"))
                            .setStateInterpreter(new ConduitStateInterpreter())
                            .setStateData("signalindustries:prototype_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(basicConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(basicConduit, DragonFly.loadBlockModel("signalindustries:conduit/energy/basic/conduit_all"))
                            .setStateInterpreter(new ConduitStateInterpreter())
                            .setStateData("signalindustries:basic_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(reinforcedConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(reinforcedConduit, DragonFly.loadBlockModel("signalindustries:conduit/energy/reinforced/conduit_all"))
                            .setStateInterpreter(new ConduitStateInterpreter())
                            .setStateData("signalindustries:reinforced_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(awakenedConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(awakenedConduit, DragonFly.loadBlockModel("signalindustries:conduit/energy/awakened/conduit_all"))
                            .setStateInterpreter(new ConduitStateInterpreter())
                            .setStateData("signalindustries:awakened_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(basicCatalystConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(basicCatalystConduit, DragonFly.loadBlockModel("signalindustries:conduit/catalyst/basic/conduit_all"))
                            .setStateInterpreter(new CatalystConduitStateInterpreter())
                            .setStateData("signalindustries:basic_catalyst_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(reinforcedCatalystConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(reinforcedCatalystConduit, DragonFly.loadBlockModel("signalindustries:conduit/catalyst/reinforced/conduit_all"))
                            .setStateInterpreter(new CatalystConduitStateInterpreter())
                            .setStateData("signalindustries:reinforced_catalyst_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(awakenedCatalystConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(awakenedCatalystConduit, DragonFly.loadBlockModel("signalindustries:conduit/catalyst/awakened/conduit_all"))
                            .setStateInterpreter(new CatalystConduitStateInterpreter())
                            .setStateData("signalindustries:awakened_catalyst_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(prototypeItemConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(prototypeItemConduit, DragonFly.loadBlockModel("signalindustries:conduit/item/prototype/conduit_all"))
                            .setStateInterpreter(new ItemConduitStateInterpreter())
                            .setStateData("signalindustries:prototype_item_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(basicItemConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(basicItemConduit, DragonFly.loadBlockModel("signalindustries:conduit/item/basic/conduit_all"))
                            .setStateInterpreter(new ItemConduitStateInterpreter())
                            .setStateData("signalindustries:basic_item_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(basicRestrictItemConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(basicRestrictItemConduit, DragonFly.loadBlockModel("signalindustries:conduit/item/basic/restrict/conduit_all"))
                            .setStateInterpreter(new ItemConduitStateInterpreter())
                            .setStateData("signalindustries:basic_item_conduit_restrict");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(basicSensorItemConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(basicSensorItemConduit, DragonFly.loadBlockModel("signalindustries:conduit/item/basic/sensor/off/conduit_all"))
                            .setStateInterpreter(new ItemConduitStateInterpreter())
                            .setStateData("signalindustries:basic_item_conduit_sensor");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(multiConduit, () -> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(prototypeFluidConduit);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(multiConduit, DragonFly.loadBlockModel("signalindustries:multi_conduit/frame"))
                            .setStateInterpreter(new MultiConduitStateInterpreter())
                            .setStateData("signalindustries:multi_conduit");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(prototypeFilter, () -> {
            BlockModelStandard<? extends BlockLogic> model = new BlockModelStandard<>(prototypeFilter);
            MachineTextures tex = blockTextures.get(prototypeFilter);
            tex.defaultTextures.forEach((side, text) -> model.setTex(0, text, side));
            tex.overbrightTextures.forEach((side, text) -> model.setTex(1, text, side));
            return model;
        });

        ModelHelper.setBlockModel(basicWrathBeacon, () -> new BlockModelMachine(basicWrathBeacon, blockTextures.get(basicWrathBeacon)));
        ModelHelper.setBlockModel(reinforcedWrathBeacon, () -> new BlockModelMachine(reinforcedWrathBeacon, blockTextures.get(reinforcedWrathBeacon)));

        ModelHelper.setBlockModel(spatialEncapsulator, () -> new BlockModelMachine(spatialEncapsulator, blockTextures.get(spatialEncapsulator)));
        ModelHelper.setBlockModel(creationAltar, () -> new BlockModelMachine(creationAltar, blockTextures.get(creationAltar)));

        ModelHelper.setBlockModel(dilithiumRail, () -> new BlockModelDilithiumRail(dilithiumRail));

        ModelHelper.setBlockModel(uvLamp, () -> new BlockModelMachine(uvLamp, blockTextures.get(uvLamp)));
        ModelHelper.setBlockModel(redstoneClock, () -> new BlockModelMachine(redstoneClock, blockTextures.get(redstoneClock)));

        blockTextures.forEach((block, tex) -> {
            //LOGGER.info("Loading block model for '{}'", block.namespaceId());
            if (dispatcher.hasDispatch(block)) return;
            if (Block.hasLogicClass(block, BlockLogicMachineBase.class)) {
                if (((BlockLogicMachineBase) block.getLogic()).isVertical()) {
                    ModelHelper.setBlockModel(block, () -> new BlockModelVerticalMachine(block, tex, blockVerticalTextures.get(block)));
                } else {
                    ModelHelper.setBlockModel(block, () -> new BlockModelMachine(block, tex));
                }
            } else if (Block.hasLogicClass(block, BlockLogicStorageContainer.class)) {
                ModelHelper.setBlockModel(block, () -> new BlockModelMachine(block, tex));
            } else if (Block.hasLogicClass(block, BlockLogicFluid.class)) {
                ModelHelper.setBlockModel(block, () -> {
                    BlockModelFluid<? extends BlockLogic> model = new BlockModelFluid<>(block);
                    tex.defaultTextures.forEach((side, text) -> model.setTex(0, text, side));
                    tex.overbrightTextures.forEach((side, text) -> model.setTex(1, text, side));
                    return model;
                });
            } else if (Block.hasLogicClass(block, BlockLogicFullyRotatable.class)) {
                ModelHelper.setBlockModel(block, () -> {
                    BlockModelFullyRotatable<? extends BlockLogic> model = new BlockModelFullyRotatable<>(block);
                    tex.defaultTextures.forEach((side, text) -> model.setTex(0, text, side));
                    tex.overbrightTextures.forEach((side, text) -> model.setTex(1, text, side));
                    return model;
                });
            } else if (Block.hasLogicClass(block, BlockLogicTransparent.class)) {
                ModelHelper.setBlockModel(block, () -> {
                    BlockModelTransparent<? extends BlockLogic> model = new BlockModelTransparent<>(block, false);
                    tex.defaultTextures.forEach((side, text) -> model.setTex(0, text, side));
                    tex.overbrightTextures.forEach((side, text) -> model.setTex(1, text, side));
                    return model;
                });
            } else {
                ModelHelper.setBlockModel(block, () -> {
                    BlockModelStandard<? extends BlockLogic> model = new BlockModelStandard<>(block);
                    tex.defaultTextures.forEach((side, text) -> model.setTex(0, text, side));
                    tex.overbrightTextures.forEach((side, text) -> model.setTex(1, text, side));
                    return model;
                });
            }
        });
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        LOGGER.info("Initializing item models...");
        itemTextures.forEach((item, texture) -> {
            //LOGGER.info("Loading item model for '{}'", item.namespaceID.toString());

            ModelHelper.setItemModel(item, () -> {
                ItemModelStandard model = new ItemModelStandard(item, MOD_ID);
                if (item == basicSignalumDrill || item == reinforcedSignalumDrill) {
                    model = new ItemModelTool(item, MOD_ID);
                    model.setFull3D();
                }
                model.icon = TextureRegistry.getTexture(NamespaceID.getTemp(MOD_ID, "item/" + texture));
                return model;
            });
        });

        ModelHelper.setItemModel(signalumSaber, () -> new ItemModelSaber(signalumSaber, MOD_ID));
        ModelHelper.setItemModel(configurationTablet, () -> new ItemModelConfigurationTablet(configurationTablet, MOD_ID));
        ModelHelper.setItemModel(fuelCell, () -> new ItemModelFuelCell(fuelCell, MOD_ID));
        ModelHelper.setItemModel(meteorTracker, () -> new ItemModelMeteorTracker(fuelCell, MOD_ID, Tier.BASIC));
        ModelHelper.setItemModel(reinforcedMeteorTracker, () -> new ItemModelMeteorTracker(fuelCell, MOD_ID, Tier.REINFORCED));
        ModelHelper.setItemModel(nullTrigger, () -> new ItemModelTrigger(nullTrigger, MOD_ID));
        ModelHelper.setItemModel(pulsar, () -> new ItemModelPulsar(pulsar, MOD_ID));
        ItemBlock<?> prototypeTankItem = (ItemBlock<?>) Item.getItem(prototypeFluidTank.id());
        ModelHelper.setItemModel(prototypeTankItem, () -> new ItemModelBlockSIFluidTank(prototypeTankItem));
        ItemBlock<?> basicTankItem = (ItemBlock<?>) Item.getItem(basicFluidTank.id());
        ModelHelper.setItemModel(basicTankItem, () -> new ItemModelBlockSIFluidTank(basicTankItem));
        ItemBlock<?> infTankItem = (ItemBlock<?>) Item.getItem(infiniteFluidTank.id());
        ModelHelper.setItemModel(infTankItem, () -> new ItemModelBlockSIFluidTank(infTankItem));
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {
        LOGGER.info("Initializing entity models...");
        ModelHelper.setEntityModel(ProjectileCrystal.class, () -> new EntityRendererSprite<ProjectileCrystal>(volatileSignalumCrystal));
        ModelHelper.setEntityModel(ProjectileFallingMeteor.class, FallingMeteorRenderer::new);
        ModelHelper.setEntityModel(ProjectileEnergyOrb.class, () -> new EntityRendererSprite<ProjectileCrystal>(TextureRegistry.getTexture("signalindustries:item/energyorb")));
        ModelHelper.setEntityModel(ProjectileSunbeam.class, SunbeamRenderer::new);
        ModelHelper.setEntityModel(MobInfernal.class, () -> new MobRenderer<MobInfernal>(new ModelZombie(), 0.5f));
        ModelHelper.setEntityModel(EntityRealityTear.class, RealityTearRenderer::new);
        ModelHelper.setEntityModel(EntityShockwave.class, ShockwaveRenderer::new);
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        LOGGER.info("Initializing tile entity renderers...");
        ModelHelper.setTileEntityModel(TileEntityInductionSmelter.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityWakingAlloySmelter.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityWakingCrusher.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityWakingPlateFormer.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityWakingInfuser.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityDimensionalAnchor.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityReinforcedExtractor.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntitySignalumReactor.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityStorageContainer.class, RenderStorageContainer::new);
        ModelHelper.setTileEntityModel(TileEntityBuilder.class, RenderBuilderPreview::new);
        ModelHelper.setTileEntityModel(TileEntitySIFluidTank.class, RenderFluidInBlock::new);
        ModelHelper.setTileEntityModel(TileEntityEnergyCell.class, RenderFluidInBlock::new);
        ModelHelper.setTileEntityModel(TileEntityFluidConduit.class, RenderFluidInConduit::new);
        ModelHelper.setTileEntityModel(TileEntityConduit.class, RenderFluidInConduit::new);
        ModelHelper.setTileEntityModel(TileEntityStoneworks.class, RenderStoneworks::new);
        ModelHelper.setTileEntityModel(TileEntityEnergyInjector.class, RenderEnergyInjector::new);
        ModelHelper.setTileEntityModel(TileEntityWarpGate.class, RenderWarpGate::new);
        ModelHelper.setTileEntityModel(TileEntityItemConduit.class, RenderItemsInConduit::new);
        ModelHelper.setTileEntityModel(TileEntityAssembler.class, RenderAssembler::new);
        ModelHelper.setTileEntityModel(TileEntityAutoMiner.class, RenderAutoMiner::new);
        ModelHelper.setTileEntityModel(TileEntityReinforcedWrathBeacon.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityMultiConduit.class, RenderFluidInMultiConduit::new);
        ModelHelper.setTileEntityModel(TileEntityPulsar.class, RenderPulsar::new);
        ModelHelper.setTileEntityModel(TileEntityLaserDrill.class, RenderMultiblock::new);
        ModelHelper.setTileEntityModel(TileEntityGreenhouse.class, RenderGreenhouse::new);
        ModelHelper.setTileEntityModel(TileEntityEncapsulator.class, RenderEncapsulator::new);
        ModelHelper.setTileEntityModel(TileEntityPedestal.class, RenderPedestal::new);
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
        //ModelHelper.setBlockColor(unraveledFabric, BlockColorUnraveledFabric::new);
    }
}
