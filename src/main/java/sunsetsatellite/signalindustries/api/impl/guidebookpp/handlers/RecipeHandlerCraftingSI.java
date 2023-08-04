package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;


import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.guidebookpp.recipes.RecipeCrafting;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.RecipeHelper;
import turniplabs.halplibe.mixin.accessors.CraftingManagerAccessor;

import java.util.ArrayList;

public class RecipeHandlerCraftingSI implements IRecipeHandlerBase {
    @Override
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeCrafting recipe = (RecipeCrafting)o;
        return new ContainerGuidebookRecipeCrafting(recipe.recipe);
    }

    private final RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, Block.workbench,this,new ArrayList<>());

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        createRecipe(new ItemStack(SignalIndustries.ironPlateHammer, 1),new Object[]{"012","345","678",'1',new ItemStack(Item.ingotIron,1,0),'4',new ItemStack(Item.stick,1,0),'5',new ItemStack(Item.ingotIron,1,0),'6',new ItemStack(Item.stick,1,0)});
        createRecipe(new ItemStack(SignalIndustries.diamondCuttingGear, 1),new Object[]{"012","345","678",'1',new ItemStack(Item.diamond,1,0),'3',new ItemStack(Item.diamond,1,0),'5',new ItemStack(Item.diamond,1,0),'7',new ItemStack(Item.diamond,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeMachineCore, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.stonePlate,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.stonePlate,1,0),'4',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'5',new ItemStack(SignalIndustries.stonePlate,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.stonePlate,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.cobblestonePlate, 1),new Object[]{"012","345","678",'4',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'7',new ItemStack(Block.cobbleStone,1,0)});
        createRecipe(new ItemStack(SignalIndustries.stonePlate, 1),new Object[]{"012","345","678",'4',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'7',new ItemStack(Block.stone,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeConduit, 4), new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.stonePlate,1,0),'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'2',new ItemStack(SignalIndustries.stonePlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(Block.glass,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.stonePlate,1,0),'7',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'8',new ItemStack(SignalIndustries.stonePlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeFluidConduit, 4), new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.stonePlate,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(Block.glass,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.stonePlate,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeFluidTank, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.stonePlate,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.stonePlate,1,0),'4',new ItemStack(Block.glass,1,0),'5',new ItemStack(SignalIndustries.stonePlate,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.stonePlate,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeExtractor, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(Block.furnaceStoneIdle,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeEnergyCell, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'1',new ItemStack(Block.glass,1,0),'2',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'7',new ItemStack(Block.glass,1,0),'8',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeCrusher, 1),new Object[]{"012","345","678",'0',new ItemStack(Item.flint,1,0),'1',new ItemStack(Item.flint,1,0),'2',new ItemStack(Item.flint,1,0),'3',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'7',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeAlloySmelter, 1),new Object[]{"012","345","678",'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'3',new ItemStack(Block.furnaceBlastIdle,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(Block.furnaceBlastIdle,1,0),'7',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypePlateFormer, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'6',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'7',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'8',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0)});
        createRecipe(new ItemStack(SignalIndustries.prototypeCrystalCutter, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.diamondCuttingGear,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.diamondCuttingGear,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.basicMachineCore, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.steelPlate,1,0),'1',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'2',new ItemStack(SignalIndustries.steelPlate,1,0),'3',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'4',new ItemStack(SignalIndustries.signalumCrystal,1,0),'5',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'6',new ItemStack(SignalIndustries.steelPlate,1,0),'7',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'8',new ItemStack(SignalIndustries.steelPlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.basicConduit, 4), new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'1',new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1,0),'2',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(SignalIndustries.prototypeConduit,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'7',new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1,0),'8',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.basicFluidConduit, 4), new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'1',new ItemStack(SignalIndustries.steelPlate,1,0),'2',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(SignalIndustries.prototypeFluidConduit,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'7',new ItemStack(SignalIndustries.steelPlate,1,0),'8',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0)});
        createRecipe(new ItemStack(SignalIndustries.evilCatalyst, 1),new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.monsterShard,1,0),'1',new ItemStack(SignalIndustries.monsterShard,1,0),'2',new ItemStack(SignalIndustries.monsterShard,1,0),'3',new ItemStack(SignalIndustries.monsterShard,1,0),'4',new ItemStack(Item.slimeball,1,0),'5',new ItemStack(SignalIndustries.monsterShard,1,0),'6',new ItemStack(SignalIndustries.monsterShard,1,0),'7',new ItemStack(SignalIndustries.monsterShard,1,0),'8',new ItemStack(SignalIndustries.monsterShard,1,0)});
        createRecipe(new ItemStack(SignalIndustries.basicWrathBeacon, 1), new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'1',new ItemStack(SignalIndustries.monsterShard,1,0),'2',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'3',new ItemStack(Item.bone,1,0),'4',new ItemStack(Block.mobspawner,1,0),'5',new ItemStack(Item.bone,1,0),'6',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'7',new ItemStack(SignalIndustries.basicMachineCore,1,0),'8',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0)});
        RecipeRegistry.groups.add(group);
    }

    private void createRecipe(ItemStack output, Object[] inputs){
        ((CraftingManagerAccessor) RecipeHelper.craftingManager).callAddRecipe(output, inputs);
        IRecipe recipe = (IRecipe) RecipeHelper.craftingManager.getRecipeList().get(RecipeHelper.craftingManager.getRecipeList().size()-1);
        RecipeCrafting recipeCrafting = new RecipeCrafting(recipe);
        group.addRecipe(recipeCrafting);

    }

    private void createShapelessRecipe(ItemStack output, Object[] inputs){
        ((CraftingManagerAccessor) RecipeHelper.craftingManager).callAddShapelessRecipe(output, inputs);
        IRecipe recipe = (IRecipe) RecipeHelper.craftingManager.getRecipeList().get(RecipeHelper.craftingManager.getRecipeList().size()-1);
        RecipeCrafting recipeCrafting = new RecipeCrafting(recipe);
        group.addRecipe(recipeCrafting);
    }
}
