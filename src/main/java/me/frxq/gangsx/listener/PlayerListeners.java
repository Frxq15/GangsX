package me.frxq.gangsx.listener;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListeners implements Listener {
    private GangsX plugin = GangsX.getInstance();

    @EventHandler
    public void onGangChat(AsyncPlayerChatEvent e) {
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(e.getPlayer().getUniqueId());

        if(gPlayer.hasGang()) {
            if(gPlayer.hasChatEnabled()) {
                Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
                e.setCancelled(true);
                String message = e.getMessage();
                gang.sendMessage(plugin.getConfig().getString("gang.chat_format")
                        .replace("%role%", gPlayer.getRole().getRolePrefix())
                        .replace("%player%", e.getPlayer().getName())
                        .replace("%message%", message));
            }
        }
    }
}
