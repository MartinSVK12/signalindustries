package sunsetsatellite.signalindustries.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeFIleLoader {

    private enum RecipeType{
        SHAPED, SHAPELESS
    }

    private static class Recipe {
        RecipeType type;
        int size;
        ItemStack output;
        List<Character> pattern = new ArrayList<>();
        Map<Character, ItemStack> inputs = new HashMap<>();
    }

    public static void load(String path, Map<String,String> shortcuts) {
        shortcuts.put("Block","net.minecraft.core.block.Block");
        shortcuts.put("Item","net.minecraft.core.item.Item");
        int totalLineCount = 0;
        try (InputStream is = RecipeFIleLoader.class.getResourceAsStream(path)) {
            if (is == null) throw new NullPointerException("Stream is null!");
            try (InputStreamReader isr = new InputStreamReader(is); BufferedReader reader = new BufferedReader(isr)) {
                List<String> list = reader.lines().collect(Collectors.toList());
                List<Recipe> recipes = new ArrayList<>();
                Recipe currentRecipe = new Recipe();
                int lineCount = 0;
                for (String line : list) {
                    if(line.startsWith("//")){
                        totalLineCount++;
                        continue;
                    }
                    if(lineCount == 0){
                        if(line.split(" ").length != 2){
                            System.err.println("Error at line "+totalLineCount);
                            throw new IllegalStateException("Malformed recipe type definition!");
                        }
                        currentRecipe = new Recipe();
                        if(line.toLowerCase().contains("shaped")){
                            currentRecipe.size = Integer.parseInt(line.split(" ")[1]);
                            currentRecipe.type = RecipeType.SHAPED;
                            lineCount++;
                        } else if(line.toLowerCase().contains("shapeless")){
                            currentRecipe.size = Integer.parseInt(line.split(" ")[1]);
                            currentRecipe.type = RecipeType.SHAPELESS;
                            lineCount++;
                        } else {
                            System.err.println("Error at line "+totalLineCount);
                            throw new IllegalStateException("Invalid recipe type!");
                        }
                        if(currentRecipe.size > 3 || currentRecipe.size < 2){
                            System.err.println("Error at line "+totalLineCount);
                            throw new IllegalStateException("Recipe grid size has to be 2 or 3!");
                        }
                    } else if (lineCount == 1) {
                        if(!line.contains(":") && line.split(" ").length != 3){
                            System.err.println("Error at line "+totalLineCount);
                            throw new IllegalStateException("Malformed item definition!");
                        }
                        currentRecipe.output = getItemStackFromString(line,shortcuts);
                        lineCount++;
                    } else if ( (currentRecipe.size == 3 && (lineCount > 1 && lineCount < 5)) || (currentRecipe.size == 2 && (lineCount > 1 && lineCount < 4)) ) {
                        if(line.length() != 3 && line.length() != 2){
                            System.err.println("Error at line "+totalLineCount);
                            throw new IllegalStateException("Pattern lines have to be 2 or 3 characters long!");
                        }
                        for (char c : line.toCharArray()) {
                            currentRecipe.pattern.add(c);
                        }
                        lineCount++;
                    } else {
                        if(line.isEmpty()){
                            lineCount = 0;
                            recipes.add(currentRecipe);
                            currentRecipe = null;
                        } else {
                            if(!line.contains(":") && !line.contains("=") && line.split(" ").length != 3){
                                System.err.println("Error at line "+totalLineCount);
                                throw new IllegalStateException("Malformed item definition!");
                            }
                            if(currentRecipe.pattern.stream().allMatch((C)-> C == '_')){
                                System.err.println("Error at line "+totalLineCount);
                                throw new IllegalStateException("Recipe pattern cannot be completely blank!");
                            }
                            String[] split = line.split("=");
                            char c = split[0].charAt(0);
                            if(c == '_'){
                                System.err.println("Error at line "+totalLineCount);
                                throw new IllegalStateException("'_' can only be used as an empty space in patterns!");
                            }
                            ItemStack stack = getItemStackFromString(split[1],shortcuts);
                            currentRecipe.inputs.put(c,stack);
                        }
                    }
                    totalLineCount++;
                }
                if(currentRecipe != null){
                    recipes.add(currentRecipe);
                }
                System.out.println("Parsed "+recipes.size()+" recipes.");
                for (Recipe recipe : recipes) {
                    List<Object> objs = getRecipePattern(recipe);
                    for (Map.Entry<Character, ItemStack> entry : recipe.inputs.entrySet()) {
                        objs.add(entry.getKey());
                        objs.add(entry.getValue());
                    }
                    RecipeHelper.Crafting.createRecipe(recipe.output,objs.toArray());
                }

            } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Error at line "+totalLineCount);
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Object> getRecipePattern(Recipe recipe) {
        int i = 0;
        StringBuilder s = new StringBuilder();
        List<String> pattern = new ArrayList<>();
        for (Character c : recipe.pattern) {
            s.append(c);
            i++;
            if((recipe.size == 3 && i > 2) || (recipe.size == 2 && i > 1)){
                pattern.add(s.toString());
                s = new StringBuilder();
                i = 0;
            }
        }
        return new ArrayList<>(pattern);
    }

    private static ItemStack getItemStackFromString(String s, Map<String,String> shortcuts) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String[] data = s.split(" ");
        String name = data[0];
        int count = Integer.parseInt(data[1]);
        int meta = Integer.parseInt(data[2]);
        String className = name.split(":")[0];
        String fieldName = name.split(":")[1];
        if(shortcuts.containsKey(className)){
            className = shortcuts.get(className);
        }
        Class<?> clazz = Class.forName(className);
        Field field = clazz.getField(fieldName);
        Object obj = field.get(null);
        int id = 0;
        if(obj instanceof Item){
            id = ((Item) obj).id;
        } else if (obj instanceof Block) {
            id = ((Block) obj).id;
        }
        return new ItemStack(id,count,meta);
    }
}
