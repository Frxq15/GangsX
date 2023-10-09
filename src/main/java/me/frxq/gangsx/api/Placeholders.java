package me.frxq.gangsx.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {
    private GangsX plugin;

    public Placeholders(GangsX plugin) {
        this.plugin = plugin;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "gangsx";
    }

    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (player == null) {
            return "Invalid Player";
        }
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());

        if (gang == null) return "No Gang";
        switch (identifier) {

            // GANG PLACEHOLDERS

            case "gang", "gangname":
                return gang.getName();
            case "leader", "owner":
                return Bukkit.getOfflinePlayer(gang.getLeader()).getName();
            case "level":
                return String.valueOf(gang.getLevel());
            case "coins":
                return String.valueOf(gang.getCoins());
            case "coins_formatted":
                return String.format("%,d", gang.getCoins());
            case "balance":
                return String.valueOf(gang.getBankBalance());
            case "balance_formatted":
                return String.format("%,d", gang.getBankBalance());
            case "kills":
                return String.valueOf(gang.getKills());
            case "deaths":
                return String.valueOf(gang.getDeaths());
            case "friendlyFire":
                return String.valueOf(gang.hasFriendlyFire());
            case "members":
                return String.valueOf(gang.getMembersCount());
            case "members_online":
                return String.valueOf(gang.getOnlineMembers().size());
            case "members_list": // will sort out formatting for this later
                return gang.getMembers().toString();

             // PLAYER PLACEHOLDERS

            case "player_kills":
                return String.valueOf(gPlayer.getKills());
            case "player_deaths":
                return String.valueOf(gPlayer.getDeaths());
            case "player_role":
                return gPlayer.getRole().getName();

                /* PLUGIN PLACEHOLDERS MAYBE?

            case "plugin_total_gangs":
                return sql query to get amount of gangs;

            // GANG TOP PLACEHOLDERS

                 */

            default:
                return null;
        }
    }
}