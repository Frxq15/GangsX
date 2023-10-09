package me.frxq.gangsx.command.commands;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.ParentCommand;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.command.commands.subcommands.gang.*;
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
        register(new valueCommand(plugin));
        register(new onlineCommand(plugin));
        register(new topCommand(plugin));
        register(new disbandCommand(plugin));
        register(new renameCommand(plugin));
        register(new chatCommand(plugin));
        register(new permissionsCommand(plugin));
        register(new InvseeCommand(plugin));
        register(new friendlyFireCommand(plugin));
        register(new promoteCommand(plugin));
        register(new demoteCommand(plugin));
        register(new levelCommand(plugin));
        register(new levelupCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        if (args.length == 0) {
            for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_MAIN")) {
                plugin.getLocaleManager().sendRawMessage(sender, lines);
            }
            return true;
        }
        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!exists(subLabel)) {
            plugin.getLocaleManager().sendMessage(sender, "SUB_COMMAND_NOT_FOUND");
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
