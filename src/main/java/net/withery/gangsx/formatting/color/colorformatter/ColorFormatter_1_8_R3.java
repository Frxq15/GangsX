package net.withery.gangsx.formatting.color.colorformatter;

import net.md_5.bungee.api.ChatColor;
import net.withery.gangsx.formatting.color.ColorFormatter;

public class ColorFormatter_1_8_R3 implements ColorFormatter {

    @Override
    public String format(String string) {

        return ChatColor.translateAlternateColorCodes('&', string);
    }

}