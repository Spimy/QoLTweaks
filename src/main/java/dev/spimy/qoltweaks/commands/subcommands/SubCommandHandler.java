package dev.spimy.qoltweaks.commands.subcommands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.commands.Help;
import dev.spimy.qoltweaks.commands.subcommands.commands.Reload;

import java.util.*;
import java.util.stream.Collectors;

public class SubCommandHandler {

    private final List<SubCommand> subCommands = new ArrayList<>();


    public SubCommandHandler() {
        subCommands.add(new Reload());
        subCommands.add(new Help(this));
    }

    public SubCommand getSubCommand(String command) {
        return subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(command)).findFirst().orElse(null);
    }

    public List<String> getSubCommandNames() {
        return this.subCommands.stream().map(SubCommand::getName).collect(Collectors.toList());
    }

    public ArrayList<String> getCommandList() {
        ArrayList<String> commandList = new ArrayList<>();

        subCommands.forEach(subCommand -> commandList.add(String.format(
            "&6/%s %s %s: &a%s",
            QoLTweaks.getInstance().getClass().getSimpleName().toLowerCase(),
            subCommand.getName(),
            String.join(" ", subCommand.getArguments().keySet()),
            subCommand.getDescription()
        )));

        return commandList;
    }

}
