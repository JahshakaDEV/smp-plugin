package de.jahshaka.smp.listener.server;

import de.jahshaka.smp.Config;
import de.jahshaka.smp.Smp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockBreakListener implements Listener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.DIAMOND_ORE)
                || event.getBlock().getType().equals(Material.ANCIENT_DEBRIS)
                || event.getBlock().getType().equals(Material.DEEPSLATE_DIAMOND_ORE)
                || event.getBlock().getType().equals(Material.SPAWNER)) {
            Location oreLocation = event.getBlock().getLocation();
            Smp.jda.getGuildById(Config.guildID).getTextChannelById(Config.logForModChannelID)
                    .sendMessage("`" + event.getPlayer().getName() + "` farmed `" + event.getBlock().getType().name() +
                            "` at X:" + oreLocation.getBlockX() + " Y:" + oreLocation.getBlockY() + " Z:" + oreLocation.getBlockZ() +
                            " in " + oreLocation.getWorld().getName() + " at " + Config.timestampForLogs).queue();
        }
    }

}
