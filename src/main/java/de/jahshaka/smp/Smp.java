package de.jahshaka.smp;

import de.jahshaka.smp.commands.PositionCMD;
import de.jahshaka.smp.utils.commands.CommandManager;
import de.jahshaka.smp.utils.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Smp extends JavaPlugin {

	public ConfigManager configManager;
	public TextComponent prefix = Component.text("[")
			.color(TextColor.color(0xa8a8a8a))
			.append(Component.text("SMP")
					.color(TextColor.color(0xffc72b)))
			.append(Component.text("] ")
					.color(TextColor.color(0xa8a8a8a)));

	public static Smp getInstance() {
		return JavaPlugin.getPlugin(Smp.class);
	}

	public void onEnable() {
		setupConfigs();
		registerCommands();
		Bukkit.getLogger().info("SMP-Plugin has been enabled!");
	}

	private void registerCommands() {
		if (CommandManager.isCommandActive("position")) {
			getCommand("position").setExecutor(new PositionCMD());
		}
	}

	private void setupConfigs() {
		configManager = new ConfigManager();
		configManager.newConfig("configuration");
		if (CommandManager.isCommandActive("position")) {
			configManager.newConfig("position");
		}
	}

}
