package sunsetsatellite.signalindustries.commands;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.legacy.CraftingManager;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeRegistry;
import net.minecraft.core.data.registry.recipe.entry.*;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.vintagequesting.VintageQuestingSIPlugin;
import sunsetsatellite.signalindustries.SIRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraft.core.data.registry.Registries.*;

public class RecipeReloadCommand extends Command {
    public RecipeReloadCommand(String name, String... alts) {
        super(name, alts);
    }

    @Override
    public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] strings) {
        if(commandHandler.isServer()){
            commandSender.sendMessage("This command can only be used in singleplayer!");
            return true;
        }
        if(strings.length == 1 && Objects.equals(strings[0], "reload")){
            Registries.RECIPES = new RecipeRegistry();
            List<List<ItemStack>> list = new ArrayList<>();
            List<Class<? extends RecipeEntryBase<?,?,?>>> list2 = new ArrayList<>();
            RECIPE_TYPES.forEach(list2::add);
            ITEM_GROUPS.forEach(list::add);
            list.forEach((L)->{
                String key = ITEM_GROUPS.getKey(L);
                if(key != null) ITEM_GROUPS.unregister(key);
            });
            list2.forEach((C)->{
                String key = RECIPE_TYPES.getKey(C);
                RECIPE_TYPES.unregister(key);
            });
            readdVanillaRegistryItems();
            CraftingManager.getInstance().reset();
            CraftingManager.getInstance().init();
            DataLoader.loadRecipesFromFile("/recipes/blast_furnace.json");
            DataLoader.loadRecipesFromFile("/recipes/furnace.json");
            DataLoader.loadRecipesFromFile("/recipes/trommel.json");
            DataLoader.loadRecipesFromFile("/recipes/workbench.json");
            SIRecipes siRecipes = new SIRecipes();
            siRecipes.resetGroups();
            siRecipes.registerNamespaces();
            siRecipes.load();
            DataLoader.loadDataPacks(Minecraft.getMinecraft(this));
            RECIPES_LOCAL_COPY = RECIPES;
            int recipes = RECIPES.getAllRecipes().size();
            int groups = RECIPES.getAllGroups().size();
            int namespaces = RECIPES.size();
            int itemGroups = ITEM_GROUPS.size();
            commandSender.sendMessage(String.format("%d item groups.",itemGroups));
            commandSender.sendMessage(String.format("%d recipes in %d groups in %d namespaces.",recipes,groups,namespaces));
            commandSender.sendMessage("Recipes reloaded!");
            if (FabricLoaderImpl.INSTANCE.isModLoaded("vintagequesting")) {
                if (SignalIndustries.config.getBoolean("Other.enableQuests")) {
                    new VintageQuestingSIPlugin().reset();
                    commandSender.sendMessage("Quests reloaded!");
                }
            }
            return true;
        }
        return false;
    }

    private void readdVanillaRegistryItems(){
        RECIPE_TYPES.register("minecraft:crafting/shaped", RecipeEntryCraftingShaped.class);
        RECIPE_TYPES.register("minecraft:crafting/shapeless", RecipeEntryCraftingShapeless.class);
        RECIPE_TYPES.register("minecraft:crafting/label", RecipeEntryLabel.class);
        RECIPE_TYPES.register("minecraft:crafting/label_dye", RecipeEntryLabelDye.class);
        RECIPE_TYPES.register("minecraft:crafting/scrap", RecipeEntryScrap.class);
        RECIPE_TYPES.register("minecraft:crafting/repairable", RecipeEntryRepairable.class);
        RECIPE_TYPES.register("minecraft:crafting/repairable_stackable", RecipeEntryRepairableStackable.class);
        RECIPE_TYPES.register("minecraft:crafting/uses_tool", RecipeEntryCraftingWithTool.class);
        RECIPE_TYPES.register("minecraft:smelting", RecipeEntryFurnace.class);
        RECIPE_TYPES.register("minecraft:smelting/blast", RecipeEntryBlastFurnace.class);
        RECIPE_TYPES.register("minecraft:trommeling", RecipeEntryTrommel.class);

        ITEM_GROUPS.register("minecraft:stones",stackListOf(Block.stone, Block.basalt, Block.limestone, Block.granite));
        ITEM_GROUPS.register("minecraft:cobblestones",stackListOf(Block.cobbleStone, Block.cobbleBasalt, Block.cobbleLimestone, Block.cobbleGranite, Block.cobblePermafrost));
        ITEM_GROUPS.register("minecraft:planks",stackListOf(Block.planksOak, new ItemStack(Block.planksOakPainted, 1, 0), new ItemStack(Block.planksOakPainted, 1, 1), new ItemStack(Block.planksOakPainted, 1, 2), new ItemStack(Block.planksOakPainted, 1, 3), new ItemStack(Block.planksOakPainted, 1, 4), new ItemStack(Block.planksOakPainted, 1, 5), new ItemStack(Block.planksOakPainted, 1, 6), new ItemStack(Block.planksOakPainted, 1, 7), new ItemStack(Block.planksOakPainted, 1, 8), new ItemStack(Block.planksOakPainted, 1, 9), new ItemStack(Block.planksOakPainted, 1, 10), new ItemStack(Block.planksOakPainted, 1, 11), new ItemStack(Block.planksOakPainted, 1, 12), new ItemStack(Block.planksOakPainted, 1, 13), new ItemStack(Block.planksOakPainted, 1, 14), new ItemStack(Block.planksOakPainted, 1, 15)));
        ITEM_GROUPS.register("minecraft:grasses",stackListOf(Block.grass, Block.grassRetro));
        ITEM_GROUPS.register("minecraft:dirt",stackListOf(Block.dirt, Block.dirtScorched));
        ITEM_GROUPS.register("minecraft:trommel_dirt",stackListOf(Block.dirt, Block.dirtScorched, Block.grass, Block.grassRetro, Block.grassScorched, Block.pathDirt, Block.farmlandDirt));
        ITEM_GROUPS.register("minecraft:moss_stones",stackListOf(Block.mossStone, Block.mossBasalt, Block.mossLimestone, Block.mossGranite));
        ITEM_GROUPS.register("minecraft:logs",stackListOf(Block.logOak, Block.logPine, Block.logBirch, Block.logCherry, Block.logEucalyptus, Block.logOakMossy));
        ITEM_GROUPS.register("minecraft:leaves",stackListOf(Block.leavesOak, Block.leavesOakRetro, Block.leavesPine, Block.leavesBirch, Block.leavesCherry, Block.leavesEucalyptus, Block.leavesShrub));
        ITEM_GROUPS.register("minecraft:coal_ores",stackListOf(Block.oreCoalStone, Block.oreCoalBasalt, Block.oreCoalLimestone, Block.oreCoalGranite));
        ITEM_GROUPS.register("minecraft:iron_ores",stackListOf(Block.oreIronStone, Block.oreIronBasalt, Block.oreIronLimestone, Block.oreIronGranite));
        ITEM_GROUPS.register("minecraft:gold_ores",stackListOf(Block.oreGoldStone, Block.oreGoldBasalt, Block.oreGoldLimestone, Block.oreGoldGranite));
        ITEM_GROUPS.register("minecraft:lapis_ores",stackListOf(Block.oreLapisStone, Block.oreLapisBasalt, Block.oreLapisLimestone, Block.oreLapisGranite));
        ITEM_GROUPS.register("minecraft:redstone_ores",stackListOf(Block.oreRedstoneStone, Block.oreRedstoneBasalt, Block.oreRedstoneLimestone, Block.oreRedstoneGranite, Block.oreRedstoneGlowingStone, Block.oreRedstoneGlowingBasalt, Block.oreRedstoneGlowingLimestone, Block.oreRedstoneGlowingGranite));
        ITEM_GROUPS.register("minecraft:diamond_ores",stackListOf(Block.oreDiamondStone, Block.oreDiamondBasalt, Block.oreDiamondLimestone, Block.oreDiamondGranite));
        ITEM_GROUPS.register("minecraft:nethercoal_ores", stackListOf(Block.oreNethercoalNetherrack));
        ITEM_GROUPS.register("minecraft:chests",stackListOf(Block.chestPlanksOak, new ItemStack(Block.chestPlanksOakPainted, 1, 0x00), new ItemStack(Block.chestPlanksOakPainted, 1, 0x10), new ItemStack(Block.chestPlanksOakPainted, 1, 0x20), new ItemStack(Block.chestPlanksOakPainted, 1, 0x30), new ItemStack(Block.chestPlanksOakPainted, 1, 0x40), new ItemStack(Block.chestPlanksOakPainted, 1, 0x50), new ItemStack(Block.chestPlanksOakPainted, 1, 0x60), new ItemStack(Block.chestPlanksOakPainted, 1, 0x70), new ItemStack(Block.chestPlanksOakPainted, 1, 0x80), new ItemStack(Block.chestPlanksOakPainted, 1, 0x90), new ItemStack(Block.chestPlanksOakPainted, 1, 0xA0), new ItemStack(Block.chestPlanksOakPainted, 1, 0xB0), new ItemStack(Block.chestPlanksOakPainted, 1, 0xC0), new ItemStack(Block.chestPlanksOakPainted, 1, 0xD0), new ItemStack(Block.chestPlanksOakPainted, 1, 0xE0), new ItemStack(Block.chestPlanksOakPainted, 1, 0xF0)));
        ITEM_GROUPS.register("minecraft:wools", stackListOf(new ItemStack(Block.wool, 1, 0), new ItemStack(Block.wool, 1, 1), new ItemStack(Block.wool, 1, 2), new ItemStack(Block.wool, 1, 3), new ItemStack(Block.wool, 1, 4), new ItemStack(Block.wool, 1, 5), new ItemStack(Block.wool, 1, 6), new ItemStack(Block.wool, 1, 7), new ItemStack(Block.wool, 1, 8), new ItemStack(Block.wool, 1, 9), new ItemStack(Block.wool, 1, 10), new ItemStack(Block.wool, 1, 11), new ItemStack(Block.wool, 1, 12), new ItemStack(Block.wool, 1, 13), new ItemStack(Block.wool, 1, 14), new ItemStack(Block.wool, 1, 15)));
        ITEM_GROUPS.register("minecraft:lamps", stackListOf(new ItemStack(Block.lampIdle, 1, 0), new ItemStack(Block.lampIdle, 1, 1), new ItemStack(Block.lampIdle, 1, 2), new ItemStack(Block.lampIdle, 1, 3), new ItemStack(Block.lampIdle, 1, 4), new ItemStack(Block.lampIdle, 1, 5), new ItemStack(Block.lampIdle, 1, 6), new ItemStack(Block.lampIdle, 1, 7), new ItemStack(Block.lampIdle, 1, 8), new ItemStack(Block.lampIdle, 1, 9), new ItemStack(Block.lampIdle, 1, 10), new ItemStack(Block.lampIdle, 1, 11), new ItemStack(Block.lampIdle, 1, 12), new ItemStack(Block.lampIdle, 1, 13), new ItemStack(Block.lampIdle, 1, 14), new ItemStack(Block.lampIdle, 1, 15)));

    }

    @Override
    public boolean opRequired(String[] strings) {
        return true;
    }

    @Override
    public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
        commandSender.sendMessage("/recipes reload");
    }
}
