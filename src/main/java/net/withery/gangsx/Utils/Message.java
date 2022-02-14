package net.withery.gangsx.Utils;

import net.withery.gangsx.GangsX;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private final GangsX plugin;

    public Message(GangsX plugin) {
        this.plugin = plugin;
    }
    public String colourize(String text) {
        if(Integer.parseInt(plugin.getSettings().getVersion()) < 1.16) {
            return ChatColor.translateAlternateColorCodes('&', text);
        }
        return translateFromHex(text);
    }
    public static String translateFromHex(String string) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            String color = string.substring(matcher.start(), matcher.end());
            string = string.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }
        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }
}
