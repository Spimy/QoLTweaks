package dev.spimy.qoltweaks;

import dev.spimy.qoltweaks.commands.CommandLoader;
import dev.spimy.qoltweaks.modules.ModuleLoader;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class QoLTweaks extends JavaPlugin {

    public ConfigManager configManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        configManager = new ConfigManager(this);
        new CommandLoader(this);
        new ModuleLoader(this);

        getServer().getConsoleSender().sendMessage(
            formatMessage("&aQoLTweaks enabled.")
        );
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(
            formatMessage("&cQoLTweaks disabled.")
        );
    }

    public String formatMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
