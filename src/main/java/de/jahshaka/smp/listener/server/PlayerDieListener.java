package de.jahshaka.smp.listener.server;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDieListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Smp.jda.getGuildById(Config.guildID).getTextChannelById(Config.logForModChannelID)
                .sendMessage(":skull_crossbones: " + event.getDeathMessage() + "! " + Config.timestampForLogs).queue();

    }

}
