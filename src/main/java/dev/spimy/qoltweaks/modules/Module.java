package dev.spimy.qoltweaks.modules;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.config.ConfigManager;
import dev.spimy.qoltweaks.config.RemovableConfigPaths;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Module implements Listener {

    private String name;
    private boolean requireProtocolLib;

    private ConfigManager configManager;
    private final HashMap<String, String> permissionNodes = new HashMap<>();


    private final QoLTweaks plugin = QoLTweaks.getInstance();
    private final String DEFAULT_PERMISSION_KEY = "default";
    private ProtocolManager protocolManager = null;

    protected Module(boolean requireProtocolLib) {
        setup(requireProtocolLib);
    }

    protected Module(boolean requireProtocolLib, HashMap<String, Object> customConfigs) {
        setup(requireProtocolLib);
        setCustomConfig(customConfigs);
        configManager.saveConfig();
    }

    protected Module(boolean requireProtocolLib, HashMap<String, Object> customConfigs, RemovableConfigPaths[] pathsToRemove) {
        setup(requireProtocolLib);
        removeTemplatedConfig(pathsToRemove);
        setCustomConfig(customConfigs);
        configManager.saveConfig();
    }

    protected Module(boolean requireProtocolLib, RemovableConfigPaths[] pathsToRemove) {
        setup(requireProtocolLib);
        removeTemplatedConfig(pathsToRemove);
        configManager.saveConfig();
    }

    private void setup(boolean requireProtocolLib) {
        this.requireProtocolLib = requireProtocolLib;
        if (requireProtocolLib) protocolManager = ProtocolLibrary.getProtocolManager();

        String name = getClass().getSimpleName();
        name = name.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
        this.name = name;

        String[] splitPackageName = getClass().getPackageName().split("\\.");

        List<String> remainingPaths = new ArrayList<>();
        if (splitPackageName.length > 5) {
            remainingPaths = new ArrayList<>(
                Arrays.asList(splitPackageName)
            ).subList(5, splitPackageName.length - 1);
        }

        String moduleType = splitPackageName[4];
        this.configManager = new ConfigManager(name, moduleType, remainingPaths);

        permissionNodes.put(DEFAULT_PERMISSION_KEY, String.format("qoltweaks.%s", name));
        plugin.getConfigManagers().put(name, configManager);
    }

    private void removeTemplatedConfig(RemovableConfigPaths[] pathsToRemove) {
        Arrays.stream(pathsToRemove).forEach(path -> configManager.getConfig().set(path.getPath(), null));
    }

    private void setCustomConfig(HashMap<String, Object> customConfigs) {
        customConfigs.forEach((path, value) -> {
            if (!configManager.getConfig().isSet(path)) configManager.getConfig().set(path, value);
        });
    }

    protected void addPermission(String key, String subPermission) {
        permissionNodes.put(key, String.format("qoltweaks.%s.%s", name, subPermission));
    }

    protected boolean isMissingPermission(Player player) {
        return isMissingPermission(player, DEFAULT_PERMISSION_KEY);
    }

    protected boolean isMissingPermission(Player player, String permissionKey) {
        if (!configManager.getConfig().getBoolean("require-permission")) return false;
        return !player.hasPermission(permissionNodes.get(permissionKey));
    }

    protected boolean isDisabled() {
        return !configManager.getConfig().getBoolean("enabled");
    }

    public boolean requireProtocolLib() {
       return requireProtocolLib;
    }

    public String getName() {
        return name;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public @Nullable ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
