package sunsetsatellite.signalindustries.dim.custom;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.config.season.SeasonConfig;
import net.minecraft.core.world.config.season.SeasonConfigBuilder;
import net.minecraft.core.world.season.Season;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.overworld.WorldTypeOverworld;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.Weathers;
import net.minecraft.core.world.wind.WindProviderGeneric;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec4f;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorBase;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorEmpty;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorBase;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorEmpty;
import sunsetsatellite.signalindustries.dim.custom.property.*;

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

    public Properties properties = new Properties();

    public class Properties {
        public ChunkGeneratorBase chunkGenerator;
        public ChunkDecoratorBase chunkDecorator;
        public DimensionPropertyBiome biomeProvider;
        public WorldTypeFXCustom worldTypeFX;
        public WorldType.Properties worldTypeProperties;

        public Properties() {

        }

        public void test(){
            worldTypeProperties = WorldType.Properties.of("signalindustries.custom")
                    .defaultWeather(Weathers.OVERWORLD_CLEAR)
                    .windManager(new WindProviderGeneric())
                    .seasonConfig(SeasonConfig.builder().withSingleSeason(Seasons.NULL).build())
                    .brightnessRamp(WorldTypeOverworld.createLightRamp());
            biomeProvider = new DimensionPropertyBiome(Biomes.OVERWORLD_PLAINS);
            worldTypeFX = new WorldTypeFXCustom(CustomDimensionData.this);
            chunkGenerator = new ChunkGeneratorEmpty(CustomDimensionData.this);
            chunkDecorator = new ChunkDecoratorEmpty(CustomDimensionData.this);
        }

        public void readFromNbt(CompoundTag tag) {
            if(tag.containsKey("Biome")) {
                biomeProvider = new DimensionPropertyBiome(tag.getCompound("Biome"));
            }
            if(tag.containsKey("WorldType")){
                worldTypeProperties = readWorldType(tag);
            }
            worldTypeFX = new WorldTypeFXCustom(CustomDimensionData.this);
            if(tag.containsKey("FX")){
                readWorldTypeFX(tag);
            }

            chunkGenerator = new ChunkGeneratorEmpty(CustomDimensionData.this);
            chunkDecorator = new ChunkDecoratorEmpty(CustomDimensionData.this);
        }

        private void readWorldTypeFX(CompoundTag tag) {
            CompoundTag props = tag.getCompound("FX");
            Boolean hasClouds = getProperty("HasClouds", props, DimensionPropertyBoolean.class);
            Boolean hasSky = getProperty("HasSky", props, DimensionPropertyBoolean.class);
            Boolean hasGround = getProperty("HasGround", props, DimensionPropertyBoolean.class);
            Boolean hasAurora = getProperty("HasAurora", props, DimensionPropertyBoolean.class);
            Integer cloudHeight = getProperty("CloudHeight", props, DimensionPropertyInt.class);
            Vec4f fogColor = getProperty("FogColor", props, DimensionPropertyColor.class);
            Vec4f sunriseColor = getProperty("SunriseColor", props, DimensionPropertyColor.class);
            if(hasClouds != null) worldTypeFX.hasClouds = hasClouds;
            if(hasGround != null)  worldTypeFX.hasSky = hasSky;
            if(hasAurora != null) worldTypeFX.hasAurora = hasAurora;
            if(cloudHeight != null) worldTypeFX.cloudHeight = cloudHeight;
            if(fogColor != null) worldTypeFX.fogColor = Vec3.getPermanentVec3(fogColor.x, fogColor.y, fogColor.z);
            if(sunriseColor != null) worldTypeFX.sunriseColor = new float[]{(float) sunriseColor.x, (float) sunriseColor.y, (float) sunriseColor.z, (float) sunriseColor.w};
        }

        public <T> T getProperty(String key, CompoundTag tag, Class<?> clazz){
            if(tag.containsKey(key)){
                try {
                    return (T) clazz.getDeclaredConstructor(CompoundTag.class).newInstance(tag.getCompound(key));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }

        private WorldType.Properties readWorldType(CompoundTag tag) {
            CompoundTag props = tag.getCompound("WorldType");
            Weather weather = getProperty("Weather", props, DimensionPropertyWeather.class);
            Block<?> oceanBlock = getProperty("OceanBlock", props, DimensionPropertyBlock.class);
            Block<?> fillerBlock = getProperty("FillerBlock", props, DimensionPropertyBlock.class);
            Boolean mayRespawn = getProperty("MayRespawn", props, DimensionPropertyBoolean.class);
            Integer daylightTicks = getProperty("DaylightTicks", props, DimensionPropertyInt.class);
            List<Pair<Season, Integer>> seasons = null;
            if(props.containsKey("Seasons")){
                seasons = new ArrayList<>();
                ListTag seasonsTag = props.getList("Seasons");
                for (Tag<?> seasonTag : seasonsTag) {
                    seasons.add(new DimensionPropertySeason((CompoundTag) seasonTag).get());
                }
            }
            WorldType.Properties worldTypeProps = WorldType.Properties.of("signalindustries.custom");
            if(weather != null) worldTypeProps.defaultWeather(weather);
            if(oceanBlock != null) worldTypeProps.oceanBlock(oceanBlock);
            if(fillerBlock != null) worldTypeProps.oceanBlock(fillerBlock);
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
        properties.readFromNbt(tag.getCompound("Properties"));
    }

    public void writeToNbt(CompoundTag tag) {
        tag.putString("Name", name);
        tag.putInt("Id", id);
        CompoundTag propertiesTag = new CompoundTag();
        properties.writeToNbt(propertiesTag);
        tag.putCompound("Properties", propertiesTag);
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
}
