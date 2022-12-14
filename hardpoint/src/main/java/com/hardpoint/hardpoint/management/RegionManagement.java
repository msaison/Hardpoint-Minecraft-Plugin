package com.hardpoint.hardpoint.management;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.LegacyWorldData;


/**
 * RegionManagement
 */
public class RegionManagement{
    private WorldEditPlugin _wep;
    private BukkitPlayer _actor;
    private LocalSession _localSession;
    private EditSession _editSession;

    public RegionManagement(org.bukkit.entity.Player player){
        _wep = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        _localSession = _wep.getSession(player);
        _actor = _wep.wrapPlayer(player);
        _editSession = _localSession.createEditSession(_actor);
    }


    public boolean saveSchematic(org.bukkit.entity.Player p, String schematicName, File dir) {
        Closer closer = Closer.create();
        try {
            Region region = _localSession.getSelection(_actor.getWorld());
            Clipboard cb = new BlockArrayClipboard(region);
            ForwardExtentCopy copy = new ForwardExtentCopy(_editSession, region, cb, region.getMinimumPoint());
            Operations.completeLegacy(copy);
            File schematicFile = new File(dir, schematicName + ".schematics");
            schematicFile.createNewFile();

            FileOutputStream fos = closer.register(new FileOutputStream(schematicFile));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bos));
            writer.write(cb, LegacyWorldData.getInstance());
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        } finally {
            try {
                closer.close();
            } catch (IOException ignore) {
            }
        }
        return false;
    }


    public Region getRegion() {
        try {
            return _localSession.getSelection(_actor.getWorld());
        } catch (IncompleteRegionException e) {
            System.out.println("Incomplete region");
            return null;
        }
    }

    // public void saveRegion(Plugin plugin, String name, BlockArrayClipboard clipboard, WorldData dWorldData){
    //     File file = new File(plugin.getDataFolder() + File.separator + "schematics", name) /* figure out where to save the clipboard */;

    //     try (ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(new FileOutputStream(file))) {
    //         writer.write(clipboard);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    // WORLD EDIT
    // private void SelectMap(Player player) {
    // player.sendMessage("Oui");
    // BukkitPlayer actor = BukkitAdapter.adapt(player);

    // Region region; // declare the region variable
    // // note: not necessarily the player's current world, see the concepts page
    // World selectionWorld = localSession.getSelectionWorld();

    // try {
    // if (selectionWorld == null)
    // throw new IncompleteRegionException();
    // region = localSession.getSelection(selectionWorld);
    // } catch (IncompleteRegionException ex) {
    // actor.printError("Please make a region selection first.");
    // return;
    // }

}