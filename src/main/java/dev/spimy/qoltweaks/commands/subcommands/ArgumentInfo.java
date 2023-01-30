package dev.spimy.qoltweaks.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

public record ArgumentInfo(String infoArg, String argDescription, boolean required, List<String> arguments) {

    public ArgumentInfo(final String infoArg, final String argDescription, final boolean required) {
        this(infoArg, argDescription, required, new ArrayList<>());
    }

    @Override
    public String infoArg() {
        return required ? String.format("<%s>", infoArg) : String.format("[%s]", infoArg);
    }

}
