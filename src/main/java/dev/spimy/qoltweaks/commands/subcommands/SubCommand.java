package dev.spimy.qoltweaks.commands.subcommands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    boolean execute(CommandSender sender, String[] args);
    String getDescription();
    String getArgumentsList();
    String getDetailedDescription();
}
