package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.world.generate.CavesLargeFeature;
import net.minecraft.core.world.generate.LargeFeature;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorBase;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorCustom;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorEmpty;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorWorld;
import sunsetsatellite.signalindustries.dim.custom.feature.WorldFeatureBase;
import sunsetsatellite.signalindustries.dim.custom.feature.WorldFeatureOre;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorBase;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorClassic;
import sunsetsatellite.signalindustries.dim.custom.generator.ChunkGeneratorEmpty;
import sunsetsatellite.signalindustries.dim.custom.property.*;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorBase;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorEmpty;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorOverworld;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorRetro;

public class DimensionRegistries {
    public static final Registry<Class<? extends DimPropertyBase>> DIM_PROPERTIES = new Registry<>();
    public static final Registry<Class<? extends ChunkGeneratorBase>> CHUNK_GENERATORS = new Registry<>();
    public static final Registry<Class<? extends ChunkDecoratorBase>> CHUNK_DECORATORS = new Registry<>();
    public static final Registry<Class<? extends SurfaceGeneratorBase>> SURFACE_GENERATORS = new Registry<>();
    public static final Registry<Class<? extends LargeFeature>> LARGE_FEATURES = new Registry<>();
    public static final Registry<Class<? extends WorldFeatureBase>> FEATURES = new Registry<>();

    public static boolean init = false;

    public static void init() {
        if(!init){
            initDimProperties();
            initChunkGenerators();
            initChunkDecorators();
            initSurfaceGenerators();
            initLargeFeatures();
            initFeatures();
            Registries.getInstance().register("signalindustries:dim_properties", DIM_PROPERTIES);
            Registries.getInstance().register("signalindustries:chunk_generators", CHUNK_GENERATORS);
            Registries.getInstance().register("signalindustries:chunk_decorators", CHUNK_DECORATORS);
            Registries.getInstance().register("signalindustries:surface_generators", SURFACE_GENERATORS);
            init = true;
        }
    }

    private static void initDimProperties() {
        if(!init){
            DIM_PROPERTIES.register("signalindustries:block", DimPropertyBlock.class);
            DIM_PROPERTIES.register("signalindustries:biome", DimPropertyBiome.class);
            DIM_PROPERTIES.register("signalindustries:season", DimPropertySeason.class);
            DIM_PROPERTIES.register("signalindustries:weather", DimPropertyWeather.class);
            DIM_PROPERTIES.register("signalindustries:int", DimPropertyInt.class);
            DIM_PROPERTIES.register("signalindustries:float", DimPropertyFloat.class);
            DIM_PROPERTIES.register("signalindustries:boolean", DimPropertyBoolean.class);
            DIM_PROPERTIES.register("signalindustries:string", DimPropertyString.class);
            DIM_PROPERTIES.register("signalindustries:color", DimPropertyColor.class);
        }
    }

    private static void initChunkGenerators() {
        if(!init){
            CHUNK_GENERATORS.register("signalindustries:empty", ChunkGeneratorEmpty.class);
            CHUNK_GENERATORS.register("signalindustries:classic", ChunkGeneratorClassic.class);
        }
    }

    private static void initChunkDecorators() {
        if(!init){
            CHUNK_DECORATORS.register("signalindustries:empty", ChunkDecoratorEmpty.class);
            CHUNK_DECORATORS.register("signalindustries:custom", ChunkDecoratorCustom.class);
            CHUNK_DECORATORS.register("minecraft:overworld", ChunkDecoratorWorld.class);
        }
    }

    private static void initSurfaceGenerators() {
        if(!init){
            SURFACE_GENERATORS.register("signalindustries:empty", SurfaceGeneratorEmpty.class);
            SURFACE_GENERATORS.register("signalindustries:overworld", SurfaceGeneratorOverworld.class);
            SURFACE_GENERATORS.register("signalindustries:retro", SurfaceGeneratorRetro.class);
        }
    }

    private static void initLargeFeatures() {
        if(!init){
            LARGE_FEATURES.register("minecraft:caves", CavesLargeFeature.class);
        }
    }

    private static void initFeatures() {
        if(!init){
            FEATURES.register("signalindustries:ore", WorldFeatureOre.class);
        }
    }
}
