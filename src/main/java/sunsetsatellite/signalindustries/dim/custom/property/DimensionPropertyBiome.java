package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.biome.provider.BiomeProviderOverworld;
import net.minecraft.core.world.biome.provider.BiomeProviderSingleBiome;

public class DimensionPropertyBiome extends DimensionPropertyBase {

    public boolean single = false;
    public Biome singleBiome;

    public DimensionPropertyBiome(CompoundTag nbt) {
        super(nbt);
    }

    public DimensionPropertyBiome(Biome biome) {
        single = true;
        singleBiome = biome;
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        if(nbt.getBoolean("Single")){
            single = true;
            singleBiome = Registries.BIOMES.getItem(nbt.getString("Biome"));
        } else {
            single = false;
        }
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        if(single){
            nbt.putBoolean("single", true);
            nbt.putString("biome", Registries.BIOMES.getKey(singleBiome));
        } else {
            nbt.putBoolean("single", false);
        }
    }

    @Override
    public Object get() {
        throw new UnsupportedOperationException();
    }

    public BiomeProvider get(World world) {
        if(!single) {
            return new BiomeProviderOverworld(world.getRandomSeed(), world.getWorldType());
        } else {
            return new BiomeProviderSingleBiome(singleBiome, 1,1,1);
        }
    }
}
