package me.frxq.gangsx.utils;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CommandUtils {
    private GangsX plugin = GangsX.getInstance();
    private String[] suffix = new String[]{"","k", "M", "B", "T"};
    private int MAX_LENGTH = 4;

    public String getFinalArg(String[] args, int start) {
        StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
    public boolean containsBlacklistedWords(String string) {
        for(String words : plugin.getConfig().getStringList("gang.blacklisted_words")) {
            if (string.equalsIgnoreCase(words) || string.contains(words)) {
                return true;
                }
            }
        return false;
        }
    public boolean isBlacklistedName(String string) {
        for(String words : plugin.getConfig().getStringList("gang.blacklisted_names")) {
            if (string.equalsIgnoreCase(words) || string.contains(words)) {
                return true;
            }
        }
        return false;
    }
    public String formatNumber(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
            r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
        }
        return r;
    }
    public boolean isRosterOnline(String arg) {
        AtomicBoolean status = new AtomicBoolean(true);
        String[] provided = arg.split(",");
        Arrays.stream(provided).forEach(member -> {
            if(Bukkit.getPlayer(member) == null) {
                status.set(false);
            }
        });
        status.set(true);
        return status.get();
    }
    public boolean isRosterInGang(String arg, Gang gang) {
        List<GPlayer> members = gang.getOnlineMembers();
        AtomicBoolean status = new AtomicBoolean(true);
        String[] provided = arg.split(",");
        Arrays.stream(provided).forEach(member -> {
            Player p = Bukkit.getPlayer(member);
            GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
            if(!members.contains(gPlayer)) {
                status.set(false);
            }
        });
        status.set(true);
        return status.get();
    }
}
