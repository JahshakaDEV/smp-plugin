package de.jahshaka.smp.commands.discord;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;

public class UptimeCMD implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getArgs().size() > 0) {
            ctx.getMessage().delete().queue();
        } else {
            ctx.getChannel().sendMessage(Config.calculateUptime(Smp.startup)).queue();
            ctx.getMessage().addReaction("U+2705").queue();
        }
    }

    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getSyntax(String prefix) {
        return null;
    }
}
