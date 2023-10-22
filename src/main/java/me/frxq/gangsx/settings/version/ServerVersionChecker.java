package me.frxq.gangsx.settings.version;

import org.bukkit.Bukkit;

public class ServerVersionChecker {

    private ServerVersion version;

    public ServerVersionChecker() {
        checkVersion();
    }

    public boolean isServerAbove(ServerVersion version) {
        return this.version.ordinal() >= version.ordinal();
    }

    public ServerVersion getVersion() {
        return version;
    }

    private void checkVersion() {
        String versionString = Bukkit.getBukkitVersion().split("-")[0];
        version = ServerVersion.LEGACY;
    }

}