package dev.spimy.qoltweaks.commands.subcommands;

import dev.spimy.qoltweaks.QoLTweaks;
import dev.spimy.qoltweaks.commands.subcommands.commands.Help;
import dev.spimy.qoltweaks.commands.subcommands.commands.Reload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCommandHandler {

    private final QoLTweaks plugin;
    private final HashMap<String, SubCommand> subCommands = new HashMap<>();


    public SubCommandHandler(QoLTweaks plugin) {
        this.plugin = plugin;
        subCommands.put("reload", new Reload(plugin));
        subCommands.put("help", new Help(plugin, this));
    }

    public SubCommand getSubCommand(String command) {
        return subCommands.get(command);
    }

    public ArrayList<String> getCommandList() {
        ArrayList<String> commandList = new ArrayList<>();

        for (Map.Entry<String, SubCommand> entry : subCommands.entrySet()) {
            commandList.add(String.format(
                    "&6/%s %s: &a%s %s",
                    plugin.getClass().getSimpleName().toLowerCase(),
                    entry.getKey(),
                    entry.getValue().getDescription(),
                    entry.getValue().getArgumentsList()
            ));
        }

        return commandList;
    }

}
