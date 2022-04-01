package de.jahshaka.smp.methods;

import de.jahshaka.smp.Smp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PositionsAPI {
    static Smp smp = Smp.getPlugin();

    public static List<String> getPositions(Player player) {
        List<String> locations = new ArrayList<>();
        if (smp.getConfig().contains("position." + player.getUniqueId().toString())) {
            for (String key : smp.getConfig().getConfigurationSection("position." + player.getUniqueId().toString()).getKeys(false)) {
                locations.add(key);
            }
            return locations;
        } else {
            return locations;
        }
    }

    public static boolean positionExists(String positionName, Player player) {
        positionName = "position." + player.getUniqueId().toString() + "." + positionName;
        return smp.getConfig().contains(positionName + ".X");
    }

    public static void setPosition(String positionName, Player p) {
        positionName = "position." + p.getUniqueId().toString() + "." + positionName;
        smp.getConfig().set(positionName + ".X", p.getLocation().getX());
        smp.getConfig().set(positionName + ".Y", p.getLocation().getY());
        smp.getConfig().set(positionName + ".Z", p.getLocation().getZ());
        smp.getConfig().set(positionName + ".Yaw", p.getLocation().getYaw());
        smp.getConfig().set(positionName + ".Pitch", p.getLocation().getPitch());
        smp.getConfig().set(positionName + ".World", p.getWorld().getName());

        smp.saveConfig();
    }

    public static void deletePosition(String positionName, Player p) {
        positionName = "position." + p.getUniqueId().toString() + "." + positionName;
        smp.getConfig().set(positionName, null);
        smp.saveConfig();
    }

    public static Location getLocation(String positionName, Player player) {
        positionName = "position." + player.getUniqueId().toString() + "." + positionName;
        double x = smp.getConfig().getDouble(positionName + ".X");
        double y = smp.getConfig().getDouble(positionName + ".Y");
        double z = smp.getConfig().getDouble(positionName + ".Z");
        String world = smp.getConfig().getString(positionName + ".World");
        org.bukkit.World w = Bukkit.getWorld(world);
        return new Location(w, x, y, z);

    }
}
