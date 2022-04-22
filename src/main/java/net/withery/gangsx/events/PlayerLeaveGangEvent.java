package net.withery.gangsx.events;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveGangEvent extends Event implements Cancellable {
    private final GangsX plugin;
    private final Player player;
    private final Gang gang;
    private boolean cancelled;

    public PlayerLeaveGangEvent(GangsX plugin, Player player, Gang gang) {
        this.plugin = plugin;
        this.player = player;
        this.gang = gang;
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    public Gang getGang() {
        return gang;
    }
}