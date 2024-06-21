package sunsetsatellite.signalindustries.blocks.machines;


import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemBucketEmpty;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.containers.ContainerFluidTank;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.gui.GuiSIFluidTank;
import sunsetsatellite.signalindustries.inventories.TileEntitySIFluidTank;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockSIFluidTank extends BlockMachineBase {

    public BlockSIFluidTank(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
        withOverbright();
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntitySIFluidTank();
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        super.onBlockRemoved(world, i, j, k, data);
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(super.blockActivated(world, i, j, k, entityplayer)){
            return true;
        }
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntitySIFluidTank tile = (TileEntitySIFluidTank) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                ItemStack equippedStack = entityplayer.getCurrentEquippedItem();
                if(equippedStack != null){
                    Item equippedItem = equippedStack.getItem();
                    if(!(CatalystFluids.FLUIDS.findFluidsWithFilledContainer(equippedItem).isEmpty())){
                        for (BlockFluid fluid : CatalystFluids.FLUIDS.findFluidsWithFilledContainer(equippedItem)) {
                            if(equippedItem instanceof ItemBucket){
                                FluidStack stack = new FluidStack(fluid,1000);
                                if(tile.canInsertFluid(0,stack) && tile.getRemainingCapacity(0) >= 1000){
                                    if(tile.getAllowedFluidsForSlot(0).contains(fluid)){
                                        tile.insertFluid(0,stack);
                                        Item emptyContainer = CatalystFluids.FLUIDS.findEmptyContainersWithContainer(fluid, equippedItem).get(0);
                                        entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = new ItemStack(emptyContainer);
                                        return true;
                                    }
                                }
                            } else if (equippedItem instanceof IItemFluidContainer) {
                                //TODO:
                            }
                        }
                    } else if(!(CatalystFluids.FLUIDS.findFluidsWithEmptyContainer(equippedItem).isEmpty())){
                        for (BlockFluid fluid : CatalystFluids.FLUIDS.findFluidsWithEmptyContainer(equippedItem)) {
                            if (equippedItem instanceof ItemBucketEmpty) {
                                if(tile.getFluidInSlot(0) != null && tile.getFluidInSlot(0).isFluidEqual(fluid)){
                                    if(tile.getFluidInSlot(0).amount >= 1000){
                                        tile.getFluidInSlot(0).amount -= 1000;
                                        Item filledContainer = CatalystFluids.FLUIDS.findFilledContainersWithContainer(fluid, equippedItem).get(0);
                                        entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = new ItemStack(filledContainer);
                                        return true;
                                    }
                                }
                            } else if (equippedItem instanceof IItemFluidContainer) {
                                //TODO:
                            }
                        }
                    }
                }
                SignalIndustries.displayGui(entityplayer,() -> new GuiSIFluidTank(entityplayer.inventory, tile),new ContainerFluidTank(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }
}
