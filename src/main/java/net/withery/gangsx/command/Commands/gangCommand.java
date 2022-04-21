package net.withery.gangsx.command.Commands;

import net.withery.gangsx.command.Commands.SubCommands.createCommand;
import net.withery.gangsx.command.Commands.SubCommands.helpCommand;
import net.withery.gangsx.command.Commands.SubCommands.testCommand;
import net.withery.gangsx.command.ParentCommand;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.GangsX;
import net.withery.gangsx.settings.locale.LocaleReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class gangCommand extends ParentCommand {

    public gangCommand(GangsX plugin) {
        super(plugin, "gang", "gangsx.command.gang");
        register(new helpCommand(plugin));
        register(new testCommand(plugin));
        register(new createCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission(permission)) {
            plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_NO_PERMISSION, "gangsx.command.gang");
            return true;
        }
        if(args.length == 0) {
            //change to help message later
            plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_WRONG_USAGE, "/gang <subcommand>");
            return true;
        }

        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        if(!exists(subLabel)) {
            plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_SUB_COMMAND_NOT_FOUND);
            return true;
        }
        SubCommand subCommand = getExecutor(subLabel);

        if(!sender.hasPermission(subCommand.getPermission())) {
            plugin.getLocaleRegistry().sendMessage(sender, LocaleReference.COMMAND_NO_PERMISSION, subCommand.getPermission());
            return true;
        }
        subCommand.onCommand(sender, command, subLabel, subArgs);
        return true;
    }
}
