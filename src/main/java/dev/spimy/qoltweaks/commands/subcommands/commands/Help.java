package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.commands.subcommands.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Help implements SubCommand {

    private final QoLTweaks plugin;
    private final SubCommandHandler handler;

    public Help(QoLTweaks plugin, SubCommandHandler handler) {
        this.plugin = plugin;
        this.handler = handler;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(
                plugin.formatMessage(
                    String.format(
                        "&7-= &6QoLTweaks &7=-\n%s",
                        String.join("\n", handler.getCommandList())
                    )
                )
            );
            return true;
        }

        String command = args[0];
        SubCommand subCommand = handler.getSubCommand(command);
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("lang");

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

        sender.sendMessage(plugin.formatMessage(String.format(
            "&6/%s %s: &a%s %s\n%s",
            plugin.getClass().getSimpleName().toLowerCase(),
            command,
            subCommand.getDescription(),
            subCommand.getArgumentsList(),
            subCommand.getDetailedDescription()
        )));
        return true;
    }

    @Override
    public String getDescription() {
        return "View this list of commands. You may provide the name of a command to get more detailed help.";
    }

    @Override
    public String getArgumentsList() {
        return "[command]";
    }

    @Override
    public String getDetailedDescription() {
        return String.join("\n", new String[]
            {"&6[command]: &acommand for which more details to show."}
        );
    }

}
