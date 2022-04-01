package de.jahshaka.smp.listener.server;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import de.jahshaka.smp.methods.AllowedPlayerList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final net.kyori.adventure.text.TextComponent header = net.kyori.adventure.text.Component.text("Jahshaka-SMP\n", NamedTextColor.GRAY);
        Smp.getPlugin().getServer().sendPlayerListHeader(header);
        final TextComponent joinMessage = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("+", NamedTextColor.GREEN))
                .append(Component.text("] " + event.getPlayer().getName(), NamedTextColor.GRAY));
        event.joinMessage(joinMessage);
        Smp.jda.getGuildById(Config.guildID).getTextChannelById(Config.logForModChannelID)
                .sendMessage("`" + event.getPlayer().getName() + "` joined the Server! " + Config.timestampForLogs()).queue();
    }


    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        final TextComponent quitMessage = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("-", NamedTextColor.RED))
                .append(Component.text("] " + event.getPlayer().getName(), NamedTextColor.GRAY));
        event.quitMessage(quitMessage);
        Smp.jda.getGuildById(Config.guildID).getTextChannelById(Config.logForModChannelID)
                .sendMessage("`" + event.getPlayer().getName() + "` left the Server! " + Config.timestampForLogs()).queue();
        if (PlayerSleepListener.inBed.contains(event.getPlayer().getUniqueId().toString())) {
            PlayerSleepListener.inBed.remove(event.getPlayer().getUniqueId().toString());
        }
        PlayerSleepListener.checkForNightSkip(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!AllowedPlayerList.isPlayerAllowed(event.getPlayer().getUniqueId().toString(), Smp.getPlugin()) || AllowedPlayerList.isPlayerOnBlocklist(event.getPlayer(), Smp.getPlugin())) {
            final TextComponent kickMessage = Component.text("§l✗ Du hast keine Berechrigung zu joinen ✗\n\n", NamedTextColor.RED)
                    .append(Component.text("\nDein Minecraft Account befindet sich nicht auf der Whiteliste!\n", NamedTextColor.GRAY))
                    .append(Component.text("\n§l✗ Du hast keine Berechrigung zu joinen ✗\n\n", NamedTextColor.RED));
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, kickMessage);
            if (!AllowedPlayerList.isPlayerOnWaitingList(event.getPlayer(), Smp.getPlugin()) && !AllowedPlayerList.isPlayerOnBlocklist(event.getPlayer(), Smp.getPlugin())) {
                AllowedPlayerList.addPlayerToWaitingList(event.getPlayer(), Smp.getPlugin());
            }
        }
    }


}
