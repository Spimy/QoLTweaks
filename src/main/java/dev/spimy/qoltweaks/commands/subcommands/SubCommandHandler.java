package dev.spimy.qoltweaks.commands.subcommands;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommandHandler {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private SubCommandHandler() {
        String packageName = getClass().getPackageName();

        for (final Class<? extends SubCommand> subCommand : new Reflections(packageName).getSubTypesOf(SubCommand.class)) {
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

    public SubCommand getSubCommand(final String command) {
        return subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(command)).findFirst().orElse(null);
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public List<String> getSubCommandNames() {
        return subCommands.stream().map(SubCommand::getName).collect(Collectors.toList());
    }

}
