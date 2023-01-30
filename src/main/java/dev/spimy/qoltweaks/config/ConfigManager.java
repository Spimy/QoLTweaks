package dev.spimy.qoltweaks.config;

import dev.spimy.qoltweaks.QoLTweaks;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

public class ConfigManager {

    private File configFile;
    private FileConfiguration config;
    private final String fileName;
    private final String filePath;
    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public ConfigManager(final String fileName, final String moduleType, final List<String> remainingPaths) {
        this.fileName = fileName.endsWith(".yml") ? fileName : fileName + ".yml";

        remainingPaths.add(0, moduleType);
        remainingPaths.add(this.fileName);
        this.filePath = Paths.get("modules", remainingPaths.toArray(String[]::new)).toString();

        saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        if (configFile == null || config == null) return;

        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + fileName);
        }

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveDefaultConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), filePath);
        }

        if (configFile.exists()) {
            reloadConfig();
            return;
        }

        final InputStream template = plugin.getResource("template.yml");
        if (template == null) return;

        final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(template));
        try {
            defaultConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load the template configs for " + fileName);
        }

        reloadConfig();
    }

}
