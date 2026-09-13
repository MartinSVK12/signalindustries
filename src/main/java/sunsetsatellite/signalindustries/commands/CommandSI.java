package sunsetsatellite.signalindustries.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import sunsetsatellite.signalindustries.SIConfig;

public class CommandSI implements CommandManager.CommandRegistry {
	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("si");
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("info").executes(ctx -> {
			ctx.getSource().sendMessage(String.format("%s is %s", "signaliteGeodeChance", SIConfig.signaliteGeodeChance));
			ctx.getSource().sendMessage(String.format("%s is %s", "ironMeteorChance", SIConfig.ironMeteorChance));
			ctx.getSource().sendMessage(String.format("%s is %s", "signaliteMeteorChance", SIConfig.signaliteMeteorChance));
			ctx.getSource().sendMessage(String.format("%s is %s", "dilithiumMeteorChance", SIConfig.dilithiumMeteorChance));
			ctx.getSource().sendMessage(String.format("%s is %s", "obeliskChance", SIConfig.obeliskChance));
			ctx.getSource().sendMessage(String.format("%s is %s", "suitDamageCostMultiplier", SIConfig.suitDamageCostMultiplier));
			ctx.getSource().sendMessage(String.format("%s is %s", "wingsFlightCost", SIConfig.wingsFlightCost));
			return Command.SINGLE_SUCCESS;
		}));
		dispatcher.register(builder);
	}
}
