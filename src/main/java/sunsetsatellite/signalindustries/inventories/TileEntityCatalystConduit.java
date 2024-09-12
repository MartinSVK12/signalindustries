package sunsetsatellite.signalindustries.inventories;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.HashMap;
import java.util.Map;

public class TileEntityCatalystConduit extends TileEntityEnergyConductor implements ISupportsMultiparts {

    public final HashMap<Direction, Multipart> parts = (HashMap<Direction, Multipart>) Catalyst.mapOf(Direction.values(),new Multipart[Direction.values().length]);

    public TileEntityCatalystConduit() {
        for (Direction dir : Direction.values()){
            setConnection(dir, Connection.BOTH);
        }
    }

    @Override
    public void tick() {
        if(getBlockType() != null){
            Tier tier = ((ITiered) getBlockType()).getTier();
            setCapacity((int) Math.pow(2,tier.ordinal()) * 1024);
            setTransfer(128 * (tier.ordinal()+1));
        }
        super.tick();
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        CompoundTag coversNbt = new CompoundTag();

        for (Map.Entry<Direction, Multipart> entry : parts.entrySet()) {
            if(entry.getValue() == null) continue;
            CompoundTag partNbt = new CompoundTag();
            entry.getValue().writeToNbt(partNbt);
            coversNbt.putCompound(String.valueOf(entry.getKey().ordinal()),partNbt);
        }

        tag.putCompound("Parts",coversNbt);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        CompoundTag coversNbt = tag.getCompound("Parts");

        for (Map.Entry<String, Tag<?>> entry : coversNbt.getValue().entrySet()) {
            Direction dir = Direction.values()[Integer.parseInt(entry.getKey())];
            CompoundTag partTag = (CompoundTag) entry.getValue();
            parts.put(dir,new Multipart(partTag));
        }
    }

    @Override
    public HashMap<Direction, Multipart> getParts() {
        return parts;
    }
}
