package de.jahshaka.smp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Config {

    public static String guildID = Objects.requireNonNull(Smp.getPlugin().getConfig().get("guild_id")).toString();
    public static String discordPrefix = Objects.requireNonNull(Smp.getPlugin().getConfig().get("discord_prefix")).toString();
    public static String ownerID = Objects.requireNonNull(Smp.getPlugin().getConfig().get("owner_id")).toString();
    public static String chatLogChannelID = Objects.requireNonNull(Smp.getPlugin().getConfig().get("chat_log_channel_id")).toString();
    public static String logForModChannelID = Objects.requireNonNull(Smp.getPlugin().getConfig().get("log_for_mod_channel_id")).toString();
    public static String announcementChannelID = Objects.requireNonNull(Smp.getPlugin().getConfig().get("announcement_channel_id")).toString();
    public static String whitelistChannelID = Objects.requireNonNull(Smp.getPlugin().getConfig().get("whitelist_channel_id")).toString();
    public static String timestampForLogs = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(getLocatDateTime()) + "! \n▬▬▬▬▬▬▬▬▬▬";
    public static String webhookURL = Objects.requireNonNull(Smp.getPlugin().getConfig().get("webhook_url")).toString();
    public static String channelForMcChatUserID = Objects.requireNonNull(Smp.getPlugin().getConfig().get("chat_for_user_channel_id")).toString();
    public static TextComponent tc = Component.text("[", NamedTextColor.GRAY).append(Component.text("SMP", NamedTextColor.YELLOW)).append(Component.text("] ", NamedTextColor.GRAY));

    public static LocalDateTime getLocatDateTime() {
        return LocalDateTime.now();
    }

    public static String calculateUptime(long startup) {
        long now = System.currentTimeMillis();
        long seconds = now - startup;
        seconds = seconds / 1000;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        while (seconds >= 60) {
            seconds = seconds - 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes = minutes - 60;
            hours++;
        }
        while (hours >= 24) {
            hours = hours - 24;
            days++;
        }
        while (days >= 7) {
            days = days - 7;
            weeks++;
        }
        return "Der Server ist online für " + weeks + " Wochen " + days + " Tage " + hours + " Stunden " + minutes + " Minuten und " + seconds + " Sekunden!";
    }
}
