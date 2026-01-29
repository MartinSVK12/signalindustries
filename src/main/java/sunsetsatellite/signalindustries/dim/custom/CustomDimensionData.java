package sunsetsatellite.signalindustries.dim.custom;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.config.season.SeasonConfig;
import net.minecraft.core.world.config.season.SeasonConfigBuilder;
import net.minecraft.core.world.config.season.SeasonConfigCycle;
import net.minecraft.core.world.config.season.SeasonConfigSingle;
import net.minecraft.core.world.season.Season;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.type.overworld.WorldTypeOverworld;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.Weathers;
import net.minecraft.core.world.wind.WindProviderGeneric;
import sunsetsatellite.catalyst.core.util.vector.Vec4f;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorBase;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorEmpty;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorBase;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorClassic;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorEmpty;
import sunsetsatellite.signalindustries.dim.custom.property.*;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorBase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CustomDimensionData {

    public String name;
    public int id;
    public CompoundTag dataTag;

    private BiomeProviderWrapper biomeProvider;
    private ChunkGeneratorWrapper chunkGenerator;
    private ChunkDecoratorWrapper chunkDecorator;
    private WorldTypeWrapper worldType;
    public List<BiomeCustom> biomes = new ArrayList<>();

    public Properties properties = new Properties();

    public class Properties {
        public ChunkGeneratorBase chunkGenerator;
        public ChunkDecoratorBase chunkDecorator;
        public DimPropertyBiome biomeProvider;
        public WorldTypeFXCustom worldTypeFX;
        public WorldType.Properties worldTypeProperties;

        public Properties() {

        }

        public void empty(){
            worldTypeProperties = WorldType.Properties.of("signalindustries.custom")
                    .defaultWeather(Weathers.OVERWORLD_CLEAR)
                    .windManager(new WindProviderGeneric())
                    .seasonConfig(SeasonConfig.builder().withSingleSeason(Seasons.NULL).build())
                    .brightnessRamp(WorldTypeOverworld.createLightRamp());
            biomeProvider = new DimPropertyBiome(Biomes.OVERWORLD_PLAINS);
            worldTypeFX = new WorldTypeFXCustom(CustomDimensionData.this);
            chunkGenerator = new ChunkGeneratorEmpty(CustomDimensionData.this, new CompoundTag());
            chunkDecorator = new ChunkDecoratorEmpty(CustomDimensionData.this, new CompoundTag());
        }

        public void readFromNbt(CompoundTag tag) {
            if(tag.containsKey("Biome")) {
                biomeProvider = new DimPropertyBiome(tag.getCompound("Biome"));
            }
            if(tag.containsKey("WorldType")){
                worldTypeProperties = readWorldType(tag);
            }
            worldTypeFX = new WorldTypeFXCustom(CustomDimensionData.this);
            if(tag.containsKey("FX")){
                readWorldTypeFX(tag);
            }
            chunkGenerator = getProperty("ChunkGenerator", tag, DimensionRegistries.CHUNK_GENERATORS);
            if(chunkGenerator == null){
                chunkGenerator = new ChunkGeneratorEmpty(CustomDimensionData.this, tag.getCompound("ChunkGenerator"));
            }
            chunkDecorator = new ChunkDecoratorEmpty(CustomDimensionData.this, tag.getCompound("ChunkDecorator"));
        }

        public void readWorldTypeFX(CompoundTag tag) {
            CompoundTag props = tag.getCompound("FX");
            Boolean hasClouds = getProperty("HasClouds", props, DimPropertyBoolean.class);
            Boolean hasSky = getProperty("HasSky", props, DimPropertyBoolean.class);
            Boolean hasGround = getProperty("HasGround", props, DimPropertyBoolean.class);
            Boolean hasAurora = getProperty("HasAurora", props, DimPropertyBoolean.class);
            Float cloudHeight = getProperty("CloudHeight", props, DimPropertyFloat.class);
            Vec4f fogColor = getProperty("FogColor", props, DimPropertyColor.class);
            Vec4f sunriseColor = getProperty("SunriseColor", props, DimPropertyColor.class);
            if(hasClouds != null) worldTypeFX.hasClouds = hasClouds;
            if(hasGround != null)  worldTypeFX.hasSky = hasSky;
            if(hasAurora != null) worldTypeFX.hasAurora = hasAurora;
            if(cloudHeight != null) worldTypeFX.cloudHeight = cloudHeight;
            if(fogColor != null) worldTypeFX.fogColor = Vec3.getPermanentVec3(fogColor.x, fogColor.y, fogColor.z);
            if(sunriseColor != null) worldTypeFX.sunriseColor = new float[]{(float) sunriseColor.x, (float) sunriseColor.y, (float) sunriseColor.z, (float) sunriseColor.w};
        }
        
        public void writeWorldTypeFX(CompoundTag tag){
            saveProperty("HasClouds", new DimPropertyBoolean(worldTypeFX.hasClouds), tag);
            saveProperty("HasSky", new DimPropertyBoolean(worldTypeFX.hasSky), tag);
            saveProperty("HasGround", new DimPropertyBoolean(worldTypeFX.hasGround), tag);
            saveProperty("HasAurora", new DimPropertyBoolean(worldTypeFX.hasAurora), tag);
            saveProperty("CloudHeight", new DimPropertyFloat(worldTypeFX.cloudHeight), tag);
            if (worldTypeFX.fogColor != null) {
                saveProperty("FogColor", new DimPropertyColor(new Vec4f((float) worldTypeFX.fogColor.x, (float) worldTypeFX.fogColor.y, (float) worldTypeFX.fogColor.z, 1.0f)), tag);
            }
            if (worldTypeFX.sunriseColor != null) {
                saveProperty("SunriseColor", new DimPropertyColor(new Vec4f(worldTypeFX.sunriseColor[0], worldTypeFX.sunriseColor[1], worldTypeFX.sunriseColor[2], worldTypeFX.sunriseColor[3])), tag);
            }
        }

        public void writeWorldType(CompoundTag tag){
            WorldTypeWrapper wt = getWorldType();

            saveProperty("Weather", new DimPropertyWeather(wt.getDefaultWeather()), tag);
            saveProperty("OceanBlock", new DimPropertyBlock(Blocks.getBlock(wt.getOceanBlockId())), tag);
            saveProperty("FillerBlock", new DimPropertyBlock(Blocks.getBlock(wt.getFillerBlockId())), tag);
            saveProperty("MayRespawn", new DimPropertyBoolean(wt.mayRespawn()), tag);
            saveProperty("DaylightTicks", new DimPropertyInt(wt.getDayNightCycleTicks()), tag);

            ListTag seasonsTag = new ListTag();
            SeasonConfig seasonConfig = wt.getSeasonConfig();
            if(seasonConfig instanceof SeasonConfigSingle){
                Season season = ((SeasonConfigSingle) seasonConfig).getSingleSeason();
                CompoundTag seasonTag = new CompoundTag();
                DimPropertySeason dps = new DimPropertySeason(season, -1);
                dps.writeToNbt(seasonTag);
                seasonsTag.addTag(seasonTag);
            } else if (seasonConfig instanceof SeasonConfigCycle) {
                for (Season season : ((SeasonConfigCycle) seasonConfig).getSeasons()) {
                    int length = ((SeasonConfigCycle) seasonConfig).getSeasonLength(season);
                    CompoundTag seasonTag = new CompoundTag();
                    DimPropertySeason dps = new DimPropertySeason(season, length);
                    dps.writeToNbt(seasonTag);
                    seasonsTag.addTag(seasonTag);
                }
            }
            tag.putList("Seasons", seasonsTag);
        }

        private WorldType.Properties readWorldType(CompoundTag tag) {
            CompoundTag props = tag.getCompound("WorldType");
            Weather weather = getProperty("Weather", props, DimPropertyWeather.class);
            Block<?> oceanBlock = getProperty("OceanBlock", props, DimPropertyBlock.class);
            Block<?> fillerBlock = getProperty("FillerBlock", props, DimPropertyBlock.class);
            Boolean mayRespawn = getProperty("MayRespawn", props, DimPropertyBoolean.class);
            Integer daylightTicks = getProperty("DaylightTicks", props, DimPropertyInt.class);
            List<Pair<Season, Integer>> seasons = null;
            if(props.containsKey("Seasons")){
                seasons = new ArrayList<>();
                ListTag seasonsTag = props.getList("Seasons");
                for (Tag<?> seasonTag : seasonsTag) {
                    seasons.add(new DimPropertySeason((CompoundTag) seasonTag).get());
                }
            }
            WorldType.Properties worldTypeProps = WorldType.Properties.of("signalindustries.custom");
            if(weather != null) worldTypeProps.defaultWeather(weather);
            if(oceanBlock != null) worldTypeProps.oceanBlock(oceanBlock);
            if(fillerBlock != null) worldTypeProps.fillerBlock(fillerBlock);
            if(mayRespawn != null && mayRespawn) worldTypeProps.allowRespawn();
            if(daylightTicks != null) worldTypeProps.dayNightCycleTicks(daylightTicks);
            worldTypeProps.brightnessRamp(WorldTypeOverworld.createLightRamp());
            if(seasons != null) {
                if(seasons.size() == 1){
                    worldTypeProps.seasonConfig(SeasonConfig.builder().withSingleSeason(seasons.get(0).getLeft()).build());
                } else {
                    SeasonConfigBuilder builder = SeasonConfig.builder();
                    for (Pair<Season, Integer> season : seasons) {
                        builder.withSeasonInCycle(season.getLeft(), season.getRight());
                    }
                    worldTypeProps.seasonConfig(builder.build());
                }
            }
            return worldTypeProps;
        }

        public void writeToNbt(CompoundTag tag) {
            CompoundTag biomeTag = new CompoundTag();
            biomeProvider.writeToNbt(biomeTag);
            tag.putCompound("Biome", biomeTag);

            CompoundTag worldTypePropsTag = new CompoundTag();
            writeWorldType(worldTypePropsTag);
            tag.putCompound("WorldType", worldTypePropsTag);
            
            CompoundTag fxTag = new CompoundTag();
            writeWorldTypeFX(fxTag);
            tag.putCompound("FX", fxTag);

            CompoundTag chunkGeneratorTag = new CompoundTag();
            chunkGeneratorTag.putString("Type", DimensionRegistries.CHUNK_GENERATORS.getKey(chunkGenerator.getClass()));
            CompoundTag chunkGenData = new CompoundTag();
            chunkGenerator.writeToNbt(chunkGenData);
            chunkGeneratorTag.put("Data", chunkGenData);
            tag.putCompound("ChunkGenerator", chunkGeneratorTag);

            /*CompoundTag chunkDecoratorTag = new CompoundTag();
            chunkDecorator.writeToNbt(chunkDecoratorTag);
            tag.putCompound("ChunkDecorator", chunkDecoratorTag);*/
        }
    }

    public CustomDimensionData(String name, int id) {
        this.dataTag = new CompoundTag();
        this.name = name;
        this.id = id;
    }

    public CustomDimensionData(CompoundTag tag) {
        this.dataTag = tag;
        readFromNbt(tag);
    }

    public void readFromNbt(CompoundTag tag) {
        name = tag.getString("Name");
        id = tag.getInteger("Id");
        if (tag.containsKey("Biomes")) {
            tag.getList("Biomes").forEach(b -> {
                CompoundTag biomeTag = (CompoundTag) b;
                String id = biomeTag.getString("Id");
                BiomeCustom biome = new BiomeCustom(id, this, biomeTag);
                biomes.add(biome);
                Biomes.register(SignalIndustries.key(id), biome);
            });
        }
        properties.readFromNbt(tag.getCompound("Properties"));
    }

    public void writeToNbt(CompoundTag tag) {
        tag.putString("Name", name);
        tag.putInt("Id", id);
        CompoundTag propertiesTag = new CompoundTag();
        properties.writeToNbt(propertiesTag);
        tag.putCompound("Properties", propertiesTag);
        ListTag biomesTag = new ListTag();
        for (BiomeCustom biome : biomes) {
            CompoundTag biomeTag = new CompoundTag();
            biomeTag.putString("Id", biome.translationKey);
            biome.writeToNbt(biomeTag);
            biomesTag.addTag(biomeTag);
        }
        tag.putList("Biomes", biomesTag);
    }

    public WorldType.Properties getWorldTypeProperties() {
        return properties.worldTypeProperties;
    }

    public BiomeProviderWrapper getBiomeProvider(World world) {
        if (biomeProvider == null) biomeProvider = new BiomeProviderWrapper(world, this);
        return biomeProvider;
    }

    public ChunkGeneratorWrapper getChunkGenerator(World world) {
        if (chunkGenerator == null) chunkGenerator = new ChunkGeneratorWrapper(world, this);
        return chunkGenerator;
    }

    public ChunkDecoratorWrapper getChunkDecorator(World world) {
        if (chunkDecorator == null) chunkDecorator = new ChunkDecoratorWrapper(world, this);
        return chunkDecorator;
    }

    public WorldTypeWrapper getWorldType() {
        if (worldType == null) worldType = new WorldTypeWrapper(this);
        return worldType;
    }

    public void reset(){
        biomeProvider = null;
        chunkGenerator = null;
        chunkDecorator = null;
        worldType = null;
    }

    public <T> T getProperty(String key, CompoundTag tag, Class<?> clazz){
        if(tag.containsKey(key)){
            try {
                DimPropertyBase o = (DimPropertyBase) clazz.getDeclaredConstructor(CompoundTag.class).newInstance(tag.getCompound(key));
                return (T) o.get();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public <T> T getProperty(String key, CompoundTag tag, Registry<Class<? extends T>> registry){
        if(tag.containsKey(key)){
            CompoundTag innerTag = tag.getCompound(key);
            String type = innerTag.getString("Type");
            Class<? extends T> clazz = registry.getItem(type);
            if(clazz == null) return null;
            try {
                return clazz.getDeclaredConstructor(CustomDimensionData.class, CompoundTag.class).newInstance(this, innerTag.getCompound("Data"));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void saveProperty(String key, DimPropertyBase prop, CompoundTag tag){
        CompoundTag innerTag = new CompoundTag();
        prop.writeToNbt(innerTag);
        tag.put(key, innerTag);
    }
}
