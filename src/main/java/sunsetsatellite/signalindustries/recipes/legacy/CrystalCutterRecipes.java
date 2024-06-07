package sunsetsatellite.signalindustries.recipes.legacy;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Deprecated
public class CrystalCutterRecipes extends MachineRecipesBase<ArrayList<Object>, ItemStack> {
    private static final CrystalCutterRecipes instance = new CrystalCutterRecipes();

    public static CrystalCutterRecipes getInstance() {
        return instance;
    }

    protected CrystalCutterRecipes() {
        ArrayList<Object> list = new ArrayList<>();
        list.add(new FluidStack((BlockFluid) Block.fluidWaterFlowing,1000));
        list.add(new ItemStack(SIItems.rawSignalumCrystal,8));
        list.add(0);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("saturation",1000);
        nbt.putInt("size",1);
        addRecipe(list,new ItemStack(SIItems.signalumCrystal.id,1,0,nbt));
        //addRecipe(new ArrayList<Object>{new FluidStack((BlockFluid) Block.waterMoving,1000),new ItemStack(mod_SignalIndustries.rawSignalumCrystal,8)},new ItemStack(mod_SignalIndustries.signalumCrystal,1));
    }

    public void addRecipe(ArrayList<Object> list, ItemStack stack) {
        this.recipeList.put(list, stack);
    }

    public ItemStack getResult(ArrayList<Object> list) {
        ItemStack stack = null;
        for(Map.Entry<ArrayList<Object>,ItemStack> entry : recipeList.entrySet()){
            ArrayList<Object> key = entry.getKey();
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
                    } else if(recipeStack instanceof Integer) {
                        boolean eq = recipeStack == outsideStack;
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

    public Map.Entry<ArrayList<Object>, ItemStack> getValidRecipe(ArrayList<Object> list) {
        for(Map.Entry<ArrayList<Object>,ItemStack> entry : recipeList.entrySet()){
            ArrayList<Object> key = entry.getKey();
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
                    } else if(recipeStack instanceof Integer) {
                        boolean eq = recipeStack == outsideStack;
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