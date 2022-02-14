package net.withery.gangsx.formatting.color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFormatter_1_16_R1 implements ColorFormatter{

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");

    @Override
    public String format(String string) {
        // To be tested
        Matcher matcher = HEX_PATTERN.matcher(string);

        return null;
    }

}