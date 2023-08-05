package net.withery.gangsx.utils;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GangUtils {
    private final GangsX plugin;
    public GangUtils(GangsX plugin) {
        this.plugin = plugin;
    }
    public void prepareDisband(Gang gang) {
        gang.disband();
        plugin.getGangDataFactory().unloadGangData(gang.getID());
        plugin.getGangDataFactory().deleteGangData(gang.getID());
    }
    public boolean matchesRegex(String input) {
        String regex = plugin.getConfig().getString("gang.name_regex");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(!matcher.matches()) {
            return false;
        }
        return true;
    }
}
