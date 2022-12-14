package com.hardpoint.hardpoint.management;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import com.hardpoint.Plugin;

public class FileManagement {
    private List<File> lFiles = new ArrayList<>();
    private List<File> lFolders = new ArrayList<>();
    private Map<String, YamlConfiguration> mConfigurations = new HashMap<>();
    private Map<String, File> mFiles = new HashMap<>();

    public FileManagement(Plugin plugin, Map<String, Boolean> names) {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();
        for (String key : names.keySet()) {
            if (!names.get(key)) {
                lFiles.add(new File(plugin.getDataFolder(), key));
            } else {
                lFolders.add(new File(plugin.getDataFolder(), key));
            }
        }

        for (int i = 0; i < lFolders.size(); i++) {
            if (!lFolders.get(i).exists())
                lFolders.get(i).mkdir();
        }

        for (int i = 0; i < lFiles.size(); i++) {
            mConfigurations.put(lFiles.get(i).getName(),
                    YamlConfiguration.loadConfiguration(lFiles.get(i)));
            mFiles.put(lFiles.get(i).getName(), lFiles.get(i));
        }

        for (int i = 0; i < mConfigurations.size(); i++) {
            try {
                mConfigurations.get(lFiles.get(i).getName()).save(lFiles.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // public void saveSchematics(Region region, Plugin plugin, String name) {
    // File file = new File(plugin.getDataFolder() + File.separator + "schematics",
    // name);
    // Schematic schem = new Schematic(region);
    // schem.save(file, ClipboardFormat.SCHEMATIC);
    // }

    public YamlConfiguration getConfiguration(String fileName) {
        return mConfigurations.get(fileName);
    }

    public void saveFile(String fileName) {
        try {
            mConfigurations.get(fileName).save(mFiles.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile(String fileName) {
        return mFiles.get(fileName);
    }
}
