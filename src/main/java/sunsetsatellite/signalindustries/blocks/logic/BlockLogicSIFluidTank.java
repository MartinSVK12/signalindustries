package sunsetsatellite.signalindustries.blocks.logic;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntitySIFluidTank;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicSIFluidTank extends BlockLogicMachine {
    public BlockLogicSIFluidTank(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, material, tier, tileEntitySupplier, guiId);
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        TileEntitySIFluidTank tile = (TileEntitySIFluidTank) tileEntity;
        CompoundTag fluidContents = new CompoundTag();
        ItemStack stack = new ItemStack(this);
        if(tile != null && tile.getFluidInSlot(0) != null){
            tile.getFluidInSlot(0).writeToNBT(fluidContents);
            stack.getData().putCompound("Fluid",fluidContents);
        }
        return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{stack} : null;
    }

    @Override
    public String getDescription(ItemStack stack) {
        if(stack.getData().containsKey("Fluid")){
            NamespaceID fluidId;
            try {
                fluidId = NamespaceID.getTemp(stack.getData().getCompound("Fluid").getString("fluid"));
            } catch (HardIllegalArgumentException e) {
                throw new RuntimeException(e);
            }
            Fluid fluid = Fluid.fluidMap.get(fluidId);
            int amount = stack.getData().getCompound("Fluid").getInteger("amount");
            int maxAmount = (int) Math.pow(2, tier.ordinal()) * 16000;
            return super.getDescription(stack)+"\n"+String.format("Contains: %d/%d mB %s",amount, maxAmount, fluid.getName());
        }
        return super.getDescription(stack);
    }
}
