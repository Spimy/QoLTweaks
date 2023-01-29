package dev.spimy.qoltweaks.commands.subcommands;

import dev.spimy.qoltweaks.QoLTweaks;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommandHandler {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private SubCommandHandler() {
        String packageName = getClass().getPackageName();

        for (Class<? extends SubCommand> subCommand : new Reflections(packageName).getSubTypesOf(SubCommand.class)) {
            try {
                subCommands.add(subCommand.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final class SubCommandHandlerHolder {
        private static final SubCommandHandler subCommandHandler = new SubCommandHandler();
    }

    public static SubCommandHandler getInstance() {
        return SubCommandHandlerHolder.subCommandHandler;
    }

    public SubCommand getSubCommand(String command) {
        return subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(command)).findFirst().orElse(null);
    }

    public List<String> getSubCommandNames() {
        return this.subCommands.stream().map(SubCommand::getName).collect(Collectors.toList());
    }

    public ArrayList<String> getCommandList() {
        ArrayList<String> commandList = new ArrayList<>();

        subCommands.forEach(subCommand -> commandList.add(String.format(
            "&6/%s %s %s: &a%s",
            QoLTweaks.getInstance().getClass().getSimpleName().toLowerCase(),
            subCommand.getName(),
            String.join(" ", subCommand.getArguments().keySet()),
            subCommand.getDescription()
        )));

        return commandList;
    }

}
