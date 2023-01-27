package dev.spimy.qoltweaks.commands.subcommands.commands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.SubCommand;
import dev.spimy.qoltweaks.config.ConfigManager;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Toggle extends SubCommand {

    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public Toggle() {
        super(
            "toggle",
            "Enable or disable a module.",
            true,
            new HashMap<>() {{
                put("<module>", "Module to enable or disable.");
            }}
        );
    }
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessageManager().getConfigMessage("missing-argument", true));
            return true;
        }

        String moduleName = args[0].toLowerCase();
        ConfigManager configManager = plugin.getConfigManagers().get(moduleName);

        if (configManager == null) {
            sender.sendMessage(plugin.getMessageManager().getConfigMessage("invalid-argument", true));
            return true;
        }

        boolean toggle = !configManager.getConfig().getBoolean("enabled");
        configManager.getConfig().set("enabled", toggle);
        configManager.saveConfig();

        String messagePath = toggle ? "module.on-enable" : "module.on-disable";
        sender.sendMessage(
            plugin.getMessageManager().getConfigMessage(messagePath, true)
                .replaceAll("\\{\\bmodule\\b}", moduleName)
        );
        return true;
    }
}
