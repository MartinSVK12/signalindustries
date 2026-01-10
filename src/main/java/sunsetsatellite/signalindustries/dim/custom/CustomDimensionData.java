package sunsetsatellite.signalindustries.dim.custom;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.biome.provider.BiomeProviderSingleBiome;
import net.minecraft.core.world.config.season.SeasonConfig;
import net.minecraft.core.world.config.season.SeasonConfigBuilder;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.empty.ChunkGeneratorEmpty;
import net.minecraft.core.world.season.SeasonNull;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.overworld.WorldTypeOverworld;
import net.minecraft.core.world.weather.Weathers;
import net.minecraft.core.world.wind.WindProviderGeneric;

public class CustomDimensionData {

    public String name;
    public int id;
    public CompoundTag dataTag;

    public BiomeProviderSingleBiome biome;
    public ChunkGeneratorCustom chunkGenerator;
    public ChunkDecoratorCustom chunkDecorator;
    public WorldTypeCustom worldType;

    public CustomDimensionData(String name, int id) {
        this.dataTag = new CompoundTag();
        this.name = name;
        this.id = id;
    }

    public CustomDimensionData(CompoundTag tag) {
        this.dataTag = tag;
        readFromNbt(tag);
    }

    public void readFromNbt(CompoundTag tag){
        name = tag.getString("Name");
        id = tag.getInteger("Id");
    }

    public void writeToNbt(CompoundTag tag){
        tag.putString("Name", name);
        tag.putInt("Id", id);
    }

    public WorldType.Properties getWorldTypeProperties() {
        return WorldType.Properties.of("signalindustries.custom")
                .defaultWeather(Weathers.OVERWORLD_CLEAR)
                .windManager(new WindProviderGeneric())
                .seasonConfig(SeasonConfig.builder().withSingleSeason(Seasons.NULL).build())
                .brightnessRamp(WorldTypeOverworld.createLightRamp());
    }

    public BiomeProvider getBiomeProvider(World world) {
        if(biome == null) biome = new BiomeProviderSingleBiome(Biomes.OVERWORLD_PLAINS, 1, 1, 1);
        return biome;
    }

    public ChunkGenerator getChunkGenerator(World world) {
        if(chunkGenerator == null) chunkGenerator = new ChunkGeneratorCustom(world, this);
        return chunkGenerator;
    }

    public ChunkDecorator getChunkDecorator(World world) {
        if(chunkDecorator == null) chunkDecorator = new ChunkDecoratorCustom(world, this);
        return chunkDecorator;
    }

    public WorldType getWorldType() {
        if(worldType == null) worldType = new WorldTypeCustom(this);
        return worldType;
    }
}
