package dev.spimy.qoltweaks.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.commands.subcommands.SubCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BaseCommand implements TabExecutor {

    private final QoLTweaks plugin = QoLTweaks.getInstance();
    private final SubCommandHandler handler = SubCommandHandler.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            return handler.getSubCommand("help").execute(sender, args);
        }

        SubCommand subCommand = handler.getSubCommand(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(plugin.getMessageManager().getConfigMessage("not-exist", true));
            return true;
        }

        if (subCommand.isRequirePermission()) {
            if (subCommand.isMissingPermission(sender)) {
                sender.sendMessage(plugin.getMessageManager().getConfigMessage("no-permission", true));
                return true;
            }
        }

        return subCommand.execute(
            sender,
            Arrays.copyOfRange(args, 1, args.length)
        );
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], handler.getSubCommandNames(), completions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                StringUtil.copyPartialMatches(args[1], handler.getSubCommandNames(), completions);
            }

            else if (args[0].equalsIgnoreCase("toggle")) {
                StringUtil.copyPartialMatches(args[1], plugin.getConfigManagers().keySet(), completions);
            }

            else {
                for (int i = 0; i < handler.getSubCommandNames().size(); i++) {
                    if (args[0].equalsIgnoreCase(handler.getSubCommandNames().get(i))) {
                        SubCommand subCommand = handler.getSubCommand(handler.getSubCommandNames().get(i));
                        StringUtil.copyPartialMatches(args[1], subCommand.getStrippedArguments(), completions);
                    }
                }
            }
        }

        Collections.sort(completions);
        return completions;
    }
}
