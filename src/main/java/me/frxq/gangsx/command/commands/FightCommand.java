package me.frxq.gangsx.command.commands;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.ParentCommand;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.command.commands.subcommands.gang.*;
import me.frxq.gangsx.objects.GPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class FightCommand extends ParentCommand {

    public FightCommand(GangsX plugin) {
        super(plugin, "fight", "gangsx.command.fight");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        if (args.length == 0) {
            for(String lines : plugin.getLocaleManager().getLocaleFile().getStringList("GANG_HELP_FIGHT_MAIN")) {
                plugin.getLocaleManager().sendRawMessage(sender, lines);
            }
            return true;
        }
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return true;
        }
        Player p = (Player)sender;
        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if(!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return true;
        }

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
