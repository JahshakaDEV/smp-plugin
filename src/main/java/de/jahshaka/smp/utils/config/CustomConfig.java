package de.jahshaka.smp.utils.config;

import de.jahshaka.smp.Smp;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class CustomConfig {
    private final File customFile;
    Smp smp = Smp.getInstance();
    private String name;
    private FileConfiguration customConfig;

    public CustomConfig(String name) {
        this.name = name;
        this.customFile = new File(smp.getDataFolder(), name + ".yml");
        if (!customFile.exists()) {
            try {
                if (!customFile.createNewFile()) {
                    smp.getLogger().log(Level.SEVERE, "[1706351980827] There was an error creating the " + name + " config!");
                }
            } catch (IOException exception) {
                smp.getLogger().log(Level.SEVERE, "[1706352076626] " + exception.getMessage() + " caused by: " + exception.getCause());
            }
        }
        this.customConfig = YamlConfiguration.loadConfiguration(customFile);
        getCustomConfig().options().copyDefaults(true);
        InputStream defaultConfig = smp.getResource(name + ".yml");
        if (defaultConfig != null) {
            getCustomConfig().setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfig)));
        }
        saveConfig();
    }

    public void set(String path, Object object) {
        this.customConfig.set(path, object);
        saveConfig();
    }

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    private void setCustomConfig(FileConfiguration customConfig) {
        this.customConfig = customConfig;
    }

    public void saveConfig() {
        try {
            this.customConfig.save(customFile);
        } catch (IOException exception) {
            smp.getLogger().log(Level.SEVERE, "[1706352109445] " + exception.getMessage() + " caused by: " + exception.getCause());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
