package sunsetsatellite.signalindustries.tiles.base;


import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.ITileEntityInit;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicParallelProcessor;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPartBlock;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;
import sunsetsatellite.signalindustries.util.MultiblockPart;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.*;
import java.util.stream.Collectors;

public abstract class TileEntityTieredMultiblock extends TileEntityTieredMachineBase implements IMultiblock, ITileEntityInit {

    public MultiblockInstance multiblock;
    public TileEntityItemBus itemInput;
    public TileEntityItemBus itemOutput;
    public TileEntityFluidHatch fluidInput;
    public TileEntityFluidHatch fluidOutput;
    public TileEntityEnergyConnector energy;
    public boolean usesItemInput = false;
    public boolean usesItemOutput = false;
    public boolean usesFluidInput = false;
    public boolean usesFluidOutput = false;
    public boolean usesEnergy = false;
    public Tier minimumItemInputTier = Tier.BASIC;
    public Tier minimumItemOutputTier = Tier.BASIC;
    public Tier minimumFluidInputTier = Tier.BASIC;
    public Tier minimumFluidOutputTier = Tier.BASIC;
    public Tier minimumEnergyTier = Tier.BASIC;
    public RecipeGroupSI<?> recipeGroup;
    public RecipeEntrySI<?, ?, RecipeProperties> currentRecipe;
    public Random random = new Random();

	public List<BlockInstance> cachedSubstitutions = new ArrayList<>();
	public List<BlockInstance> cachedBlocks = new ArrayList<>();
	public List<BlockInstance> cachedTEs = new ArrayList<>();
	public Direction cachedDirection;
	public Vec3i cachedPosition;

    public int parallel = 1;
    public int baseParallel = 1;

    public TileEntityTieredMultiblock() {
        super();
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
    }

    @Override
    public void tick() {
        if (multiblock == null) {
            return;
        }
        super.tick();
        if (worldObj != null) {
            worldObj.markBlockDirty(tilePos);
        }
        Block<?> block = getBlock();
        itemInput = null;
        itemOutput = null;
        fluidInput = null;
        fluidOutput = null;
        energy = null;
        if (multiblock.isValid()) {
			Direction dir = Direction.getDirectionFromSide(getBlockMeta());
			if (multiblock.data != null) {
				if(dir != cachedDirection || !getPosition().equals(cachedPosition)){
					cachedSubstitutions = multiblock.data.getSubstitutions(new Vec3i(tilePos), dir);
					cachedBlocks = multiblock.data.getBlocks(new Vec3i(tilePos), dir);
					cachedTEs = multiblock.data.getTileEntities(worldObj, new Vec3i(tilePos), dir);
					cachedDirection = dir;
					cachedPosition = new Vec3i(tilePos);
				}
	        }
            List<BlockInstance> tileEntities = cachedTEs;
            for (BlockInstance tileEntity : tileEntities) {
                IMultiblockPartBlock multiblockPart = Catalyst.blockLogic(tileEntity.block, IMultiblockPartBlock.class);
                if (tileEntity.tile instanceof IMultiblockPart && multiblockPart instanceof ITiered) {
                    if (multiblockPart.getType() == MultiblockPart.Type.ITEM && multiblockPart.getIO() == MultiblockPart.IO.INPUT) {
                        if (((ITiered) multiblockPart).getTier().ordinal() >= minimumItemInputTier.ordinal()) {
                            itemInput = (TileEntityItemBus) tileEntity.tile;
                        }
                    } else if (multiblockPart.getType() == MultiblockPart.Type.ITEM && multiblockPart.getIO() == MultiblockPart.IO.OUTPUT) {
                        if (((ITiered) multiblockPart).getTier().ordinal() >= minimumItemOutputTier.ordinal()) {
                            itemOutput = (TileEntityItemBus) tileEntity.tile;
                        }
                    } else if (tileEntity.tile instanceof TileEntityEnergyConnector) {
                        if (((ITiered) multiblockPart).getTier().ordinal() >= minimumEnergyTier.ordinal()) {
                            energy = (TileEntityEnergyConnector) tileEntity.tile;
                        }
                    } else if (multiblockPart.getType() == MultiblockPart.Type.FLUID && multiblockPart.getIO() == MultiblockPart.IO.INPUT) {
                        if (((ITiered) multiblockPart).getTier().ordinal() >= minimumFluidInputTier.ordinal()) {
                            fluidInput = (TileEntityFluidHatch) tileEntity.tile;
                        }
                    } else if (multiblockPart.getType() == MultiblockPart.Type.FLUID && multiblockPart.getIO() == MultiblockPart.IO.INPUT) {
                        if (((ITiered) multiblockPart).getTier().ordinal() >= minimumFluidOutputTier.ordinal()) {
                            fluidOutput = (TileEntityFluidHatch) tileEntity.tile;
                        }
                    }
                    ((IMultiblockPart) tileEntity.tile).connect(this);
                }
            }
            parallel = baseParallel;
            List<BlockInstance> extraBlocks = cachedSubstitutions;
            for (BlockInstance extraBlock : extraBlocks) {
                if (worldObj != null && extraBlock.exists(worldObj)) {
                    BlockLogicParallelProcessor multiblockPart = Catalyst.blockLogic(extraBlock.block, BlockLogicParallelProcessor.class);
                    if (multiblockPart != null && multiblockPart.getType() == MultiblockPart.Type.PARALLEL) {
                        parallel = baseParallel * multiblockPart.maxParallel;
                    }
                }
            }
            if (block != null && allPartsPresent()) {
                int oldParallel = parallel;
                parallel = 1;
                setCurrentRecipe();
                parallel = oldParallel;
                if (currentRecipe != null) {
                    int recipeInputSum = Arrays.stream(((RecipeExtendedSymbol[]) currentRecipe.getInput())).map(RecipeExtendedSymbol::asNormalSymbol).map(RecipeSymbol::resolve).map(L -> L.get(0)).mapToInt(S -> S.stackSize).sum();
                    int inputSum = 0;
                    if (itemInput != null) {
                        inputSum += Catalyst.condenseItemList(Arrays.asList(itemInput.itemContents)).stream().mapToInt(S -> S.stackSize).sum();
                    }
                    if (fluidInput != null) {
                        inputSum += CatalystFluids.condenseFluidList(Arrays.asList(fluidInput.fluidContents)).stream().mapToInt(S -> S.amount).sum();
                    }
                    int effectiveParallel = inputSum / recipeInputSum;

                    if (parallel > effectiveParallel && effectiveParallel > 0) {
                        parallel = effectiveParallel;
                    }
                }
                setCurrentRecipe();
                work();
            }

			int maxChange = Integer.MAX_VALUE;
            if (isBurning()) {
                List<BlockInstance> blocks = cachedBlocks;
				int i = 0;
                for (BlockInstance structBlock : blocks) {
					if(i >= maxChange) break;
                    if (structBlock.block == SIBlocks.reinforcedCasing2 || structBlock.block == SIBlocks.awakenedSocketCasing || structBlock.block == SIBlocks.awakenedCasing2) {
                        if (worldObj != null && structBlock.pos.getBlockMetadata(worldObj) != 1) {
                            worldObj.setBlockDataNotify(structBlock.pos.tilePos(), 1);
							i++;
                        }
                    }
                }
            } else {
				int i = 0;
                List<BlockInstance> blocks = cachedBlocks;
                for (BlockInstance structBlock : blocks) {
					if(i >= maxChange) break;
                    if (structBlock.block == SIBlocks.reinforcedCasing2 || structBlock.block == SIBlocks.awakenedSocketCasing || structBlock.block == SIBlocks.awakenedCasing2) {
                        if (worldObj != null && structBlock.pos.getBlockMetadata(worldObj) == 1) {
                            worldObj.setBlockDataNotify(structBlock.pos.tilePos(), 0);
							i++;
                        }
                    }
                }
            }
        }
    }

    public boolean allPartsPresent() {
        return (itemInput != null || !usesItemInput)
                && (itemOutput != null || !usesItemOutput)
                && (fluidInput != null || !usesFluidInput)
                && (fluidOutput != null || !usesFluidOutput)
                && (energy != null || !usesEnergy);
    }

    public void work() {
        if (multiblock.isValid() && allPartsPresent()) {
            boolean update = false;
            if (fuelBurnTicks > 0) {
                fuelBurnTicks--;
            }
            ArrayList<ItemStack> inputContents = getItemInputContents();
            ArrayList<FluidStack> fluidInputContents = getFluidInputContents();
            if (inputContents.isEmpty() && fluidInputContents.isEmpty()) {
                progressTicks = 0;
            } else if (canProcess()) {
                progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
            }
            if (worldObj != null && !worldObj.isClientSide) {
                if (progressTicks == 0 && canProcess() && fuelBurnTicks < 2) {
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
    }

    @Override
    public boolean isBurning() {
        if (multiblock == null) return false;
        return super.isBurning() && multiblock.isValid();
    }

    @NotNull
    private ArrayList<ItemStack> getItemInputContents() {
        if (!allPartsPresent() || !usesItemInput) {
            return new ArrayList<>();
        }
        return Catalyst.condenseItemList(Arrays.asList(itemInput.itemContents));
    }

    @NotNull
    private ArrayList<ItemStack> getItemOutputContents() {
        if (!allPartsPresent() || !usesItemOutput) {
            return new ArrayList<>();
        }
        return Catalyst.condenseItemList(Arrays.asList(itemOutput.itemContents));
    }

    @NotNull
    private ArrayList<FluidStack> getFluidInputContents() {
        if (!allPartsPresent() || !usesFluidInput) {
            return new ArrayList<>();
        }
        return CatalystFluids.condenseFluidList(Arrays.asList(fluidInput.fluidContents));
    }

    @NotNull
    private ArrayList<FluidStack> getFluidOutputContents() {
        if (!allPartsPresent() || !usesFluidOutput) {
            return new ArrayList<>();
        }
        return CatalystFluids.condenseFluidList(Arrays.asList(fluidOutput.fluidContents));
    }

    @Override
    public MultiblockInstance getMultiblock() {
        return multiblock;
    }

    public boolean fuel() {
        if (allPartsPresent() && energy.getFluidInSlot(0) != null) {
            int burn = SignalIndustries.getEnergyBurnTime(energy.getFluidInSlot(0));
            if (burn > 0 && canProcess() && energy.getFluidInSlot(0) != null && currentRecipe != null) {
                if (energy.getFluidInSlot(0) != null && energy.getFluidInSlot(0).amount >= currentRecipe.getData().cost) {
                    progressMaxTicks = (int) (currentRecipe.getData().ticks / speedMultiplier);
                    fuelMaxBurnTicks = fuelBurnTicks = burn;
                    energy.getFluidInSlot(0).amount -= currentRecipe.getData().cost;
                    if (energy.getFluidInSlot(0) != null && energy.getFluidInSlot(0).amount <= 0) {
                        energy.setFluidInSlot(0, null);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void setCurrentRecipe() {
        if (allPartsPresent()) {
            List<Object> objs = new ArrayList<>();
            if (usesItemInput) {
                List<ItemStack> items = getItemInputContents().stream().map(ItemStack::copy).toList();
                items.forEach(stack -> stack.stackSize /= parallel);
                objs.addAll(items);
            }
            if (usesFluidInput) {
                List<FluidStack> fluids = getFluidInputContents().stream().map(FluidStack::copy).toList();
                fluids.forEach(stack -> stack.amount /= parallel);
                objs.addAll(fluids);
            }
            objs = objs.stream().map(o -> {
                if (o instanceof ItemStack) {
                    if (((ItemStack) o).stackSize <= 0) {
                        return null;
                    }
                } else if (o instanceof FluidStack) {
                    if (((FluidStack) o).amount <= 0) {
                        return null;
                    }
                } else {
                    return null;
                }
                return o;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            RecipeExtendedSymbol[] symbols = RecipeExtendedSymbol.arrayOf(objs);
            currentRecipe = recipeGroup.findRecipe(symbols, tier);
        }
    }


    public boolean canProcess() {
        if (allPartsPresent() && currentRecipe != null) {
            return currentRecipe.canMultiblockProcess(this);
        }
        return false;
    }

    public boolean areItemOutputsValid(ItemStack stack) {
        if (!usesItemOutput) return true;
        int outputAmountRemaining;
        outputAmountRemaining = stack.stackSize;

        outputAmountRemaining *= parallel;
        stack = stack.copy();
        stack.stackSize *= parallel;

        if (outputAmountRemaining <= 0) return true;
        for (ItemStack outputStack : itemOutput.itemContents) {
            if (outputStack != null) {
                if (outputStack.isItemEqual(stack)) {
                    int maxFreeAmountInSlot = Math.min(outputStack.getMaxStackSize(), itemOutput.getMaxStackSize()) - outputStack.stackSize;
                    int willTake = Math.min(outputAmountRemaining, maxFreeAmountInSlot);
                    outputAmountRemaining -= willTake;
                }
            } else {
                int maxFreeAmountInSlot = Math.min(itemOutput.getMaxStackSize(), stack.getMaxStackSize());
                int willTake = Math.min(outputAmountRemaining, maxFreeAmountInSlot);
                outputAmountRemaining -= willTake;
            }
            if (outputAmountRemaining <= 0) {
                break;
            }
        }

        return outputAmountRemaining <= 0;
    }

    public boolean areFluidOutputsValid(FluidStack stack) {
        if (!usesFluidOutput) return true;
        int outputAmountRemaining;
        outputAmountRemaining = stack.amount;

        outputAmountRemaining *= parallel;

        if (outputAmountRemaining <= 0) return true;
        FluidStack[] contents = fluidOutput.fluidContents;
        for (int i = 0; i < contents.length; i++) {
            FluidStack outputStack = contents[i];
            if (outputStack != null) {
                if (outputStack.isFluidEqual(stack)) {
                    int maxFreeAmountInSlot = fluidOutput.getFluidCapacityForSlot(i) - outputStack.amount;
                    int willTake = Math.min(outputAmountRemaining, maxFreeAmountInSlot);
                    outputAmountRemaining -= willTake;
                }
            } else {
                int maxFreeAmountInSlot = fluidOutput.getFluidCapacityForSlot(i);
                int willTake = Math.min(outputAmountRemaining, maxFreeAmountInSlot);
                outputAmountRemaining -= willTake;
            }
            if (outputAmountRemaining <= 0) {
                break;
            }
        }

        return outputAmountRemaining <= 0;
    }

    public void processItem() {
        if (canProcess()) {
            currentRecipe.processMultiblockRecipe(this);
        }
    }

    public void consumeInputs() {
        if (currentRecipe != null) {
            currentRecipe.consumeMultiblockInputs(this);
        }
    }

	@Override
	public void sort() {

	}
}
