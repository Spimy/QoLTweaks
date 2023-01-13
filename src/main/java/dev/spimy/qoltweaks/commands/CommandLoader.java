package dev.spimy.qoltweaks.commands;

import dev.spimy.qoltweaks.QoLTweaks;

public class CommandLoader {

    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public CommandLoader() {
        registerCommands();
    }

    @SuppressWarnings("ConstantConditions")
    public void registerCommands() {
        plugin.getCommand(plugin.getClass().getSimpleName().toLowerCase()).setExecutor(new BaseCommand());
    }

}
