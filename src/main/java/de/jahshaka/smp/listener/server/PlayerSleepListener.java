package de.jahshaka.smp.listener.server;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.ArrayList;

public class PlayerSleepListener implements Listener {

    public static ArrayList<String> inBed = new ArrayList<>();
    public static boolean skipNight = false;

    public static void playerBedEnterHandler(Player player) {
        if (!inBed.contains(player.getUniqueId().toString())) {
            inBed.add(player.getUniqueId().toString());
        }
        double inBed = PlayerSleepListener.inBed.size();
        double percentage = inBed / player.getWorld().getPlayerCount();
        final TextComponent msgInBed = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("SMP", NamedTextColor.YELLOW))
                .append(Component.text("] " + player.getName() + " ist im Bett.", NamedTextColor.GRAY));
        Bukkit.broadcast(msgInBed);
        if (percentage >= 0.50) {
            skipNights(player);
        } else {
            if (PlayerSleepListener.inBed.isEmpty() || day(player.getWorld())) {
                return;
            }

            double percent = percentage * 100;
            final TextComponent message = Component.text("[", NamedTextColor.GRAY)
                    .append(Component.text("SMP", NamedTextColor.YELLOW))
                    .append(Component.text("] " + Double.toString(inBed).substring(0, Double.toString(inBed).indexOf(".")) + "/" + player.getWorld().getPlayerCount()
                            + " Spieler sind im Bett!\n", NamedTextColor.GRAY))
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("SMP", NamedTextColor.YELLOW))
                    .append(Component.text("] " + Double.toString(percent).substring(0, Double.toString(percent).indexOf(".")) + "%/50%", NamedTextColor.GRAY));
            Bukkit.broadcast(message);
        }
    }

    public static void checkForNightSkip(Player player) {
        double inBed = PlayerSleepListener.inBed.size();
        double percentage = inBed / player.getWorld().getPlayerCount();
        if (percentage >= 0.50) {
            skipNights(player);
        } else {
            if (PlayerSleepListener.inBed.isEmpty() || day(player.getWorld())) {
                return;
            }
            double percent = percentage * 100;
            final TextComponent message = Component.text("[", NamedTextColor.GRAY)
                    .append(Component.text("SMP", NamedTextColor.YELLOW))
                    .append(Component.text("] " + Double.toString(inBed).substring(0, Double.toString(inBed).indexOf(".")) + "/" + player.getWorld().getPlayerCount()
                            + " Spieler sind im Bett!\n", NamedTextColor.GRAY))
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("SMP", NamedTextColor.YELLOW))
                    .append(Component.text("] " + Double.toString(percent).substring(0, Double.toString(percent).indexOf(".")) + "%/50%", NamedTextColor.GRAY));
            Bukkit.broadcast(message);
        }
    }

    public static void skipNights(Player player) {
        if (PlayerSleepListener.inBed.isEmpty() || day(player.getWorld())) {
            return;
        }
        final TextComponent message = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("SMP", NamedTextColor.YELLOW))
                .append(Component.text("] " + PlayerSleepListener.inBed.size() + "/" + player.getWorld().getPlayerCount()
                        + " Spieler sind im Bett!", NamedTextColor.GRAY));
        final TextComponent msgNightSkip = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("SMP", NamedTextColor.YELLOW))
                .append(Component.text("] " + "Die Nacht wird geskipped!", NamedTextColor.GRAY));
        Bukkit.broadcast(message);
        Bukkit.broadcast(msgNightSkip);
        skipNight = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Smp.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (skipNight) {
                    setDatime(player);
                    String msg = inBed + "/" + player.getWorld().getPlayerCount() + " in Bed! Skip night ";
                    Smp.jda.getGuildById(Config.guildID).getTextChannelById(Config.logForModChannelID)
                            .sendMessage(msg + Config.timestampForLogs).queue();
                }
            }
        }, 50L); //20 Tick (1 Second) delay before run() is called
    }

    public static void setDatime(Player player) {
        World world = player.getWorld();
        if (world.hasStorm()) {
            world.setStorm(false);
        }
        if (world.isThundering()) {
            world.setThundering(false);
        }
        long relativeTime = 24000 - world.getTime();
        world.setFullTime(world.getFullTime() + relativeTime);
    }

    public static boolean day(World world) {
        long time = world.getTime();

        return time < 12300 || time > 23850;
    }

    public static void playerBedLeaveHandler(Player player) {
        inBed.remove(player.getUniqueId().toString());
        double inBed = PlayerSleepListener.inBed.size();
        double percentage = inBed / player.getWorld().getPlayerCount();
        final TextComponent msgLeftBed = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("SMP", NamedTextColor.YELLOW))
                .append(Component.text("] " + player.getName() + " hat sein Bett verlassen.", NamedTextColor.GRAY));
        Bukkit.broadcast(msgLeftBed);
        if (percentage < 0.50) {
            skipNight = false;
            if (PlayerSleepListener.inBed.isEmpty() || day(player.getWorld())) {
                return;
            }
            int percent = (int) percentage * 100;
            final TextComponent message = Component.text("[", NamedTextColor.GRAY)
                    .append(Component.text("SMP", NamedTextColor.YELLOW))
                    .append(Component.text("] " + inBed + "/" + player.getWorld().getPlayerCount()
                            + " Spieler sind im Bett!\n", NamedTextColor.GRAY))
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text("SMP", NamedTextColor.YELLOW))
                    .append(Component.text("] " + percent + "%/50%", NamedTextColor.GRAY));
            Bukkit.broadcast(message);
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!event.isCancelled()) {
            playerBedEnterHandler(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (!event.isCancelled()) {
            playerBedLeaveHandler(event.getPlayer());
        }
    }

}
