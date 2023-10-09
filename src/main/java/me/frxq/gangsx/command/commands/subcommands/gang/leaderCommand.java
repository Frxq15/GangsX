package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.enums.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class leaderCommand extends SubCommand {
    private final GangsX plugin;

    public leaderCommand(GangsX plugin) {
        super("leader", "gangsx.command.leader", "/gang leader <player>", Arrays.asList("setleader"));
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
        if(args.length == 1) {
            UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();

            if(plugin.getGPlayerDataFactory().getGPlayerData(uuid) == null) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_FOUND");
                return;
            }

            GPlayer tPlayer = plugin.getGPlayerDataFactory().getGPlayerData(uuid);

            if(tPlayer.getGangId() == null || !tPlayer.getGangId().equals(gPlayer.getGangId())) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_YOUR_GANG");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            gang.setLeader(uuid);
            tPlayer.setRole(Role.LEADER);
            gPlayer.setRole(Role.CO_LEADER);

            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_LEADER_CHANGED")
                    .replace("%player%", p.getName())
                    .replace("%target%", tPlayer.getName()));

            if(Bukkit.getPlayer(tPlayer.getName()) != null) {
                Player target = Bukkit.getPlayer(tPlayer.getName());
                target.sendMessage(plugin.getLocaleManager().getMessage("PLAYER_GIVEN_LEADER")
                        .replace("%player%", p.getName()));
            }

            if(plugin.getConfig().getBoolean("gang.player_actions.leader_changed")) {
                Bukkit.broadcastMessage(plugin.getLocaleManager().getMessage("BROADCAST_GANG_LEADER_CHANGED")
                        .replace("%player%", p.getName())
                        .replace("%target%", tPlayer.getName())
                        .replace("%gang%", gang.getName()));
            }
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang leader <player>");
        return;
    }
}
