package dev.spimy.qoltweaks.modules;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.modules.blocks.LadderWarp;
import org.bukkit.event.Listener;

public class ModuleLoader {

    QoLTweaks plugin;

    public ModuleLoader(QoLTweaks plugin) {
        this.plugin = plugin;
        this.loadModules();
    }

    private void registerEvent(Listener event) {
        plugin.getServer().getPluginManager().registerEvents(event, plugin);
    }

    private void loadModules() {
        this.registerEvent(new LadderWarp(plugin));
    }

}
