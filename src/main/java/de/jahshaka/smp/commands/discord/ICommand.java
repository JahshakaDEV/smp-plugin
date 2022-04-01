package de.jahshaka.smp.commands.discord;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    String getSyntax(String prefix);

    default List<String> getAliases() {
        return List.of();
    }
}
