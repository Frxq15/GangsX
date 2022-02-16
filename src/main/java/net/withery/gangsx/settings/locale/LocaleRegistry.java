package net.withery.gangsx.settings.locale;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.formatting.text.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.logging.Level;

public class LocaleRegistry {

    private final static String LOCALE_FILE_NAME = "en-us.yml";

    private final GangsX plugin;
    private final File localeFile;

    private FileConfiguration locale;

    public LocaleRegistry(GangsX plugin) {
        this.plugin = plugin;

        localeFile = new File(plugin.getDataFolder(), LOCALE_FILE_NAME);
    }

    public void sendMessage(@NotNull CommandSender sender, @NotNull LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages = TextFormatter.splitString(string);

        for (String message : messages)
            sender.sendMessage(message);
    }

    public void sendMessage(@NotNull CommandSender sender, @NotNull LocaleReference reference, String... variables) {
        sendMessage(sender, reference, true, variables);
    }

    public void sendMessage(CommandSender[] senders, LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages = TextFormatter.splitString(string);

        for (CommandSender sender : senders)
            for (String message : messages)
                sender.sendMessage(message);
    }

    public void sendMessage(CommandSender[] senders, LocaleReference reference, String... variables) {
        sendMessage(senders, reference, true, variables);
    }

    public void sendMessage(Collection<CommandSender> senders, LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages =  TextFormatter.splitString(string);

        for (CommandSender sender : senders)
            for (String message : messages)
                sender.sendMessage(message);
    }

    public void sendMessage(Collection<CommandSender> senders, LocaleReference reference, String... variables) {
        sendMessage(senders, reference, true, variables);
    }

    public void sendMessageToAll(LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages =  TextFormatter.splitString(string);

        for (CommandSender player : Bukkit.getOnlinePlayers())
            for (String message : messages)
                player.sendMessage(message);
    }

    public void sendMessageToAll(LocaleReference reference, String... variables) {
        sendMessageToAll(reference, true, variables);
    }

    public String getString(@NotNull LocaleReference reference, String... variables) {
        String string = locale.getString(reference.getReference(), reference.getDefaultValue());
        string = plugin.getColorFormatter().format(string);

        if (variables != null) {
            int i = 0;
            for (String variable : reference.getVariables()) {
                if (i >= variables.length) {
                    string = string.replace("%" + variable + "%", "");
                    continue;
                }

                string = string.replace("%" + variable + "%", variables[i++]);
            }
        }

        return string;
    }

    public void load() {
        if (!localeFile.exists()) {
            if (!localeFile.getParentFile().exists())
                if (!localeFile.getParentFile().mkdirs())
                    plugin.getLogger().log(Level.SEVERE, "An error occurred while creating a directory!");// plugin.getDebugger().log(Level.SEVERE, "An error occurred while creating a directory!", true); - Might add different logger soon

            plugin.saveResource(LOCALE_FILE_NAME, false);
        }

        locale = YamlConfiguration.loadConfiguration(localeFile);
    }

}