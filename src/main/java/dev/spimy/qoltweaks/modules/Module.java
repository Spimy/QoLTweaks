package dev.spimy.qoltweaks.modules;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.config.ConfigManager;
import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Module implements Listener {

    private String name;
    private ConfigManager configManager;
    private final HashMap<String, String> permissionNodes = new HashMap<>();

    private final QoLTweaks plugin = QoLTweaks.getInstance();
    private final String DEFAULT_PERMISSION_KEY = "default";

    protected Module() {
        setup();
    }

    protected Module(HashMap<String, Object> customConfigs) {
        setup();
        setCustomConfig(customConfigs);
        saveConfig();
    }

    protected Module(HashMap<String, Object> customConfigs, RemovableConfigPaths[] pathsToRemove) {
        setup();
        removeTemplatedConfig(pathsToRemove);
        setCustomConfig(customConfigs);
        saveConfig();
    }

    protected Module(RemovableConfigPaths[] pathsToRemove) {
        setup();
        removeTemplatedConfig(pathsToRemove);
        saveConfig();
    }

    private void setup() {
        String name = getClass().getSimpleName();
        name = name.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
        this.name = name;

        String[] splitPackageName = getClass().getPackageName().split("\\.");

        List<String> remainingPaths = new ArrayList<>();
        if (splitPackageName.length > 5) {
            remainingPaths = new ArrayList<>(
                Arrays.asList(splitPackageName)
            ).subList(5, splitPackageName.length);
        }

        String moduleType = splitPackageName[4];
        this.configManager = new ConfigManager(name, moduleType, remainingPaths);

        permissionNodes.put(DEFAULT_PERMISSION_KEY, String.format("qoltweaks.%s", name));
        plugin.getConfigManagers().put(name, configManager);
    }

    private void removeTemplatedConfig(RemovableConfigPaths[] pathsToRemove) {
        Arrays.stream(pathsToRemove).forEach(path -> getConfig().set(path.getPath(), null));
    }

    private void setCustomConfig(HashMap<String, Object> customConfigs) {
        customConfigs.forEach((path, value) -> {
            if (!getConfig().isSet(path)) getConfig().set(path, value);
        });
    }

    protected void addPermission(String key, String subPermission) {
        permissionNodes.put(key, String.format("qoltweaks.%s.%s", name, subPermission));
    }

    protected boolean isMissingPermission(Player player) {
        return isMissingPermission(player, DEFAULT_PERMISSION_KEY);
    }

    protected boolean isMissingPermission(Player player, String permissionKey) {
        if (!getConfig().getBoolean("require-permission")) return false;
        return !player.hasPermission(permissionNodes.get(permissionKey));
    }

    protected boolean isDisabled() {
        return !getConfig().getBoolean("enabled");
    }

    protected FileConfiguration getConfig() {
        return configManager.getConfig();
    }

    protected void saveConfig() {
        configManager.saveConfig();
    }

}