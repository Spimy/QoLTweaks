package dev.spimy.qoltweaks.modules;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.modules.blocks.LadderWarp;
import dev.spimy.qoltweaks.modules.enchanting.BookExtract;
import dev.spimy.qoltweaks.modules.entities.NametagShear;
import dev.spimy.qoltweaks.modules.entities.petting.Petting;
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
        this.registerEvent(new BookExtract(plugin));
        this.registerEvent(new NametagShear(plugin));
        this.registerEvent(new Petting(plugin));
    }

}
