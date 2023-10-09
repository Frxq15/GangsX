package me.frxq.gangsx.formatting.color.colorformatter;

import net.md_5.bungee.api.ChatColor;
import me.frxq.gangsx.formatting.color.ColorFormatter;

public class ColorFormatter_LEGACY implements ColorFormatter {

    @Override
    public String format(String string) {

        return ChatColor.translateAlternateColorCodes('&', string);
    }

}