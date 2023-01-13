package dev.spimy.qoltweaks.modules.security;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import dev.spimy.qoltweaks.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class RestrictPing extends Module {

    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public RestrictPing() {
        super(
                new RemovableConfigPaths[]{
                        RemovableConfigPaths.REQUIRE_PERMISSION
                }
        );
    }

    @EventHandler()
    public void onPing(PaperServerListPingEvent event) {
        if (isDisabled()) return;

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
            try {
                Image resizedImage = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                BufferedImage resizedIcon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = resizedIcon.createGraphics();
                g2d.drawImage(resizedImage, 0, 0, null);
                g2d.dispose();

                event.setServerIcon(Bukkit.loadServerIcon(resizedIcon));
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load offline server icon.");
            }
        } catch (MalformedURLException e) {
            plugin.getLogger().warning("Failed to load url of offline server icon.");
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to read image from url provided.");
        }

    }

}
