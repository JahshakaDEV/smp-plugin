package de.jahshaka.smp.methods;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.Button;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Instant;

public class AllowedPlayerList {

    public static void addPlayer(String uuid, String discordID, Plugin smp) {
        smp.getConfig().set("allowedplayers." + uuid, discordID);
        smp.saveConfig();
        if (discordID.matches("[0-9]") && discordID.length() > 15) {
            Smp.jda.getGuildById(Config.guildID).getMemberById(discordID).getUser().openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage("Du bist nun auf der Whitelist vom SMP-Server! Viel Spaß!").queue());
        }
    }

    public static Member getMemberFromUniqueID(String uuid, Smp smp) {
        String discordID = smp.getConfig().get("allowedplayers." + uuid).toString();
        if (!discordID.equals("to_be_filled")) {
            Member returnMemeber = Smp.jda.getGuildById(Config.guildID).getMemberById(discordID);
            return returnMemeber;
        } else {
            return null;
        }
    }

    public static void removePlayer(String uuid, Plugin smp) {
        smp.getConfig().set("allowedplayers." + uuid, null);
        smp.saveConfig();
    }

    public static boolean isPlayerAllowed(String uuid, Plugin smp) {
        return smp.getConfig().contains("allowedplayers." + uuid);
    }

    public static void addPlayerToWaitingList(Player player, Plugin smp) {
        smp.getConfig().set("waitinglist." + player.getUniqueId(), "yep");
        sendDiscordMessage(player, Smp.jda);
        smp.saveConfig();
    }

    public static void removePlayerFromWaitingList(String uuid, Plugin smp) {
        smp.getConfig().set("waitinglist." + uuid, null);
        smp.saveConfig();
    }

    public static boolean isPlayerOnWaitingList(Player player, Plugin smp) {
        return smp.getConfig().contains("waitinglist." + player.getUniqueId());
    }

    public static void addPlayerToBlocklist(String uuid, Plugin smp) {
        smp.getConfig().set("blocklist." + uuid, "yep");
        smp.saveConfig();
    }

    public static void removePlayerFromBlocklist(String uuid, Plugin smp) {
        smp.getConfig().set("blocklist." + uuid, null);
        smp.saveConfig();
    }

    public static boolean isPlayerOnBlocklist(Player player, Plugin smp) {
        return smp.getConfig().contains("blocklist." + player.getUniqueId());
    }

    public static void sendDiscordMessage(Player player, JDA jda) {
        MessageEmbed embed = EmbedUtils.getDefaultEmbed()
                .setTitle("Account zur Whitelist hinzufügen?")
                .addField("Name:", player.getName(), false)
                .addField("UUID:", player.getUniqueId().toString(), false)
                .setTimestamp(Instant.now())
                .build();
        jda.getGuildById(Config.guildID)
                .getTextChannelById(Config.whitelistChannelID)
                .sendMessageEmbeds(embed)
                .setActionRow(Button.primary("access_whitelist_" + player.getUniqueId(), "Hinzufügen"), Button.secondary("deny_whitelist_" + player.getUniqueId(), "Ablehnen"))
                .queue();
    }

}
