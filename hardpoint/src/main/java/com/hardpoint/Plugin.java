package com.hardpoint;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.hardpoint.hardpoint.HardPointGestion;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/*
 * hardpoint java plugin
 */
public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER=Logger.getLogger("hardpoint");

  public void onEnable()
  {
    File hardpointf = new File(getDataFolder(), "hardpoint.yml");

    if (!getDataFolder().exists()) getDataFolder().mkdirs();
    if (!hardpointf.exists()) saveResource("hardpoint.yml", false);

    YamlConfiguration hardpoint = new YamlConfiguration();

    try {
      hardpoint.load(hardpointf);
    } catch (IOException | InvalidConfigurationException e){
      e.printStackTrace();
    }

    getCommand("hardpoint").setExecutor(new HardPointGestion(this, hardpoint));
  }

  public void onDisable()
  {
    LOGGER.info("hardpoint disabled");
  }
}
