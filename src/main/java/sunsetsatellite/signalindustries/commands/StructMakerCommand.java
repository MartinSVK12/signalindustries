package sunsetsatellite.signalindustries.commands;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.Command;
import net.minecraft.core.net.command.CommandHandler;
import net.minecraft.core.net.command.CommandSender;
import net.minecraft.core.net.command.PlayerCommandSender;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.VarargsFunction3;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;

public class StructMakerCommand extends Command {

	public static Structure currentStructure;
	public static BlockInstance origin;
	public static Set<BlockInstance> structBlocks = new HashSet<>();
	public static boolean autoAddRemove = false;
	public static boolean ignoreRotation = false;

	public StructMakerCommand(String name, String... alts) {
		super(name, alts);
	}

	@Override
	public boolean execute(CommandHandler commandHandler, CommandSender commandSender, String[] args) {
		World world = Minecraft.getMinecraft(Minecraft.class).theWorld;
		if (commandSender instanceof PlayerCommandSender) {
			EntityPlayer player = commandSender.getPlayer();
			if (args.length > 0) {
				for (Cmd cmd : Cmd.values()) {
					if (cmd.name.equalsIgnoreCase(args[0])) {
						return cmd.method.apply(commandHandler,commandSender,Arrays.copyOfRange(args, 1, args.length));
					}
				}
			}
		}
		return false;
	}

	private static Boolean createStructure(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(Objects.equals(args[0], "")){
			commandSender.sendMessage("Please specify a structure name!");
			return true;
		}
		CompoundTag nbt = new CompoundTag();
		nbt.putCompound("Blocks",new CompoundTag());
		nbt.putCompound("Origin",new CompoundTag());
		nbt.putCompound("Substitutions",new CompoundTag());
		nbt.putCompound("TileEntities",new CompoundTag());
		currentStructure = new Structure(SignalIndustries.MOD_ID, new Class[]{SIBlocks.class}, args[0], nbt , false, false);
		commandSender.sendMessage("Structure '"+args[0]+"' created!");
		structBlocks.clear();
		return true;
	}

	private static Boolean setOrigin(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(currentStructure == null){
			commandSender.sendMessage("No structure! Use /s create <name>!");
			return true;
		}
		Minecraft mc = commandHandler.asClient().minecraft;
		World world = mc.theWorld;
		if (mc.objectMouseOver.hitType == HitResult.HitType.TILE) {
			Vec3i posVec = new Vec3i(mc.objectMouseOver.x, mc.objectMouseOver.y, mc.objectMouseOver.z);
			origin = new BlockInstance(posVec.getBlock(world),posVec, ignoreRotation ? -1 : posVec.getBlockMetadata(world), posVec.getTileEntity(world));
			for (BlockInstance structBlock : structBlocks) {
				structBlock.offset = distanceFromOrigin(structBlock.pos);
			}
			commandSender.sendMessage("Origin set at " + posVec + " with meta "+ origin.meta + " as " + origin.block.getKey() + "!");
		}
		return true;
	}

	private static Boolean addBlockToStructure(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(currentStructure == null){
			commandSender.sendMessage("No structure! Use /s create <name>!");
			return true;
		}
		Minecraft mc = commandHandler.asClient().minecraft;
		if (mc.objectMouseOver.hitType == HitResult.HitType.TILE) {
			Vec3i posVec = new Vec3i(mc.objectMouseOver.x, mc.objectMouseOver.y, mc.objectMouseOver.z);
			internalAddBlock(mc,posVec);
		}

		return true;
	}

	private static Boolean removeBlockFromStructure(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(currentStructure == null){
			commandSender.sendMessage("No structure! Use /s create <name>!");
			return true;
		}
		Minecraft mc = commandHandler.asClient().minecraft;
		if (mc.objectMouseOver.hitType == HitResult.HitType.TILE) {
			Vec3i posVec = new Vec3i(mc.objectMouseOver.x, mc.objectMouseOver.y, mc.objectMouseOver.z);
			internalRemoveBlock(mc,posVec);
		}
		return true;
	}

	public static void internalRemoveBlock(Minecraft mc, Vec3i posVec){
		if(currentStructure == null){
			return;
		}
		World world = mc.theWorld;
		BlockInstance block = new BlockInstance(posVec.getBlock(world), posVec, ignoreRotation ? -1 : posVec.getBlockMetadata(world), posVec.getTileEntity(world));
		if(structBlocks.remove(block)){
			mc.thePlayer.sendMessage("Removed block at " + posVec + " with meta "+ block.meta + " from structure (" + block.block.getKey() + ")!");
		}
	}


	public static void internalAddBlock(Minecraft mc, Vec3i posVec){
		if(currentStructure == null){
			return;
		}
		World world = mc.theWorld;
		BlockInstance block = new BlockInstance(posVec.getBlock(world), posVec, ignoreRotation ? -1 : posVec.getBlockMetadata(world), posVec.getTileEntity(world));
		block.offset = distanceFromOrigin(posVec);
		structBlocks.add(block);
		mc.thePlayer.sendMessage("Added block at " + posVec + " with meta "+ block.meta + " to structure as " + block.block.getKey() + "!");
	}

	private static Boolean toggleAutoAddRemove(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(autoAddRemove){
			autoAddRemove = false;
			commandSender.sendMessage("Disabled automatic adding and removing!");
		} else {
			autoAddRemove = true;
			commandSender.sendMessage("Enabled automatic adding and removing!");
		}
		return true;
	}

	private static Boolean toggleIgnoreRotation(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(ignoreRotation){
			ignoreRotation = false;
			commandSender.sendMessage("Disabled rotation ignoring!");
		} else {
			ignoreRotation = true;
			commandSender.sendMessage("Enabled rotation ignoring!");
		}
		return true;
	}

	private static Boolean clearStructure(CommandHandler commandHandler, CommandSender commandSender, String... args){
		currentStructure = null;
		structBlocks.clear();
		commandSender.sendMessage("Structure data cleared!");
		return true;
	}

	private static Boolean listContents(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(currentStructure == null){
			commandSender.sendMessage("No structure! Use /s create <name>!");
			return true;
		}
		commandSender.sendMessage("Name: "+currentStructure.translateKey);
		commandSender.sendMessage("Blocks: "+structBlocks.size());
		//commandSender.sendMessage("Blocks: "+currentStructure.data.getCompound("Blocks").getValues().size());
		//commandSender.sendMessage("Substitutions: "+currentStructure.data.getCompound("Substitutions").getValues().size());
		//commandSender.sendMessage("Tile Entities: "+currentStructure.data.getCompound("TileEntities").getValues().size());
		return true;
	}

	private static void serializeOrigin(){
		CompoundTag blockNbt = new CompoundTag();
		CompoundTag posNbt = new CompoundTag();
		new Vec3i().writeToNBT(posNbt);
		blockNbt.putCompound("pos", posNbt);

		List<Field> fields = new ArrayList<>(Arrays.asList(SIBlocks.class.getDeclaredFields()));
		fields.removeIf((F)->F.getType() != Block.class);
		for (Field field : fields) {
			Block fieldBlock;
			try {
				fieldBlock = (Block) field.get(null);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if(origin.block == fieldBlock){
				blockNbt.putString("id", SIBlocks.class.getName()+":"+field.getName());
			}
		}

		blockNbt.putBoolean("tile",origin.tile != null);
		blockNbt.putInt("meta",origin.meta);
		currentStructure.data.putCompound("Origin",blockNbt);
	}

	private static Boolean saveStructure(CommandHandler commandHandler, CommandSender commandSender, String... args){
		if(currentStructure == null){
			commandSender.sendMessage("No structure! Use /s create <name>!");
			return true;
		}
		if(Objects.equals(args[0], "")){
			commandSender.sendMessage("Please specify a structure name!");
			return true;
		}
		Minecraft mc = commandHandler.asClient().minecraft;
		currentStructure.data.putCompound("Origin",new CompoundTag());
		currentStructure.data.putCompound("Blocks",new CompoundTag());
		currentStructure.data.putCompound("TileEntities",new CompoundTag());
		serializeOrigin();
		for (BlockInstance block : structBlocks) {
			CompoundTag blockNbt = new CompoundTag();
			CompoundTag posNbt = new CompoundTag();
			block.offset.writeToNBT(posNbt);
			blockNbt.putCompound("pos", posNbt);

			List<Field> fields = new ArrayList<>(Arrays.asList(SIBlocks.class.getDeclaredFields()));
			fields.removeIf((F)->F.getType() != Block.class);
			for (Field field : fields) {
				Block fieldBlock;
				try {
					fieldBlock = (Block) field.get(null);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				if(block.block == fieldBlock){
					blockNbt.putString("id", SIBlocks.class.getName()+":"+field.getName());
				}
			}

			blockNbt.putBoolean("tile",block.tile != null);
			blockNbt.putInt("meta",block.meta);
			int i = currentStructure.data.getCompound("Blocks").getValues().size();
			currentStructure.data.getCompound("Blocks").putCompound(String.valueOf(i),blockNbt);
			if(block.tile != null){
				currentStructure.data.getCompound("TileEntities").putCompound(String.valueOf(i),blockNbt);
			}
		}
		String s = String.format("%s\\%s.nbt", Minecraft.getMinecraft(Minecraft.class).getMinecraftDir(), args[0]);
		File file = new File(s);
		try {
			try (DataOutputStream output = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
				NbtIo.writeCompressed(currentStructure.data, output);
			}
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		return true;
	}

	private static Vec3i distanceFromOrigin(Vec3i vec){
		if(origin == null) return new Vec3i();
		return origin.pos.copy().subtract(vec).multiply(-1);
	}

	private enum Cmd {
		CREATE("create", StructMakerCommand::createStructure),
		ADD("add",StructMakerCommand::addBlockToStructure),
		REMOVE("remove",StructMakerCommand::removeBlockFromStructure),
		CLEAR("clear",StructMakerCommand::clearStructure),
		ORIGIN("origin",StructMakerCommand::setOrigin),
		AUTO("auto",StructMakerCommand::toggleAutoAddRemove),
		IGNORE_ROT("ignore-rot",StructMakerCommand::toggleIgnoreRotation),
		LIST("list",StructMakerCommand::listContents),
		SAVE("save",StructMakerCommand::saveStructure);

		Cmd(String name, VarargsFunction3<CommandHandler,CommandSender,String,Boolean> method) {
			this.name = name;
			this.method = method;
		}

		public final String name;
		public final VarargsFunction3<CommandHandler,CommandSender,String,Boolean> method;
	}

	@Override
	public boolean opRequired(String[] strings) {
		return true;
	}

	@Override
	public void sendCommandSyntax(CommandHandler commandHandler, CommandSender commandSender) {
		if (commandSender instanceof PlayerCommandSender) {
			commandSender.sendMessage("/s create <name>");
			commandSender.sendMessage("/s add");
			commandSender.sendMessage("/s remove");
			commandSender.sendMessage("/s clear");
			commandSender.sendMessage("/s origin");
			commandSender.sendMessage("/s auto");
			commandSender.sendMessage("/s ignore-rot");
			commandSender.sendMessage("/s list");
			commandSender.sendMessage("/s save <name>");
		}
	}
}
