package nl.thieme.tp.models;

import nl.thieme.tp.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileConfig {

    private String name;
    private File file;
    protected YamlConfiguration config;

    public FileConfig(String name) {
        this.name = name + ".yml";
        this.file = new File(Main.INSTANCE.getDataFolder(), this.name);
        this.config = new YamlConfiguration();
        getOrCreateConfig();
    }

    private void getOrCreateConfig() {
        if(Main.INSTANCE.DEBUG && file.exists()) file.delete();
        if (!file.exists()) {
            if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
            Main.INSTANCE.saveResource(name, false);
        }
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
