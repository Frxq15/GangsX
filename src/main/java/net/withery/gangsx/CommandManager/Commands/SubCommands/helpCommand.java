package net.withery.gangsx.CommandManager.Commands.SubCommands;

import net.withery.gangsx.CommandManager.SubCommand;
import net.withery.gangsx.GangsX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class helpCommand extends SubCommand {
    private final GangsX plugin;
    public helpCommand(GangsX plugin) {
        super("help", "gangsx.command.help", "help <page>", Arrays.asList("commands", "usage"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
