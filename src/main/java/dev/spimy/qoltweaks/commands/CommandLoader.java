package dev.spimy.qoltweaks.commands;

import dev.spimy.qoltweaks.QoLTweaks;

public class CommandLoader {

    private QoLTweaks plugin;

    public CommandLoader(QoLTweaks plugin) {
        this.plugin = plugin;
        registerCommands();
    }


    private void registerCommands() {
        plugin.getCommand("qoltweaks").setExecutor(new BaseCommand(plugin));
    }

}
