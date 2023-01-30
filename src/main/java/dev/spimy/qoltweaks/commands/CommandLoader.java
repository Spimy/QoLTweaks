package dev.spimy.qoltweaks.commands;

import dev.spimy.qoltweaks.QoLTweaks;

public class CommandLoader {

    public CommandLoader() {
        registerCommands();
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommands() {
        final QoLTweaks plugin = QoLTweaks.getInstance();
        plugin.getCommand(plugin.getClass().getSimpleName().toLowerCase()).setExecutor(new BaseCommand());
    }

}
