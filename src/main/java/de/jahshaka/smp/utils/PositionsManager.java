package de.jahshaka.smp.utils;

import de.jahshaka.smp.Smp;
import de.jahshaka.smp.utils.config.ConfigManager;
import de.jahshaka.smp.utils.config.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PositionsManager {

	private final FileConfiguration config;
	private final CustomConfig customConfig;

	public PositionsManager() {
		ConfigManager configManager = Smp.getInstance().configManager;
		customConfig = configManager.getConfig("position");
		config = customConfig.getCustomConfig();
	}

	public void setPosition(String name, Location location) {
		config.set(name + ".world", location.getWorld().getName());
		config.set(name + ".X", location.getBlockX());
		config.set(name + ".Y", location.getBlockY());
		config.set(name + ".Z", location.getBlockZ());
		customConfig.saveConfig();
	}

	public void setPosition(String name, int x, int y, int z, String world) {
		config.set(name + ".world", world);
		config.set(name + ".X", x);
		config.set(name + ".Y", y);
		config.set(name + ".Z", z);
		customConfig.saveConfig();
	}

	public Location getPositions(String name) {
		if (exists(name)) {
			String worldName;
			double x, y, z;
			x = config.getInt(name + ".X");
			y = config.getInt(name + ".Y");
			z = config.getInt(name + ".Z");
			worldName = config.getString(name + ".world");
			World world = Bukkit.getWorld(worldName);
			return new Location(world, x, y, z);
		} else {
			return null;
		}
	}

	public void deletePosition(String name) {
		if (exists(name)) {
			config.set(name + ".world", null);
			config.set(name + ".X", null);
			config.set(name + ".Y", null);
			config.set(name + ".Z", null);
			config.set(name, null);
			customConfig.saveConfig();
		}
	}

	public List<String> getPositionsNamesAsList() {
		return new ArrayList<>(config.getKeys(false));
	}

	public boolean exists(String name) {
		return config.contains(name);
	}
}
