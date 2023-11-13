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

        if(identifier.startsWith("top_name")){
           String pos = identifier.replace("top_name_", "");
            if(Integer.parseInt(pos) < plugin.getConfig().getInt("gang.leaderboard-data-pull-amount")){
                int p = Integer.parseInt(pos);
                return String.valueOf(plugin.getLeaderboardManager().getGangByPosition(p));
            } else return "None";
        }
        if(identifier.startsWith("top_points")){
            String pos = identifier.replace("top_value_", "");
            if(Integer.parseInt(pos) < plugin.getConfig().getInt("gang.leaderboard-data-pull-amount")){
                int p = Integer.parseInt(pos);
                return String.valueOf(plugin.getLeaderboardManager().getGangByPosition(p).getPoints());
            } else return "None";
        }

        if(identifier.startsWith("gang")){
            switch (identifier) {

                // GANG PLACEHOLDERS

                case "name":
                    if (gang == null) return "None";
                    return gang.getName();
                case "leader", "owner":
                    if (gang == null) return "N/A";
                    return Bukkit.getOfflinePlayer(gang.getLeader()).getName();
                case "level":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getLevel());
                case "coins":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getCoins());
                case "coins_formatted":
                    if (gang == null) return "0";
                    return String.format("%,d", gang.getCoins());
                case "balance":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getBankBalance());
                case "balance_formatted":
                    if (gang == null) return "0";
                    return String.format("%,d", gang.getBankBalance());
                case "kills":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getKills());
                case "blocksmined":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getBlocksBroken());
                case "blocksmined_formatted":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getBlocksMinedFormatted());
                case "deaths":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getDeaths());
                case "friendlyFire":
                    if (gang == null) return "false";
                    return String.valueOf(gang.hasFriendlyFire());
                case "members":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getMembersCount());
                case "members_online":
                    if (gang == null) return "0";
                    return String.valueOf(gang.getOnlineMembers().size());
                case "members_list": // will sort out formatting for this later
                    if (gang == null) return "N/A";
                    return gang.getMembers().toString();
            }
        }
        switch (identifier) {

             // PLAYER PLACEHOLDERS

            case "player_kills":
                return String.valueOf(gPlayer.getKills());
            case "player_deaths":
                return String.valueOf(gPlayer.getDeaths());
            case "player_role":
                return gPlayer.getRole().getName();
            case "player_role_icon":
                return gPlayer.getRole().getRoleIcon();
            case "player_role_prefix":
                return gPlayer.getRole().getRolePrefix();
            case "player_has_gang":
                if (gang == null) return "false";
                return "true";



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
