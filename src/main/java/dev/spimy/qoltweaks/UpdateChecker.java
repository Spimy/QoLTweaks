package dev.spimy.qoltweaks;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final String spigotApi;

    public UpdateChecker(final int pluginId) {
        this.spigotApi = "https://api.spigotmc.org/legacy/update.php?resource=" + pluginId;
    }

    public String getCurrentVersion() {
        final QoLTweaks plugin = QoLTweaks.getInstance();
        return plugin.getDescription().getVersion();
    }

    public void check(final Consumer<String> consumer) {
        final QoLTweaks plugin = QoLTweaks.getInstance();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                final InputStream inputStream = new URL(spigotApi).openStream();
                final Scanner scanner = new Scanner(inputStream);

                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().warning("Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
