package dev.spimy.qoltweaks.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.commands.subcommands.SubCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BaseCommand implements CommandExecutor {

    private final QoLTweaks plugin;

    public BaseCommand(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, @NotNull String label, String[] args) {
        SubCommandHandler handler = new SubCommandHandler(plugin);

        if (args.length == 0) {
            return handler.getSubCommand("help").execute(sender, args);
        }

        ConfigurationSection config = plugin.getConfig().getConfigurationSection("lang");

        SubCommand subCommand = handler.getSubCommand(args[0]);
        if (subCommand == null) {
            sender.sendMessage(
                plugin.formatMessage(
                    String.format(
                        "%s %s",
                        config.getString("prefix"),
                        config.getString("not-exist")
                    )
                )
            );
            return true;
        }

        return subCommand.execute(
            sender,
            Arrays.copyOfRange(args, 1, args.length)
        );
    }

}
