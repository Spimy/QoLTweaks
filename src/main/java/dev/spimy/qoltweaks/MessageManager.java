package dev.spimy.qoltweaks;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class MessageManager {

    private final ConfigurationSection langConfig = QoLTweaks.getInstance().getConfig().getConfigurationSection("lang");

    public String formatMessage(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String prefixedMessage(String message) {
        message = langConfig != null ? langConfig.getString("prefix") + " " + message : message;
        return formatMessage(message);
    }

    public String getConfigMessage(final String path, final boolean prefixed) {
        final String message = langConfig != null ? langConfig.getString(path) : "language config was not found";
        return prefixed ? prefixedMessage(message) : formatMessage(message);
    }

}
