package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.ArgumentInfo;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.commands.subcommands.SubCommandHandler;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class Help extends SubCommand {

    public Help() {
        super(
            "help",
            "View list of commands.",
            false,
            new ArgumentInfo(
                "command",
                "View detailed help for the command provided.",
                false
                // List of arguments for this command specifically is set in onTabComplete method in BaseCommand
            )
        );
    }

    @Override
    public void execute(@NotNull final CommandSender sender, final String[] args) {
        final QoLTweaks plugin = QoLTweaks.getInstance();
        final SubCommandHandler handler = SubCommandHandler.getInstance();
        final String commandName = plugin.getClass().getSimpleName().toLowerCase();

        if (args.length == 0) {
            sender.sendMessage(
                plugin.getMessageManager().formatMessage(
                    String.format(
                        "&7-= &6QoLTweaks &7=-\n%s",
                        String.join("\n", getCommandList(commandName, handler))
                    )
                )
            );
            return;
        }

        final SubCommand subCommand = handler.getSubCommand(args[0].toLowerCase());

        if (subCommand == null) {
            sender.sendMessage(plugin.getMessageManager().getConfigMessage("not-exist", true));
            return;
        }

        sender.sendMessage(
            plugin.getMessageManager().formatMessage(
                String.join("\n", new String[]
                    {
                        String.format("&7-= &6Viewing detailed help for '%s' &7=-", subCommand.getName()),
                        formatHelpInfo(commandName, subCommand, true),
                    }
                )
            )
        );
    }

    private ArrayList<String> getCommandList(final String commandName, final SubCommandHandler handler) {
        final ArrayList<String> commandList = new ArrayList<>();
        handler.getSubCommands().forEach(subCommand -> commandList.add(formatHelpInfo(commandName, subCommand, false)));
        return commandList;
    }

    private String formatHelpInfo(final String commandName, final SubCommand subCommand, final boolean detailed) {
        if (subCommand.hasArguments()) {
            final String helpInfo = String.format(
                "&6/%s %s %s: &a%s",
                commandName,
                subCommand.getName(),
                subCommand.getArgumentInfo().infoArg(),
                subCommand.getDescription()
            );
            return (
                detailed ? String.format(
                    "%s\n&6%s: &a%s - %s",
                    helpInfo,
                    subCommand.getArgumentInfo().infoArg(),
                    subCommand.getArgumentInfo().argDescription(),
                    subCommand.getArgumentInfo().required() ? "REQUIRED" : "OPTIONAL"
                ) : helpInfo
            );
        } else {
            return String.format(
                "&6/%s %s: &a%s",
                commandName,
                subCommand.getName(),
                subCommand.getDescription()
            );
        }
    }

}
