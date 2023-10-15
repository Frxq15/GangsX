package me.frxq.gangsx.command.commands;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.ParentCommand;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.command.commands.subcommands.arena.createArenaCommand;
import me.frxq.gangsx.command.commands.subcommands.arena.setPositionCommand;
import me.frxq.gangsx.command.commands.subcommands.arena.viewArenaCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GangArenaCommand extends ParentCommand {

    public GangArenaCommand(GangsX plugin) {
        super(plugin, "garena", "gangsx.command.gangarena");
        register(new setPositionCommand(plugin));
        register(new viewArenaCommand(plugin));
        register(new createArenaCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        if (args.length == 0) {
            for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_ADMIN_MAIN")) {
                plugin.getLocaleManager().sendRawMessage(sender, lines);
            }
            return true;
        }
        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!exists(subLabel)) {
            for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_ADMIN_MAIN")) {
                plugin.getLocaleManager().sendRawMessage(sender, lines);
            }
            return true;
        }
        SubCommand subCommand = getExecutor(subLabel);

        if (!sender.hasPermission(subCommand.getPermission())) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        subCommand.onCommand(sender, command, subLabel, subArgs);
        return true;
    }
}
