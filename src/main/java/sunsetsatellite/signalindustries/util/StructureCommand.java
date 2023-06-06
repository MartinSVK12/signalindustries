package sunsetsatellite.signalindustries.util;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.command.Command;
import net.minecraft.src.command.CommandHandler;
import net.minecraft.src.command.CommandSender;
import net.minecraft.src.command.PlayerCommandSender;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.Objects;

public class StructureCommand extends Command {
    public StructureCommand(String name, String... alts) {
        super(name, alts);
    }

    public Vec3i pos1;
    public Vec3i pos2;

    @Override
    public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] args) {
        if(commandSender instanceof PlayerCommandSender){
            EntityPlayer player = commandSender.getPlayer();
            if(args.length > 0){
                switch (args.length) {
                    case 1:
                        switch (args[0]){
                            case "pos1":
                                pos1 = new Vec3i((int) Math.round(player.posX), (int) Math.round(player.posY-1), (int) Math.round(player.posZ));
                                commandSender.sendMessage(String.format("Position 1 set to %s",pos1));
                                return true;
                            case "pos2":
                                pos2 = new Vec3i((int) Math.round(player.posX), (int) Math.round(player.posY-1), (int) Math.round(player.posZ));
                                commandSender.sendMessage(String.format("Position 2 set to %s",pos2));
                                return true;
                            case "clearpos":
                                pos1 = null;
                                pos2 = null;
                                commandSender.sendMessage("Positions cleared!");
                                return true;
                            case "list":
                                commandSender.sendMessage("List of internal structures:");
                                commandSender.sendMessage(Structure.internalStructures.keySet().toString());
                                return true;
                            case "reload":
                                Structure.reloadInternalStructures();
                                return true;
                        }
                        break;
                    case 3:
                        if(Structure.internalStructures.containsKey(args[1])){
                            Structure struct = Structure.internalStructures.get(args[1]);
                            boolean r = struct.placeStructure(player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ, args[2]);
                            if(r){
                                commandSender.sendMessage(String.format("Structure '%s' placed at %s facing %s!",args[1],new Vec3i((int) player.posX, (int) player.posY, (int) player.posZ),args[2]));
                            }
                            return r;
                        } else {
                            commandSender.sendMessage("Invalid structure!");
                        }
                        break;
                    case 4:
                        switch (args[0]) {
                            case "pos1":
                                pos1 = new Vec3i(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                                commandSender.sendMessage(String.format("Position 1 set to %s", pos1));
                                return true;
                            case "pos2":
                                pos2 = new Vec3i(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                                commandSender.sendMessage(String.format("Position 2 set to %s", pos2));
                                return true;
                            case "save":
                                if(pos1 != null && pos2 != null){
                                    commandSender.sendMessage("Saving...");
                                    Structure structure = Structure.saveStructure(player.worldObj,pos1,pos2,args[1],false,true);
                                    structure.saveToNBT();
                                    commandSender.sendMessage("Saved!");
                                    return true;
                                } else {
                                    commandSender.sendMessage("One or both positions aren't set!");
                                    return false;
                                }
                        }
                    case 9:
                        if(Objects.equals(args[0], "save") && Objects.equals(args[1], "origin")){
                            Vec3i origin = new Vec3i(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                            Vec3i size = new Vec3i(Integer.parseInt(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8]));
                            commandSender.sendMessage(String.format("Saving structure '%s' at origin %s with size %s...",args[2],origin,size));
                            Structure structure = Structure.saveStructureAroundOrigin(player.worldObj,origin,size,args[2],false,true);
                            if(structure != null){
                                structure.saveToNBT();
                                commandSender.sendMessage("Saved!");
                            } else {
                                commandSender.sendMessage("Couldn't save structure!");
                            }
                            return true;

                        }
                    case 6:
                        if(Structure.internalStructures.containsKey(args[1])){
                            Structure struct = Structure.internalStructures.get(args[1]);
                            try {
                                boolean r = struct.placeStructure(player.worldObj, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), args[5]);
                                if(r){
                                    commandSender.sendMessage(String.format("Structure '%s' placed at %s facing %s!",args[1],new Vec3i(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])), args[5]));
                                }
                                return r;
                            } catch (NumberFormatException e){
                                commandSender.sendMessage("Invalid coordinates provided!");
                                return true;
                            }
                        } else {
                            commandSender.sendMessage("Invalid structure!");
                        }
                    default:
                        return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean opRequired(String[] args) {
        return true;
    }

    @Override
    public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
        if (commandSender instanceof PlayerCommandSender) {
            commandSender.sendMessage("/structure place <name> (r)/(<x> <y> <z> <r>)");
            commandSender.sendMessage("/structure pos1/pos2 (<x> <y> <z>)");
            commandSender.sendMessage("/structure clearpos");
            commandSender.sendMessage("/structure list");
            commandSender.sendMessage("/structure reload");
            commandSender.sendMessage("/structure save <name>");
            commandSender.sendMessage("/structure save origin <name> <originX> <originY> <originZ> <sizeX> <sizeY> <sizeZ>");
        }
    }
}
