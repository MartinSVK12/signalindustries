package sunsetsatellite.signalindustries.util;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.command.*;

import java.util.Objects;

public class NBTEditCommand extends Command {
    public NBTEditCommand() {
        super("nbtedit", "nbt");
    }

    public static NBTTagCompound copy;

    @Override
    public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] args) {
        if(commandSender instanceof PlayerCommandSender){
            if(Objects.equals(args[0], "hand")){
                if(Objects.equals(args[1],"copy")){
                    if(commandSender.getPlayer().inventory.getCurrentItem() != null){
                        copy = commandSender.getPlayer().inventory.getCurrentItem().tag;
                        commandSender.sendMessage("Copied!");
                        return true;
                    }
                }
                if(Objects.equals(args[1],"paste")){
                    if(copy == null){
                        throw new CommandError("Copy some data first!");
                    }
                    if(commandSender.getPlayer().inventory.getCurrentItem() != null){
                        commandSender.getPlayer().inventory.getCurrentItem().tag = copy;
                        commandSender.sendMessage("Pasted!");
                        return true;
                    }
                }
                if(Objects.equals(args[1],"set")){
                    if(Objects.equals(args[2],"integer")){
                        ItemStack stack = commandSender.getPlayer().inventory.getCurrentItem();
                        int actualValue = Integer.parseInt(args[4]);
                        stack.tag.setInteger(args[3],actualValue);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean opRequired(String[] strings) {
        return true;
    }

    @Override
    public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
        if (commandSender instanceof PlayerCommandSender) {
            commandSender.sendMessage("/nbt hand copy/paste/set integer");
        }
    }
}
