package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.Permissions;
import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Reload implements SubCommand {

    private final QoLTweaks plugin;

    public Reload(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("lang");

        if (!sender.hasPermission(Permissions.RELOAD.getPermissionNode())) {
            sender.sendMessage(
                plugin.formatMessage(
                    String.format(
                        "%s %s",
                        config.getString("orefix"),
                        config.getString("no-permission")
                    )
                )
            );
            return true;
        }

        plugin.reloadConfig();

        sender.sendMessage(
            plugin.formatMessage(
                String.format(
                    "%s %s",
                    config.getString("prefix"),
                    config.getString("reloaded")
                )
            )
        );
        return true;
    }

    @Override
    public String getDescription() {
        return "Reload the plugin's configs.";
    }

    @Override
    public String getArgumentsList() {
        return "";
    }

    @Override
    public String getDetailedDescription() {
        return "";
    }
}
