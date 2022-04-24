package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.settings.locale.LocaleReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class versionCommand extends SubCommand {
    private final GangsX plugin;

    public versionCommand(GangsX plugin) {
        super("version", "gangsx.command.version", "/gang version", Arrays.asList("ver", "v"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_PLUGIN_VERSION, plugin.getDescription().getVersion());
    }
}
