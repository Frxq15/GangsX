package me.frxq.gangsx.listener;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class DataFactoryListener implements Listener {

    private final GangsX plugin;

    public DataFactoryListener(GangsX plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();

        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(uuid);
        if (gPlayer == null) {
            if (plugin.getGPlayerDataFactory().doesGPlayerDataExist(uuid)) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("An error occurred, please contact an admin!");
                return;
            }

            gPlayer = new GPlayer(plugin, uuid, name);
            plugin.getGPlayerDataFactory().initializeGPlayerData(gPlayer);
        } else {
            gPlayer.setName(name);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(event.getPlayer().getUniqueId());
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if (gang != null)
            gang.addOnlineMember(gPlayer);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(player.getUniqueId());

        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if (gang != null) {
            gang.removeOnlineMember(gPlayer);
            if (gang.checkUnload())
                plugin.getGangDataFactory().unloadGangDataAsync(gang.getID());
        }

        plugin.getGPlayerDataFactory().unloadGPlayerDataAsync(player.getUniqueId());
    }

}