package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.ArgumentInfo;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.config.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Toggle extends SubCommand {

    public Toggle() {
        super(
            "toggle",
            "Enable or disable a module.",
            true,
            new ArgumentInfo(
                "module",
                "Module to enable or disable.",
                true,
                QoLTweaks.getInstance().getConfigManagers().keySet().stream().toList()
            )
        );
    }
    @Override
    public void execute(@NotNull final CommandSender sender, final String[] args) {
        final QoLTweaks plugin = QoLTweaks.getInstance();

        if (args.length == 0) {
            sender.sendMessage(plugin.getMessageManager().getConfigMessage("missing-argument", true));
            return;
        }

        final String moduleName = args[0].toLowerCase();
        final ConfigManager configManager = plugin.getConfigManagers().get(moduleName);

        if (configManager == null) {
            sender.sendMessage(plugin.getMessageManager().getConfigMessage("invalid-argument", true));
            return;
        }

        final boolean toggle = !configManager.getConfig().getBoolean("enabled");
        configManager.getConfig().set("enabled", toggle);
        configManager.saveConfig();

        final String messagePath = toggle ? "module.on-enable" : "module.on-disable";
        sender.sendMessage(
            plugin.getMessageManager().getConfigMessage(messagePath, true)
                .replaceAll("\\{\\bmodule\\b}", moduleName)
        );
    }
}
