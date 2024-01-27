package de.jahshaka.smp.utils.config;

import de.jahshaka.smp.Smp;

import java.util.HashMap;
import java.util.logging.Level;

public class ConfigManager {

    private final HashMap<String, CustomConfig> configs = new HashMap<>();

    public void newConfig(String name) {
        if (!configs.containsKey(name)) {
            CustomConfig config = new CustomConfig(name);
            configs.put(name, config);
        } else {
            Smp.getInstance().getLogger().log(Level.SEVERE, "[1676856767] Config " + name + " couldn't be created.");
        }
    }

    public CustomConfig getConfig(String name) {
        if (configs.containsKey(name)) {
            return configs.get(name);
        } else {
            Smp.getInstance().getLogger().log(Level.SEVERE, "[1676856768] Config " + name + " couldn't be loaded.");
            return null;
        }
    }
}
