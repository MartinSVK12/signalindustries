package sunsetsatellite.signalindustries.inventories.base;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.covers.CoverBase;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TileEntityCoverable extends TileEntityFluidItemContainer implements IAcceptsCovers {

    protected final HashMap<Direction, CoverBase> covers = (HashMap<Direction, CoverBase>) Catalyst.mapOf(Direction.values(),new CoverBase[Direction.values().length]);

    @Override
    public void tick() {
        worldObj.markBlockDirty(x,y,z);
        covers.values().stream().filter(Objects::nonNull).forEach(CoverBase::tick);
        super.tick();
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        CompoundTag coversNbt = new CompoundTag();

        for (Map.Entry<Direction, CoverBase> entry : covers.entrySet()) {
            if(entry.getValue() == null) continue;
            CompoundTag coverNbt = new CompoundTag();
            entry.getValue().writeToNbt(coverNbt);
            coversNbt.putCompound(String.valueOf(entry.getKey().ordinal()),coverNbt);
        }

        tag.putCompound("Covers",coversNbt);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        CompoundTag coversNbt = tag.getCompound("Covers");

        for (Map.Entry<String, Tag<?>> entry : coversNbt.getValue().entrySet()) {
            Direction dir = Direction.values()[Integer.parseInt(entry.getKey())];
            CompoundTag coverTag = (CompoundTag) entry.getValue();
            String type = coverTag.getString("Type");
            try {
                CoverBase cover = (CoverBase) Class.forName(type).getConstructor().newInstance();
                cover.setup(dir,this);
                covers.put(dir, cover);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean installCover(Direction dir, CoverBase cover, EntityPlayer player) {
        if(covers.get(dir) == null){
            covers.put(dir, cover);
            cover.onInstalled(dir,this,player);
            return true;
        }
        return false;
    }

    @Override
    public boolean installCover(Direction dir, CoverBase cover) {
        if(covers.get(dir) == null){
            covers.put(dir, cover);
            cover.onInstalled(dir,this);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCover(Direction dir, CoverBase cover, EntityPlayer player) {
        CoverBase installedCover = covers.get(dir);
        if(installedCover != null && installedCover == cover){
            EntityItem entityItem = new EntityItem(worldObj,x,y,z, new ItemStack(installedCover.getItem()));
            player.world.entityJoinedWorld(entityItem);
            installedCover.onRemoved(player);
            covers.remove(dir);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCover(Direction dir, CoverBase cover) {
        CoverBase installedCover = covers.get(dir);
        if(installedCover != null && installedCover == cover){
            EntityItem entityItem = new EntityItem(worldObj,x,y,z, new ItemStack(installedCover.getItem()));
            worldObj.entityJoinedWorld(entityItem);
            installedCover.onRemoved();
            covers.remove(dir);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCover(Direction dir, EntityPlayer player) {
        CoverBase installedCover = covers.get(dir);
        if(installedCover != null){
            EntityItem entityItem = new EntityItem(worldObj,x,y,z, new ItemStack(installedCover.getItem()));
            worldObj.entityJoinedWorld(entityItem);
            installedCover.onRemoved(player);
            covers.remove(dir);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCover(Direction dir) {
        CoverBase installedCover = covers.get(dir);
        if(installedCover != null){
            EntityItem entityItem = new EntityItem(worldObj,x,y,z, new ItemStack(installedCover.getItem()));
            worldObj.entityJoinedWorld(entityItem);
            installedCover.onRemoved();
            covers.remove(dir);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasCover(Direction dir, Class<? extends CoverBase> cover) {
        return covers.get(dir) != null && covers.get(dir).getClass().isAssignableFrom(cover);
    }

    @Override
    public boolean hasCoverAnywhere(Class<? extends CoverBase> cover) {
        return covers.values().stream().anyMatch((C)-> C != null && cover.isAssignableFrom(C.getClass()));
    }

    @Override
    public Map<Direction, CoverBase> getCovers() {
        return Collections.unmodifiableMap(covers);
    }
}
