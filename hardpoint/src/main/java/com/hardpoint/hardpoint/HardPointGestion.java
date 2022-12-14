package com.hardpoint.hardpoint;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.hardpoint.hardpoint.management.FileManagement;
import com.hardpoint.hardpoint.management.RegionManagement;

public class HardPointGestion implements CommandExecutor {
    private Plugin _plugin;
    private FileManagement _fileManagement;


    public HardPointGestion(Plugin plugin, FileManagement fileManagement) {
        _plugin = plugin;
        _fileManagement = fileManagement;
    }

    private void commands_gestion(String[] args, Player player) {
        switch (args[0]) {
            case "create":
                if (args.length > 1)
                    Create(player, args[1]);
                else {
                    player.sendMessage(new String[] {
                            ChatColor.DARK_PURPLE + "Commands usage:",
                            helps_string("create", "<name>", "Create an hardpoint.") });
                }
                break;
            case "select":
                // SelectMap(player);
                break;
            case "list":
                ShowAllHardpoint(player);
                break;
            default:
                player.sendMessage(help());
                break;
        }
    }

    private void Create(Player player, String name) {
        RegionManagement regionManagement = new RegionManagement(player);
        _fileManagement.getConfiguration("hardpoint.yml").createSection("test");
        if (!_fileManagement.getConfiguration("hardpoint.yml").contains("hardPoint")) {
            _fileManagement.getConfiguration("hardpoint.yml").createSection("hardPoint");
        }
        if (_fileManagement.getConfiguration("hardpoint.yml").contains("hardPoint." + name)) {
            player.sendMessage(ChatColor.DARK_RED + (ChatColor.BOLD + name)
                    + (ChatColor.RESET + (ChatColor.DARK_RED + " Already exists!")));
        } else if (regionManagement.getRegion() != null){
            _fileManagement.getConfiguration("hardpoint.yml").set("hardPoint." + name + ".location", player.getLocation());
            _fileManagement.getConfiguration("hardpoint.yml").set("hardPoint." + name + ".radius", 10);

            _fileManagement.saveFile("hardpoint.yml");
            regionManagement.saveSchematic(player, name, new File(_plugin.getDataFolder(), "schematics"));
            // regionManagement.saveRegion(_plugin, name, new BlockArrayClipboard(regionManagement.getRegion()));
            player.sendMessage(ChatColor.DARK_GREEN + "Hardpoint named: " + name + " created!");
        } else {
            player.sendMessage("Impossible to create map, please select region first!");
        }
    }

    private String helps_string(String command, String argument, String desc) {
        return ("/hardpoint " + command + " " + (ChatColor.LIGHT_PURPLE + argument)
                + (ChatColor.DARK_PURPLE + " - " + desc + "."));
    };

    private String[] help() {
        String[] helpStrings = {
                ChatColor.DARK_PURPLE + "All commands usage(s):",
                helps_string("create", "<name>", "Create an hardpoint."),
                helps_string("updatepos", "<name>", "Update the position of you're hardpoint."),
                helps_string("list", "", "List of all hardpoints."),
        };
        return helpStrings;
    }

    private void ShowAllHardpoint(Player player) {
        if (_fileManagement.getConfiguration("hardpoint.yml").contains("hardPoint")) {
            player.sendMessage(ChatColor.DARK_PURPLE + "Hardpoint list:");
            for (String key : _fileManagement.getConfiguration("hardpoint.yml").getConfigurationSection("hardPoint").getKeys(false)) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + key);
            }
        } else {
            player.sendMessage(new String[] {
                    ChatColor.DARK_PURPLE + "No hardpoint configuration have been found. See:",
                    helps_string("create", "<name>", "Create an hardpoint."),
            });
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("hardpoint")) {
            if (args.length <= 0) {
                player.sendMessage(help());
            } else if (args.length >= 1) {
                commands_gestion(args, player);
            }
            return true;
        }
        return false;
    }
}