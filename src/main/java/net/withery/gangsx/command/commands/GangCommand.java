package net.withery.gangsx.command.commands;

import net.withery.gangsx.command.commands.subcommands.*;
import net.withery.gangsx.command.ParentCommand;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.GangsX;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GangCommand extends ParentCommand {

    public GangCommand(GangsX plugin) {
        super(plugin, "gang", "gangsx.command.gang");
        register(new helpCommand(plugin));
        register(new testCommand(plugin));
        register(new createCommand(plugin));
        register(new versionCommand(plugin));
        register(new infoCommand(plugin));
        register(new shopCommand(plugin));
        register(new setdescriptionCommand(plugin));
        register(new inviteCommand(plugin));
        register(new joinCommand(plugin));
        register(new kickCommand(plugin));
        register(new depositCommand(plugin));
        register(new withdrawCommand(plugin));
        register(new leaderCommand(plugin));
        register(new alertCommand(plugin));
        register(new bankCommand(plugin));
        register(new coinsCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission(permission)) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        if(args.length == 0) {
            //change to help message later
            plugin.getLocaleManager().sendUsageMessage(sender, "&cUsage: /gang <subcommand>");
            return true;
        }
        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        if(!exists(subLabel)) {
            plugin.getLocaleManager().sendMessage(sender, "SUB_COMMAND_NOT_FOUND");
            return true;
        }
        SubCommand subCommand = getExecutor(subLabel);

        if(!sender.hasPermission(subCommand.getPermission())) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        subCommand.onCommand(sender, command, subLabel, subArgs);
        return true;
    }
}
