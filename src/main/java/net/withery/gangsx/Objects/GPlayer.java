package net.withery.gangsx.Objects;

import net.withery.gangsx.GangsX;

import java.util.UUID;

public class GPlayer {
    private final GangsX plugin;
    private final UUID uuid;

    private Gang gang;

    public GPlayer(GangsX plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }


}
