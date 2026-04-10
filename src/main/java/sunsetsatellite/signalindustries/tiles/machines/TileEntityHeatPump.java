package sunsetsatellite.signalindustries.tiles.machines;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.*;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.RecipeProperties;

import java.util.*;
import java.util.stream.Collectors;

public class TileEntityHeatPump extends TileEntityTieredMachineBase implements IBoostable {

    public BlockInstance currentBlock = null;
    public RecipeEntrySI<?, ?, RecipeProperties> currentRecipe;
    public TickTimer pumpTimer = new TickTimer(this, this::findFluid, 20, true);
    public int range = 3;
    public Random rand = new Random();


    public TileEntityHeatPump() {
        fluidContents = new FluidStack[1];
        itemContents = new ItemStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 2000;
        progressMaxTicks = 600;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }

    public void findFluid() {
        if (worldObj == null) return;
        if (currentBlock == null || currentRecipe == null) {
            Set<Fluid> pumpableFluids = new HashSet<>();
            pumpableFluids.add(Fluids.WATER);
            for (int pumpX = x - range; pumpX < x + range; pumpX++) {
                //for (int pumpY = y - 1; pumpY > y - range - 1; pumpY--) {
                int pumpY = y - 1;
                    for (int pumpZ = z - range; pumpZ < z + range; pumpZ++) {
                        Block<?> block = worldObj.getBlock(pumpX, pumpY, pumpZ);
                        int metadata = worldObj.getBlockMetadata(pumpX, pumpY, pumpZ);
                        BlockLogicFluid logic = Catalyst.blockLogic(block, BlockLogicFluid.class);
                        if (block != null) {
                            if(Catalyst.listContains(Registries.ITEM_GROUPS.getItem("minecraft:cobblestones"), new ItemStack(block, 1, metadata), ItemStack::isItemEqual)){
                                currentBlock = new BlockInstance(block, new Vec3i(pumpX, pumpY, pumpZ), metadata, null);
                                currentRecipe = SIRecipes.HEAT_PUMP.getItem("melting");
                                return;
                            }
                            if(Catalyst.listContains(Registries.ITEM_GROUPS.getItem("minecraft:stones"), new ItemStack(block, 1, metadata), ItemStack::isItemEqual)){
                                currentBlock = new BlockInstance(block, new Vec3i(pumpX, pumpY, pumpZ), metadata, null);
                                currentRecipe = SIRecipes.HEAT_PUMP.getItem("melting");
                                return;
                            }
                        }
                         if (block != null && logic != null) {
                            List<Fluid> fluids = pumpableFluids.stream().filter((F) -> F.blocks.contains(block)).collect(Collectors.toList());
                            if (!fluids.isEmpty()) {
                                currentBlock = new BlockInstance(block, new Vec3i(pumpX, pumpY, pumpZ), null);
                                currentRecipe = SIRecipes.HEAT_PUMP.getItem("freezing");
                                return;
                            }
                        }
                    }
                //}
            }
        }
    }

    @Override
    public void init(Block<?> block) {
        super.init(block);
        range = 3 * (tier.ordinal() + 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        pumpTimer.tick();
        extractFluids();

        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (fluidContents[0] == null) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = getTieredProgressDuration(currentRecipe.getData().ticks); //(int) (currentRecipe.getData().ticks / speedMultiplier);
        }
        if (!worldObj.isClientSide) {
            if (progressTicks == 0 && canProcess()) {
                update = fuel();
            } else if (progressTicks > 0 && fuelBurnTicks <= 0 && canProcess()) {
                update = fuel();
            }
            if (isBurning() && canProcess()) {
                progressTicks++;
                if (progressTicks >= progressMaxTicks) {
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if (canProcess()) {
                fuel();
                if (fuelBurnTicks > 0) {
                    fuelBurnTicks++;
                }
            }
        }

        if (update) {
            this.setChanged();
        }

    }


    public boolean fuel() {
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if (burn > 0 && canProcess() && fluidContents[0].amount >= currentRecipe.getData().cost) {
            progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= currentRecipe.getData().cost;
            if (fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    public void processItem() {
        if (canProcess() && worldObj != null) {
            if(currentRecipe == SIRecipes.HEAT_PUMP.getItem("melting")){
                Block<?> block = worldObj.getBlock(currentBlock.pos.x, currentBlock.pos.y, currentBlock.pos.z);
                if(block != null && Catalyst.listContains(Registries.ITEM_GROUPS.getItem("minecraft:stones"), new ItemStack(block, 1, currentBlock.meta), ItemStack::isItemEqual)){
                    worldObj.setBlockWithNotify(currentBlock.pos.x, currentBlock.pos.y, currentBlock.pos.z, Blocks.FLUID_LAVA_STILL.id());
                }
            }
            if(currentRecipe == SIRecipes.HEAT_PUMP.getItem("freezing")){
                Block<?> block = worldObj.getBlock(currentBlock.pos.x, currentBlock.pos.y, currentBlock.pos.z);
                if(block != null && Catalyst.listContains(Registries.ITEM_GROUPS.getItem("minecraft:stones"), new ItemStack(block, 1, currentBlock.meta), ItemStack::isItemEqual)) {
                    worldObj.setBlockWithNotify(currentBlock.pos.x, currentBlock.pos.y, currentBlock.pos.z, Blocks.ICE.id());
                }
            }
            currentBlock = null;
            currentRecipe = null;
        }
    }

    private boolean canProcess() {
        if (currentBlock == null || currentRecipe == null) {
            return false;
        }
        if(currentRecipe == SIRecipes.HEAT_PUMP.getItem("melting")){
            return itemContents[0] != null && itemContents[0].getItem().equals(SIItems.heatingCoil);
        }
        if(currentRecipe == SIRecipes.HEAT_PUMP.getItem("freezing")){
            return itemContents[0] != null && itemContents[0].getItem().equals(SIItems.coolingCoil);
        }
        return false;
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.heatPump";
    }
}
