package dev.spimy.qoltweaks.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public abstract class SubCommand {

    private final String name;
    private final String description;
    private final boolean requirePermission;
    private final String permissionNode;
    private final ArgumentInfo argumentInfo;

    public SubCommand(final String name, final String description, final boolean requirePermission) {
        this.name = name;
        this.description = description;
        this.requirePermission = requirePermission;
        this.permissionNode = String.format("qoltweaks.command.%s", name);
        this.argumentInfo = null;
    }

    public SubCommand(final String name, final String description, final boolean requirePermission, final ArgumentInfo argumentInfo) {
        this.name = name;
        this.description = description;
        this.requirePermission = requirePermission;
        this.permissionNode = String.format("qoltweaks.command.%s", name);
        this.argumentInfo = argumentInfo;
    }

    public abstract void execute(@NotNull final CommandSender sender, @NotNull final String[] args);

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

    public boolean isMissingPermission(final CommandSender sender) {
        return !sender.hasPermission(permissionNode);
    }

    public boolean isRequirePermission() {
        return requirePermission;
    }

}
