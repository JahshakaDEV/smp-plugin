package de.jahshaka.smp;

import de.jahshaka.smp.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Smp extends JavaPlugin {

    public ConfigManager configManager;

    public static Smp getInstance() {
        return JavaPlugin.getPlugin(Smp.class);
    }

    public void onEnable() {
        setupConfigs();
        Bukkit.getLogger().info("SMP-Plugin has been enabled!");
    }

    private void setupConfigs() {
        configManager = new ConfigManager();
    }

}
