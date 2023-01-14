package dev.spimy.qoltweaks;

import dev.spimy.qoltweaks.commands.CommandLoader;
import dev.spimy.qoltweaks.config.ConfigManager;
import dev.spimy.qoltweaks.modules.ModuleLoader;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class QoLTweaks extends JavaPlugin {

    private static QoLTweaks plugin;

    private MessageManager messageManager;
    private final HashMap<String, ConfigManager> configManagers = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        messageManager = new MessageManager();

        new CommandLoader();
        new ModuleLoader();

        getLogger().info("QoLTweaks enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("QoLTweaks disabled.");
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public HashMap<String, ConfigManager> getConfigManagers() {
        return configManagers;
    }

    public static QoLTweaks getInstance() {
        return plugin;
    }

    public NamespacedKey getKey(String key) {
        return new NamespacedKey(this, key);
    }

    public boolean hasProtocolLib() {
        return getServer().getPluginManager().getPlugin("ProtocolLib") != null;
    }

}
