package de.jahshaka.smp.commands.discord;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import de.jahshaka.smp.methods.AllowedPlayerList;
import net.dv8tion.jda.api.Permission;

import java.util.concurrent.TimeUnit;

public class ConnectMinecraftDiscordCMD implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (ctx.getArgs().size() == 2) {
                String uuid = ctx.getArgs().get(0);
                String discordID = ctx.getArgs().get(1);
                if (AllowedPlayerList.isPlayerAllowed(uuid, Smp.getPlugin())) {
                    if (discordID.matches("[0-9]+") && discordID.length() > 15) {
                        AllowedPlayerList.addPlayer(uuid, discordID, Smp.getPlugin());
                        ctx.getChannel().sendMessage("User erfolgreich verknüpft!").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                        ctx.getMessage().addReaction("U+2705").queue();
                    }
                } else {
                    ctx.getChannel().sendMessage("Diese Minecraft-UUID ist nicht gültig!").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
                }
            } else {
                ctx.getChannel().sendMessage("Command Syntax:\n" + Config.discordPrefix + "connect <Minecraft-UUID> <Discord-ID>").queue(message -> message.delete().queueAfter(7, TimeUnit.SECONDS));
            }
        }
    }

    @Override
    public String getName() {
        return "connect";
    }

    @Override
    public String getHelp() {
        return "Connect someones Minecraft account with their Discord!";
    }

    @Override
    public String getSyntax(String prefix) {
        return null;
    }
}