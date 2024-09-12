package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;

import java.util.HashMap;
import java.util.Map;

public class TileEntityFluidConduit extends TileEntityFluidPipe implements ISupportsMultiparts {

    public final HashMap<Direction, Multipart> parts = (HashMap<Direction, Multipart>) Catalyst.mapOf(Direction.values(),new Multipart[Direction.values().length]);

    public String getInvName() {
        return "Fluid Conduit";
    }

    public TileEntityFluidConduit(){
        acceptedFluids.get(0).clear();
        for (BlockFluid fluid : CatalystFluids.CONTAINERS.getAllFluids()) {
            if(fluid != SIBlocks.energyFlowing) {
                acceptedFluids.get(0).add(fluid);
            }
        }
    }

    @Override
    public void tick() {
        if(fluidContents[0] != null && fluidContents[0].amount < 0){
            fluidContents[0] = null;
        }
        if(getBlockType() != null){
            fluidCapacity[0] = (int) Math.pow(2,((BlockContainerTiered)getBlockType()).tier.ordinal()) * 1000;
            transferSpeed = 20 * (((BlockContainerTiered)getBlockType()).tier.ordinal()+1);
            super.tick();
        }
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
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }

    @Override
    public HashMap<Direction, Multipart> getParts() {
        return parts;
    }
}
