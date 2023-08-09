package sunsetsatellite.signalindustries.recipes;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InfuserRecipes extends MachineRecipesBase<ArrayList<Object>, ItemStack> {
    public static final InfuserRecipes instance = new InfuserRecipes();

    private InfuserRecipes() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(null);
        list.add(new ItemStack(SignalIndustries.crystalAlloyIngot,1));
        list.add(new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,4));
        addRecipe(list,new ItemStack(SignalIndustries.saturatedSignalumAlloyIngot.id,1,0));
        list = new ArrayList<>();
        list.add(new FluidStack((BlockFluid) Block.fluidLavaFlowing,1000));
        list.add(new ItemStack(Block.obsidian,2,0));
        list.add(new ItemStack(SignalIndustries.netherCoalDust,1,0));
        addRecipe(list,new ItemStack(SignalIndustries.glowingObsidian,1,0));

    }

    public void addRecipe(ArrayList<Object> list, ItemStack stack) {
        this.recipeList.put(list, stack);
    }

    public ItemStack getResult(ArrayList<Object> raw_list) {
        ItemStack stack = null;
        for(Map.Entry<ArrayList<Object>,ItemStack> entry : recipeList.entrySet()){
            ArrayList<Object> raw_key = entry.getKey();
            ArrayList<Object> key = (ArrayList<Object>) raw_key.clone();
            key.removeIf(Objects::isNull);
            ArrayList<Object> list = (ArrayList<Object>) raw_list.clone();
            list.removeIf(Objects::isNull);
            if(list.size() == key.size()){
                int i = 0;
                for(Object recipeStack : key){
                    Object outsideStack = list.get(i);
                    if(recipeStack instanceof ItemStack && outsideStack instanceof ItemStack){
                        boolean eq = ((ItemStack) recipeStack).itemID == ((ItemStack) outsideStack).itemID && ((ItemStack) recipeStack).stackSize <= ((ItemStack) outsideStack).stackSize && ((ItemStack) recipeStack).getMetadata() == ((ItemStack) outsideStack).getMetadata();
                        if(!eq){
                            break;
                        }
                    }
                    else if(recipeStack instanceof FluidStack && outsideStack instanceof FluidStack){
                        boolean eq = ((FluidStack) recipeStack).liquid == ((FluidStack) outsideStack).liquid && ((FluidStack) recipeStack).amount <= ((FluidStack) outsideStack).amount;
                        if(!eq){
                            break;
                        }
                    } else {
                        break;
                    }
                    i++;
                }
                if(i == key.size()){
                    stack = entry.getValue();
                }

            }
        }

        return stack == null ? null : stack.copy();
    }

    public Map.Entry<ArrayList<Object>, ItemStack> getValidRecipe(ArrayList<Object> raw_list) {
        for(Map.Entry<ArrayList<Object>,ItemStack> entry : recipeList.entrySet()){
            ArrayList<Object> raw_key = entry.getKey();
            ArrayList<Object> key = (ArrayList<Object>) raw_key.clone();
            key.removeIf(Objects::isNull);
            ArrayList<Object> list = (ArrayList<Object>) raw_list.clone();
            list.removeIf(Objects::isNull);
            if(list.size() == key.size()){
                int i = 0;
                for(Object recipeStack : key){
                    Object outsideStack = list.get(i);
                    if(recipeStack instanceof ItemStack && outsideStack instanceof ItemStack){
                        boolean eq = ((ItemStack) recipeStack).itemID == ((ItemStack) outsideStack).itemID && ((ItemStack) recipeStack).stackSize <= ((ItemStack) outsideStack).stackSize && ((ItemStack) recipeStack).getMetadata() == ((ItemStack) outsideStack).getMetadata();
                        if(!eq){
                            break;
                        }
                    }
                    else if(recipeStack instanceof FluidStack && outsideStack instanceof FluidStack){
                        boolean eq = ((FluidStack) recipeStack).liquid == ((FluidStack) outsideStack).liquid && ((FluidStack) recipeStack).amount <= ((FluidStack) outsideStack).amount;
                        if(!eq){
                            break;
                        }
                    } else {
                        break;
                    }
                    i++;
                }
                if(i == key.size()){
                    return entry;
                }

            }
        }

        return null;
    }


    public HashMap<ArrayList<Object>, ItemStack> getRecipeList() {
        return this.recipeList;
    }
}