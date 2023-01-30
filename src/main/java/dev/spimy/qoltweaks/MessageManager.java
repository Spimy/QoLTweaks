package dev.spimy.qoltweaks;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class MessageManager {

    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public String formatMessage(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String prefixedMessage(String message) {
        final ConfigurationSection langConfig = plugin.getConfig().getConfigurationSection("lang");
        message = langConfig != null ? langConfig.getString("prefix") + " " + message : message;
        return formatMessage(message);
    }

    public String getConfigMessage(final String path, final boolean prefixed) {
        final ConfigurationSection langConfig = plugin.getConfig().getConfigurationSection("lang");
        final String message = langConfig != null ? langConfig.getString(path) : "language config was not found";
        return prefixed ? prefixedMessage(message) : formatMessage(message);
    }

}
