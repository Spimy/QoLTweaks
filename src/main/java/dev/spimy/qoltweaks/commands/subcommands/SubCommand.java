package dev.spimy.qoltweaks.commands.subcommands;

import org.bukkit.command.CommandSender;


public abstract class SubCommand {

    private final String name;
    private final String description;
    private final boolean requirePermission;
    private final String permissionNode;
    private final ArgumentInfo argumentInfo;

    public SubCommand(String name, String description, boolean requirePermission) {
        this.name = name;
        this.description = description;
        this.requirePermission = requirePermission;
        this.permissionNode = String.format("qoltweaks.command.%s", name);
        this.argumentInfo = null;
    }

    public SubCommand(String name, String description, boolean requirePermission, ArgumentInfo argumentInfo) {
        this.name = name;
        this.description = description;
        this.requirePermission = requirePermission;
        this.permissionNode = String.format("qoltweaks.command.%s", name);
        this.argumentInfo = argumentInfo;
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasArguments() {
        return argumentInfo != null;
    }

    public ArgumentInfo getArgumentInfo() {
        return argumentInfo;
    }

    public boolean isMissingPermission(CommandSender sender) {
        return !sender.hasPermission(permissionNode);
    }

    public boolean isRequirePermission() {
        return requirePermission;
    }

}
