package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Reload extends SubCommand {

    public Reload() {
        super("reload", "Reload the plugin's configurations.", true);
    }

    @Override
    public void execute(@NotNull final CommandSender sender, final String[] args) {
        final QoLTweaks plugin = QoLTweaks.getInstance();

        plugin.reloadConfig();
        plugin.getConfigManagers().values().forEach(ConfigManager::reloadConfig);

        sender.sendMessage(plugin.getMessageManager().getConfigMessage("reloaded", true));
    }

}
