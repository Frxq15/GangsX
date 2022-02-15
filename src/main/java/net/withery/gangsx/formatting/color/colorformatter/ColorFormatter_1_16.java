package net.withery.gangsx.formatting.color.colorformatter;

import net.md_5.bungee.api.ChatColor;
import net.withery.gangsx.formatting.color.ColorFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFormatter_1_16 implements ColorFormatter {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");

    @Override
    public String format(String string) {
        // To be tested
        string = ChatColor.translateAlternateColorCodes('&', string);
        Matcher matcher = HEX_PATTERN.matcher(string);

        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            String hex = "#" + matcher.group(1); // '#' not covered in RegEx pattern group
            ChatColor chatColor = ChatColor.of(hex);

            if (chatColor == null) continue;
            matcher.appendReplacement(builder, Matcher.quoteReplacement(String.valueOf(chatColor)));
        }

        matcher.appendTail(builder);
        return builder.toString();
    }

}