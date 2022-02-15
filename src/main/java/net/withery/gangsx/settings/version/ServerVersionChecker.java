package net.withery.gangsx.settings.version;

import org.bukkit.Bukkit;

public class ServerVersionChecker {

    private ServerVersion version;

    public ServerVersionChecker() {
        checkVersion();
    }

    public boolean isServerAtLeast(ServerVersion version) {
        return this.version.ordinal() >= version.ordinal();
    }

    public ServerVersion getVersion() {
        return version;
    }

    private void checkVersion() {
        String versionString = Bukkit.getBukkitVersion().split("-")[0];

        switch (versionString) {
            case "1.8", "1.8.1", "1.8.2", "1.8.3", "1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8", "1.8.9" -> version = ServerVersion.VERSION_1_8;
            case "1.9", "1.9.1", "1.9.2", "1.9.3", "1.9.4" -> version = ServerVersion.VERSION_1_9;
            case "1.10", "1.10.1", "1.10.2" -> version = ServerVersion.VERSION_1_10;
            case "1.11", "1.11.1", "1.11.2" -> version = ServerVersion.VERSION_1_11;
            case "1.12", "1.12.1", "1.12.2" -> version = ServerVersion.VERSION_1_12;
            case "1.13", "1.13.1", "1.13.2" -> version = ServerVersion.VERSION_1_13;
            case "1.14", "1.14.1", "1.14.2", "1.14.3", "1.14.4" -> version = ServerVersion.VERSION_1_14;
            case "1.15", "1.15.1", "1.15.2" -> version = ServerVersion.VERSION_1_15;
            case "1.16", "1.16.1", "1.16.2", "1.16.3", "1.16.4", "1.16.5" -> version = ServerVersion.VERSION_1_16;
            case "1.17", "1.17.1" -> version = ServerVersion.VERSION_1_17;
            case "1.18", "1.18.1" -> version = ServerVersion.VERSION_1_18;

            default -> {
                // Checking if version-number starts with "1.", if so, get the 2nd number and check if it's smaller than 8 (1.8) and thus legacy
                if (versionString.startsWith("1.") && (Integer.parseInt(versionString.subSequence(versionString.indexOf(".") + 1, (versionString.indexOf(".", versionString.indexOf(".") + 1) == -1 ? versionString.length() : versionString.indexOf(".", versionString.indexOf(".") + 1))).toString()) < 8)) {
                    version = ServerVersion.LEGACY;
                    // Might throw warning for unsupported legacy version
                }

                // If none of the checks above are true, the version is most likely newer than the latest one registered (or no full-release)
                else {
                    version = ServerVersion.NEWER;
                    // Might throw warning for unsupported newer version
                }
            }
        }
    }

}