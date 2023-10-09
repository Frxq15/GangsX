package me.frxq.gangsx.utils;

import me.frxq.gangsx.GangsX;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaUtils {
    private GangsX plugin = GangsX.getInstance();
    private FileConfiguration arena = plugin.getFileManager().getArenaFile();

    public void setChallengerPosition(Location location) {
        arena.set("CHALLENGER_POSITION", location.serialize());
        plugin.getFileManager().saveArenaFile();
        plugin.getFileManager().reloadArenaFile();
    }
    public void setOpponentPosition(Location location) {
        arena.set("OPPONENT_POSITION", location.serialize());
        plugin.getFileManager().saveArenaFile();
        plugin.getFileManager().reloadArenaFile();
    }
    public void setArenaCenter(Location location) {
        arena.set("ARENA_CENTER", location.serialize());
        plugin.getFileManager().saveArenaFile();
        plugin.getFileManager().reloadArenaFile();
    }
    public Location getArenaCenter() {
        Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("ARENA_CENTER.world")),
                plugin.getConfig().getDouble("ARENA_CENTER.x"),
                plugin.getConfig().getDouble("ARENA_CENTER.y"),
                plugin.getConfig().getDouble("ARENA_CENTER.z"),
                (float)plugin.getConfig().getDouble("ARENA_CENTER.yaw"),
                (float)plugin.getConfig().getDouble("ARENA_CENTER.pitch"));
        return loc;
    }
    public boolean isArenaCenterSet() {
        if(plugin.getConfig().getString("ARENA_CENTER.world") == null) {
            return false;
        }
        return true;
    }
}
