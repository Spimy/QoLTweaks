package dev.spimy.qoltweaks.commands.subcommands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SubCommand {

    private final String name;
    private final String description;
    private final String permissionNode;
    private HashMap<String, String> arguments = new HashMap<>();

    public SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
        permissionNode = String.format("qoltweaks.command.%s", name);
    }

    public SubCommand(String name, String description, HashMap<String, String> arguments) {
        this.name = name;
        this.description = description;
        permissionNode = String.format("qoltweaks.command.%s", name);
        this.arguments = arguments;
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getArguments() {
        return arguments;
    }

    public List<String> getStrippedArguments() {
        final List<String> strippedArguments = new ArrayList<>();

        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            strippedArguments.add(entry.getKey().replaceAll("[\\[|\\]|<>]", ""));
        }

        return strippedArguments;
    }

    public boolean isMissingPermission(CommandSender sender) {
        return !sender.hasPermission(permissionNode);
    }

}
