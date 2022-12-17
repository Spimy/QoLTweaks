package dev.spimy.qoltweaks;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigManager {

    QoLTweaks plugin;

    public ConfigManager(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    public ConfigurationSection getModuleConfig(Modules module) {
        return plugin.getConfig().getConfigurationSection(
            String.format("modules.%s", module.name().toLowerCase())
        );
    }

}
