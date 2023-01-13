package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.commands.subcommands.SubCommandHandler;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Help extends SubCommand {

    private final QoLTweaks plugin = QoLTweaks.getInstance();
    private final SubCommandHandler handler;

    public Help(SubCommandHandler handler) {
        super(
            "help",
            "View list of commands.",
            new HashMap<>() {{
                put("[command]", "View detailed help for the command provided.");
            }}
        );
        this.handler = handler;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(
                plugin.getMessageManager().formatMessage(
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

        if (subCommand == null) {
            sender.sendMessage(plugin.getMessageManager().getConfigMessage("not-exist", true));
            return true;
        }

        sender.sendMessage(
            plugin.getMessageManager().formatMessage(
                String.join("\n", new String[]
                    {
                        String.format("&7-= &6Viewing detailed help for '%s' &7=-", command),
                        String.format(
                            "&6/%s %s %s: &a%s\n%s",
                            plugin.getClass().getSimpleName().toLowerCase(),
                            command,
                            String.join(" ", subCommand.getArguments().keySet()),
                            subCommand.getDescription(),
                            String.join(
                                "\n",
                                formatArguments(subCommand.getArguments())
                            )
                        )
                    }
                )
            )
        );
        return true;
    }

    private List<String> formatArguments(HashMap<String, String> arguments) {
        final List<String> formattedArguments = new ArrayList<>();

        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            formattedArguments.add(String.format("&6%s: &a%s", entry.getKey(), entry.getValue()));
        }

        return formattedArguments;
    }

}
