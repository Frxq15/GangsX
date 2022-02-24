package net.withery.gangsx.datafactory.gang.sql;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SQLGangListeners implements Listener {
    private final GangsX plugin;
    public SQLGangListeners(GangsX plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        GPlayer gPlayer = GPlayer.getPlayerData(e.getPlayer().getUniqueId());
        Gang gang = gPlayer.getGang();
        gang.addOnlineMember(gPlayer);
    }
}
