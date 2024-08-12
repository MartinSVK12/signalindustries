package sunsetsatellite.signalindustries.experimental;

import net.minecraft.client.Minecraft;
import net.minecraft.core.world.ProgressListener;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCoordinate;
import net.minecraft.core.world.chunk.EmptyChunk;
import net.minecraft.core.world.chunk.IChunkLoader;
import net.minecraft.core.world.chunk.provider.IChunkProvider;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;

import java.io.IOException;
import java.util.*;

public class ChunkProviderDynamic implements IChunkProvider
{
    private final Set<Integer> droppedChunksSet;
    private final Chunk emptyChunk;
    public ChunkGenerator chunkGenerator;
    private final IChunkLoader chunkLoader;
    private final Map<Integer, Chunk> chunkMap;
    private final Map<Integer, Chunk> forceLoadedChunkMap;
    private final List<Chunk> forceLoadedChunkList;
    private final World world;
    private int currentChunkX;
    private int currentChunkZ;
    public int forceLoadedChunksLimit = 64;
    public int maxUnloadPerTick = 100;
    private int lastQueriedChunkXPos;
    private int lastQueriedChunkZPos;
    private Chunk lastQueriedChunk;

    public ChunkProviderDynamic(World world, IChunkLoader chunkLoader, ChunkGenerator chunkGenerator)
    {
        droppedChunksSet = new HashSet<>();
        chunkMap = new HashMap<>();
        forceLoadedChunkList = new ArrayList<>();
        forceLoadedChunkMap = new HashMap<>();
        emptyChunk = new EmptyChunk(world, 0, 0);
        this.world = world;
        this.chunkLoader = chunkLoader;
        this.chunkGenerator = chunkGenerator;
    }

    @Override
    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        int id = ChunkCoordinate.toInt(chunkX, chunkZ);
        if(canChunkBeUnloaded(chunkX, chunkZ)) {
            if(chunkMap.containsKey(id)) droppedChunksSet.add(id);
            return false;
        }
        Chunk chunk = chunkMap.get(id);
        return chunk == emptyChunk || (chunk != null && chunk.isAtLocation(chunkX, chunkZ));
    }

    public boolean canChunkBeUnloaded(int chunkX, int chunkZ) {
        int id = ChunkCoordinate.toInt(chunkX, chunkZ);
        if(forceLoadedChunkMap.containsKey(id)) return false;
        int renderDistance = Minecraft.getMinecraft(this).gameSettings.renderDistance.value.chunks;
        return chunkX <= currentChunkX - renderDistance || chunkZ <= currentChunkZ - renderDistance || chunkX >= currentChunkX + renderDistance || chunkZ >= currentChunkZ + renderDistance;
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        if(chunkX == lastQueriedChunkXPos && chunkZ == lastQueriedChunkZPos && lastQueriedChunk != null)
        {
            return lastQueriedChunk;
        }
        if(!world.findingSpawnPoint && canChunkBeUnloaded(chunkX, chunkZ)) return emptyChunk;
        int id = ChunkCoordinate.toInt(chunkX, chunkZ);
        if(!isChunkLoaded(chunkX, chunkZ)) {
            if(chunkMap.get(id) != null){
                Chunk chunk = chunkMap.get(id);
                saveChunk(chunk);
                chunk.onUnload();
            }
            Chunk chunk = loadChunk(chunkX, chunkZ);
            if(chunk == null)
            {
                if (chunkGenerator != null)
                {
                    chunk = chunkGenerator.generate(chunkX, chunkZ);
                    chunk.fixMissingBlocks();
                }
                else
                {
                    chunk = emptyChunk;
                }
            }
            chunkMap.put(id, chunk);
            chunk.onLoad();
            if(chunk.isTerrainPopulated && isChunkLoaded(chunkX + 1, chunkZ + 1) && isChunkLoaded(chunkX, chunkZ + 1) && isChunkLoaded(chunkX + 1, chunkZ))
            {
                populate(this, chunkX, chunkZ);
            }
            if(isChunkLoaded(chunkX - 1, chunkZ) && !provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && isChunkLoaded(chunkX - 1, chunkZ + 1) && isChunkLoaded(chunkX, chunkZ + 1) && isChunkLoaded(chunkX - 1, chunkZ))
            {
                populate(this, chunkX - 1, chunkZ);
            }
            if(isChunkLoaded(chunkX, chunkZ - 1) && !provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && isChunkLoaded(chunkX + 1, chunkZ - 1) && isChunkLoaded(chunkX, chunkZ - 1) && isChunkLoaded(chunkX + 1, chunkZ))
            {
                populate(this, chunkX, chunkZ - 1);
            }
            if(isChunkLoaded(chunkX - 1, chunkZ - 1) && !provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && isChunkLoaded(chunkX - 1, chunkZ - 1) && isChunkLoaded(chunkX, chunkZ - 1) && isChunkLoaded(chunkX - 1, chunkZ))
            {
                populate(this, chunkX - 1, chunkZ - 1);
            }
            if (world.getCurrentWeather() != null)
            {
                world.getCurrentWeather().doChunkLoadEffect(world, chunk);
            }
        }
        lastQueriedChunkXPos = chunkX;
        lastQueriedChunkZPos = chunkZ;
        lastQueriedChunk = chunkMap.get(id);;
        return chunkMap.get(id);
    }

    @Override
    public Chunk prepareChunk(int chunkX, int chunkZ) {
        return provideChunk(chunkX, chunkZ);
    }

    @Override
    public void regenerateChunk(int chunkX, int chunkZ)
    {
        int id = ChunkCoordinate.toInt(chunkX, chunkZ);
        droppedChunksSet.remove(id);
        chunkMap.remove(id);
        Chunk chunk;
        if (chunkGenerator != null)
        {
            chunk = chunkGenerator.generate(chunkX, chunkZ);
            chunk.fixMissingBlocks();
        }
        else return;
        chunkMap.put(id, chunk);
        if(!chunk.isTerrainPopulated && isChunkLoaded(chunkX + 1, chunkZ + 1) && isChunkLoaded(chunkX, chunkZ + 1) && isChunkLoaded(chunkX + 1, chunkZ))
        {
            populate(this, chunkX, chunkZ);
        }
        if(isChunkLoaded(chunkX - 1, chunkZ) && !provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && isChunkLoaded(chunkX - 1, chunkZ + 1) && isChunkLoaded(chunkX, chunkZ + 1) && isChunkLoaded(chunkX - 1, chunkZ))
        {
            populate(this, chunkX - 1, chunkZ);
        }
        if(isChunkLoaded(chunkX, chunkZ - 1) && !provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && isChunkLoaded(chunkX + 1, chunkZ - 1) && isChunkLoaded(chunkX, chunkZ - 1) && isChunkLoaded(chunkX + 1, chunkZ))
        {
            populate(this, chunkX, chunkZ - 1);
        }
        if(isChunkLoaded(chunkX - 1, chunkZ - 1) && !provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && isChunkLoaded(chunkX - 1, chunkZ - 1) && isChunkLoaded(chunkX, chunkZ - 1) && isChunkLoaded(chunkX - 1, chunkZ))
        {
            populate(this, chunkX - 1, chunkZ - 1);
        }
    }

    @Override
    public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ)
    {
        Chunk chunk = provideChunk(chunkX, chunkZ);
        if(!chunk.isTerrainPopulated)
        {
            chunk.isTerrainPopulated = true;
            if (this.chunkGenerator != null)
            {
                this.chunkGenerator.decorate(chunk);
                chunk.setChunkModified();
            }
        }
    }

    public boolean keepLoaded(int chunkX, int chunkZ) {
        int id = ChunkCoordinate.toInt(chunkX, chunkZ);
        Chunk chunk = chunkMap.get(id);
        if(!forceLoadedChunkMap.containsKey(id) && forceLoadedChunkList.size() < forceLoadedChunksLimit) {
            forceLoadedChunkMap.put(id, chunk);
            forceLoadedChunkList.add(chunk);
            return true;
        }
        return false;
    }

    public boolean removeFromForceLoaded(int chunkX, int chunkZ){
        int id = ChunkCoordinate.toInt(chunkX, chunkZ);
        forceLoadedChunkList.remove(forceLoadedChunkMap.get(id));
        return forceLoadedChunkMap.remove(id) != null;
    }

    @Override
    public boolean saveChunks(boolean saveImmediately, ProgressListener progressUpdate)
    {
        int attempts = 0;
        int chunksToBeSaved = 0;
        if(progressUpdate != null)
        {
            for (Chunk chunk : chunkMap.values()) {
                if(chunk != null && chunk.needsSaving(saveImmediately)) chunksToBeSaved++;
            }
        }
        int progress = 0;
        for (Chunk chunk : chunkMap.values()) {
            if(chunk == null) continue;
            if(!chunk.needsSaving(saveImmediately)) continue;
            saveChunk(chunk);
            chunk.isModified = false;
            if(++attempts == 2 && !saveImmediately) return false;
            if(progressUpdate != null && ++progress % 10 == 0) progressUpdate.progressStagePercentage((progress * 100) / chunksToBeSaved);
        }
        return true;
    }

    private void saveChunk(Chunk chunk)
    {
        if(chunkLoader == null) return;
        try
        {
            chunk.lastSaveTime = world.getWorldTime();
            chunkLoader.saveChunk(world, chunk);
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private Chunk loadChunk(int i, int j)
    {
        if(chunkLoader == null) return emptyChunk;
        try
        {
            Chunk chunk = chunkLoader.loadChunk(world, i, j);
            if(chunk != null)
            {
                chunk.lastSaveTime = world.getWorldTime();
            }
            return chunk;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return emptyChunk;
    }

    @Override
    public boolean tick() {
        int dropped = 0;
        for (Chunk chunk : chunkMap.values()) {
            if(dropped >= maxUnloadPerTick) break;
            if(!isChunkLoaded(chunk.xPosition,chunk.zPosition)){
                break;
            } //droppedChunksSet gets updated as side-effect
        }
        if(!droppedChunksSet.isEmpty()){
            int unloaded = 0;
            HashSet<Integer> copy = new HashSet<>(droppedChunksSet);
            for (int i : copy) {
                if(unloaded >= maxUnloadPerTick) break;
                if(forceLoadedChunkMap.containsKey(i)) continue;
                if(chunkMap.containsKey(i)){
                    Chunk chunk = chunkMap.get(i);
                    chunk.onUnload();
                    saveChunk(chunk);
                    chunkMap.remove(i);
                    droppedChunksSet.remove(i);
                    unloaded++;
                } else {
                    droppedChunksSet.remove(i);
                }
            }
        }
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    public IChunkLoader getChunkLoader() {
        return chunkLoader;
    }

    @Override
    public String getInfoString() {
        return "Chunks: " + chunkMap.size() + ", " + "Dropped: " + droppedChunksSet.size()+", "+"Chunkloaded: "+forceLoadedChunkMap.size()+"/"+forceLoadedChunksLimit;
    }

    @Override
    public void unloadAllChunks() {
        for (Chunk chunk : chunkMap.values()) {
            if(chunk != null) chunk.onUnload();
        }
        chunkMap.clear();
        droppedChunksSet.clear();
        chunkGenerator = null;
        System.gc();
    }

    @Override
    public void setCurrentChunkOver(int chunkX, int chunkZ) {
        currentChunkX = chunkX;
        currentChunkZ = chunkZ;
    }
}