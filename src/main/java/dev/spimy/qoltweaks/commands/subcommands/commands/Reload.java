package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.config.ConfigManager;
import org.bukkit.command.CommandSender;

public class Reload extends SubCommand {

    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public Reload() {
        super("reload", "Reload the plugin's configurations.", true);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        plugin.getConfigManagers().values().forEach(ConfigManager::reloadConfig);

        sender.sendMessage(plugin.getMessageManager().getConfigMessage("reloaded", true));
        return true;
    }

}
