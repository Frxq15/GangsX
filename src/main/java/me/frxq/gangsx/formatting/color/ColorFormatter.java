package me.frxq.gangsx.formatting.color;

import net.md_5.bungee.api.ChatColor;

public class ColorFormatter {

    public String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}