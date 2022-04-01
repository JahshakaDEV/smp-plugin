package de.jahshaka.smp.methods;

import de.jahshaka.smp.Smp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class LocationMethods {

    static Smp smp = Smp.getPlugin();

    public static List<String> getLocations() {
        List<String> locations = new ArrayList<>();
        for (String key : smp.getConfig().getConfigurationSection("location").getKeys(false)) {
            locations.add(key);
        }
        return locations;
    }

    public static boolean locationExists(String LocationName) {
        LocationName = "location." + LocationName;
        return smp.getConfig().contains(LocationName + ".X");
    }

    public static void setLocation(String LocationName, Player p) {
        LocationName = "location." + LocationName;
        smp.getConfig().set(LocationName + ".X", p.getLocation().getX());
        smp.getConfig().set(LocationName + ".Y", p.getLocation().getY());
        smp.getConfig().set(LocationName + ".Z", p.getLocation().getZ());
        smp.getConfig().set(LocationName + ".Yaw", p.getLocation().getYaw());
        smp.getConfig().set(LocationName + ".Pitch", p.getLocation().getPitch());
        smp.getConfig().set(LocationName + ".World", p.getWorld().getName());

        smp.saveConfig();
    }

    public static void setLocationBlock(String LocationName, Block block) {
        LocationName = "location." + LocationName;
        smp.getConfig().set(LocationName + ".X", block.getLocation().getBlockX());
        smp.getConfig().set(LocationName + ".Y", block.getLocation().getBlockY());
        smp.getConfig().set(LocationName + ".Z", block.getLocation().getBlockZ());
        smp.getConfig().set(LocationName + ".Yaw", block.getLocation().getYaw());
        smp.getConfig().set(LocationName + ".Pitch", block.getLocation().getPitch());
        smp.getConfig().set(LocationName + ".World", block.getWorld().getName());

        smp.saveConfig();
    }


    public static Location getLocation(String LocationName) {
        LocationName = "location." + LocationName;
        double x = smp.getConfig().getDouble(LocationName + ".X");
        double y = smp.getConfig().getDouble(LocationName + ".Y");
        double z = smp.getConfig().getDouble(LocationName + ".Z");
        String world = smp.getConfig().getString(LocationName + ".World");
        org.bukkit.World w = Bukkit.getWorld(world);
        return new Location(w, x, y, z);

    }

}
