package de.jahshaka.smp.commands.server;

import de.jahshaka.smp.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaytimeCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        } else {
            Player player = (Player) commandSender;
            if (label.equalsIgnoreCase("playtime")) {
                long ticksPlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
                System.out.println(ticksPlayed);
                long seconds = ticksPlayed / 20;
                System.out.println(seconds);
                long minutes = 0;
                long hours = 0;
                long days = 0;
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
                TextComponent tc = Config.tc;
                tc = tc.append(Component.text("Deine Spielzeit beträgt " + days + " Tage, " + hours + " Stunden und " + minutes + " Minuten.", NamedTextColor.GRAY));
                player.sendMessage(tc);
                return true;
            }
        }
        return false;
    }
}
