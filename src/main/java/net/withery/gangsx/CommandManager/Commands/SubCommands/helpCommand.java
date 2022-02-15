package net.withery.gangsx.CommandManager.Commands.SubCommands;

import net.withery.gangsx.CommandManager.SubCommand;
import net.withery.gangsx.GangsX;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class helpCommand extends SubCommand {
    private final GangsX plugin;
    public helpCommand(GangsX plugin) {
        super("help", "gangsx.command.help", "help <page>", null);
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.broadcastMessage("help");
    }

}
