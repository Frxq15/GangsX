package net.withery.gangsx.leaderboard;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;

public class LeaderboardManager {

    private final GangsX plugin;
    public LinkedHashMap<Integer, Gang> top_values = new LinkedHashMap<>();

    public LeaderboardManager(GangsX plugin) {
        this.plugin = plugin;
    }
    public void updateTopValues(LinkedHashMap<Integer, Gang> top_values) {
        this.top_values = top_values;
    }
    public Gang getGangByPosition(int position) {
        return this.top_values.get(position);
    }
    public int startUpdateTask() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            plugin.getGangDataFactory().updateLeaderboardTopValues();
        }, 20L * 60L * plugin.getConfig().getInt("gang.leaderboard-interval"), 20L * 60L * plugin.getConfig().getInt("gang.leaderboar-interval")).getTaskId();
    }
}
