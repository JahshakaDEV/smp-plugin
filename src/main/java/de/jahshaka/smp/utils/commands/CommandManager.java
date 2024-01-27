package de.jahshaka.smp.utils.commands;

import de.jahshaka.smp.Smp;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandManager {

	public static boolean isCommandActive(String commandName) {
		String configPath = "minecraft.commands." + commandName;
		FileConfiguration config = Smp.getInstance().configManager.getConfig("configuration").getCustomConfig();
		if (config.contains(configPath)) {
			return config.getBoolean(configPath);
		} else {
			return false;
		}
	}

}
