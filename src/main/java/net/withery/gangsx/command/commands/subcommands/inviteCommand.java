package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class inviteCommand extends SubCommand {
    private final GangsX plugin;

    public inviteCommand(GangsX plugin) {
        super("invite", "gangsx.command.invite", "/gang invite <player>", Arrays.asList("inv"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if(!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return;
        }
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) == null) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_FOUND");
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            GPlayer tPlayer = plugin.getGPlayerDataFactory().getGPlayerData(target.getUniqueId());

            if(tPlayer.hasGang()) {
                plugin.getLocaleManager().sendMessage(p, "TARGET_ALREADY_IN_GANG");
                return;
            }
            if(gang.getInvites().contains(tPlayer)) {
                plugin.getLocaleManager().sendMessage(p, "TARGET_ALREADY_INVITED");
                return;
            }
            gang.inviteMember(tPlayer);
            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_PLAYER_INVITED")
                    .replace("%player%", p.getName())
                    .replace("%target%", target.getName()));

            target.sendMessage(plugin.getLocaleManager().getMessage("PLAYER_GANG_INVITED")
                    .replace("%player%", p.getName())
                    .replace("%gang%", gang.getName()));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang invite <player>");
        return;
    }
}
