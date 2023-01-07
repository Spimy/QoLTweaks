package dev.spimy.qoltweaks.modules.misc;

import dev.spimy.qoltweaks.Modules;
import dev.spimy.qoltweaks.QoLTweaks;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import java.util.HashSet;
import java.util.Set;

public class HostnameWhitelist implements Listener {

    private final QoLTweaks plugin;

    public HostnameWhitelist(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.MISC);
        if (!config.getBoolean("hostname-whitelist.enabled")) return;

        String hostName = event.getHostname();
        int port = hostName.indexOf(":");
        if (port != -1) {
            hostName = hostName.substring(0, port);
        }

        if (hostName == null) {
            if (!config.getBoolean("hostname-whitelist.block-legacy")) return;
        }

        Set<String> whitelistedHostnames = new HashSet<>(plugin.getConfig().getStringList("allowed-hostnames"));
        if (whitelistedHostnames.contains(hostName)) return;

        ConfigurationSection langConfig = plugin.getConfig().getConfigurationSection("lang");
        event.disallow(
                Result.KICK_OTHER,
                Component.text(plugin.formatMessage(langConfig.getString("invalid-hostname")))
        );
    }

}
