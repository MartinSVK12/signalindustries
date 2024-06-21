package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

import java.util.Collections;
import java.util.Set;

public abstract class CoverBase {

    protected Direction dir;
    protected IAcceptsCovers machine;

    public CoverBase() {
    }

    public Direction getDir() {
        return dir;
    }

    public IAcceptsCovers getMachine() {
        return machine;
    }

    public abstract void openConfiguration(EntityPlayer player);

    public void writeToNbt(CompoundTag tag) {
        tag.putString("Type",getClass().getCanonicalName());
        tag.putInt("Direction",dir.ordinal());
    }

    public void readFromNbt(CompoundTag tag) {
        dir = Direction.values()[tag.getInteger("Direction")];
    }


    //should be only called before readFromNbt when loading machine covers on world/chunk load
    public void setup(Direction dir, IAcceptsCovers machine){
        this.dir = dir;
        this.machine = machine;
    }

    public void onInstalled(Direction dir, IAcceptsCovers machine, EntityPlayer player) {
        this.dir = dir;
        this.machine = machine;
    }

    public void onRemoved(EntityPlayer player) {
        this.dir = null;
        this.machine = null;
    }

    public void onInstalled(Direction dir, IAcceptsCovers machine) {
        this.dir = dir;
        this.machine = machine;
    }

    public void onRemoved() {
        this.dir = null;
        this.machine = null;
    }

    public abstract void tick();

    public abstract IconCoordinate getTexture();

    public abstract ItemCover getItem();

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }
}
