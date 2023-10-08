package net.withery.gangsx.utils;

import net.withery.gangsx.GangsX;

import java.text.DecimalFormat;

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
}
