package net.withery.gangsx.command.Commands;

import net.withery.gangsx.command.Commands.SubCommands.helpCommand;
import net.withery.gangsx.command.Commands.SubCommands.testCommand;
import net.withery.gangsx.command.ParentCommand;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.GangsX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class gangCommand extends ParentCommand {

    public gangCommand(GangsX plugin) {
        super(plugin, "gang", "gangsx.command.gang");
        register(new helpCommand(plugin));
        register(new testCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission(permission)) {
            sender.sendMessage("no perms lul");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage("no args");
            return true;
        }

        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        if(!exists(subLabel)) {
            sender.sendMessage("sub cmd not found");
            return true;
        }
        SubCommand subCommand = getExecutor(subLabel);

        if(!sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage("no perms for sub command "+subCommand.getCommand());
            return true;
        }
        subCommand.onCommand(sender, command, subLabel, subArgs);
        return true;
    }
}
