package net.withery.gangsx.utils;

import net.withery.gangsx.GangsX;

public class CommandUtils {
    private GangsX plugin = GangsX.getInstance();

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
}
