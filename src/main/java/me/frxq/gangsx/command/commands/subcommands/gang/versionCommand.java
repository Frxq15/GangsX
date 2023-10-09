package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
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
        sender.sendMessage(plugin.getLocaleManager().getMessage("PLUGIN_VERSION").replace("%version%", plugin.getDescription().getVersion()));
    }
}
