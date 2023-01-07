package dev.spimy.qoltweaks.modules.misc;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import dev.spimy.qoltweaks.Modules;
import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class RestrictPing implements Listener {

    private final QoLTweaks plugin;

    public RestrictPing(QoLTweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler()
    public void onPing(PaperServerListPingEvent event) {

        ConfigurationSection config = plugin.configManager.getModuleConfig(Modules.MISC);
        if (!config.getBoolean("restrict-ping.enabled")) return;

        InetSocketAddress virtualAddress = event.getClient().getVirtualHost();
        if (virtualAddress == null) {
            plugin.getLogger().warning("Someone tried to ping the server using an unknown address.");
            event.setCancelled(true);
            return;
        }

        String hostName = virtualAddress.getHostName();
        Set<String> whitelistedHostnames = new HashSet<>(plugin.getConfig().getStringList("allowed-hostnames"));

        if (whitelistedHostnames.contains(hostName)) return;
        event.setCancelled(true);

        final String ICON_URL = "https://media.minecraftforum.net/attachments/300/619/636977108000120237.png";
        try {
            URL url = new URL(ICON_URL);
            BufferedImage img = ImageIO.read(url);
            event.setServerIcon(Bukkit.loadServerIcon(img));
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load offline server icon.");
        }

    }

}
