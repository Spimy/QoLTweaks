package dev.spimy.qoltweaks.modules.security;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class HostnameWhitelist extends Module {

    public HostnameWhitelist() {
        super(
            new HashMap<>() {{
                put("block-legacy", true);
            }},
            new RemovableConfigPaths[]{
                RemovableConfigPaths.REQUIRE_PERMISSION
            }
        );
    }

    @EventHandler
    @SuppressWarnings("ConstantConditions")
    public void onLogin(PlayerLoginEvent event) {
        if (isDisabled()) return;

        String hostName = event.getHostname();
        int port = hostName.indexOf(":");
        if (port != -1) {
            hostName = hostName.substring(0, port);
        }

        if (hostName == null) {
            if (!getConfigManager().getConfig().getBoolean("block-legacy")) return;
        }

        final QoLTweaks plugin = QoLTweaks.getInstance();
        Set<String> whitelistedHostnames = new HashSet<>(plugin.getConfig().getStringList("allowed-hostnames"));
        if (whitelistedHostnames.contains(hostName)) return;

        event.disallow(
            Result.KICK_OTHER,
            plugin.getMessageManager().getConfigMessage("invalid-hostname", false)
        );
    }

}
