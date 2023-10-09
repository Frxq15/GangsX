package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.enums.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class joinCommand extends SubCommand {
    private final GangsX plugin;

    public joinCommand(GangsX plugin) {
        super("join", "gangsx.command.join", "/gang join <gang>", Arrays.asList("j"));
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
        if(gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_ALREADY_IN_GANG");
            return;
        }
        if(args.length == 1) {
            String target = args[0];

            if(!plugin.getGangDataFactory().doesGangNameExist(target)) {
                plugin.getLocaleManager().sendMessage(p, "GANG_NOT_FOUND");
                return;
            }
            UUID gangId = plugin.getGangDataFactory().getGangUniqueId(target);
            Gang gang = plugin.getGangDataFactory().getGangData(gangId);

            if(!gang.getInvites().contains(gPlayer)) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NO_INVITE_PENDING");
                return;
            }
            gPlayer.setGangId(gangId);
            gPlayer.setHasGang(true);
            gPlayer.setRole(Role.RECRUIT);
            gang.addMember(gPlayer);
            gang.addOnlineMember(gPlayer);
            if(plugin.getConfig().getBoolean("gang.player_actions.join_gang")) {
                plugin.getLocaleManager().broadcastMessage(plugin.getLocaleManager().getMessage("BROADCAST_GANG_JOINED")
                        .replace("%player%", p.getName())
                        .replace("%gang%", gang.getName()));
            }
            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_PLAYER_JOINED").replace("%player%", p.getName()));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang join <gang>");
    }
}
