package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.enums.Role;
import net.withery.gangsx.managers.RoleManager;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class demoteCommand extends SubCommand {
    private final GangsX plugin;

    public demoteCommand(GangsX plugin) {
        super("demote", "gangsx.command.demote", "/gang demote <player>", null);
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

            if (plugin.getGPlayerDataFactory().getGPlayerData(uuid) == null) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_FOUND");
                return;
            }

            if (p.getName().equalsIgnoreCase(args[0])) {
                plugin.getLocaleManager().sendMessage(p, "CANNOT_PERFORM_ACTION_ON_SELF");
                return;
            }

            GPlayer tPlayer = plugin.getGPlayerDataFactory().getGPlayerData(uuid);

            if (tPlayer.getGangId() == null || !tPlayer.getGangId().equals(gPlayer.getGangId())) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_YOUR_GANG");
                return;
            }
            if(tPlayer.getRole() == Role.LEADER) {
                plugin.getLocaleManager().sendMessage(p, "CANNOT_PERFORM_ACTION_ON_OTHER");
                return;
            }
            if(!plugin.getRoleManager().canDemote(gPlayer, tPlayer)) {
                plugin.getLocaleManager().sendMessage(p, "CANNOT_PERFORM_ACTION_ON_OTHER");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_PLAYER_DEMOTED")
                    .replace("%target%", tPlayer.getName())
                    .replace("%role%", plugin.getRoleManager().getNextRole(tPlayer).getName().toLowerCase())
                    .replace("%player%", p.getName()));

            if(Bukkit.getPlayer(tPlayer.getName()) != null) {
                Bukkit.getPlayer(tPlayer.getName()).sendMessage(plugin.getLocaleManager().getMessage("PLAYER_DEMOTED")
                        .replace("%target%", tPlayer.getName())
                        .replace("%role%", plugin.getRoleManager().getNextRole(tPlayer).getName().toLowerCase())
                        .replace("%player%", p.getName()));
            }
            plugin.getRoleManager().demotePlayer(tPlayer);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang demote <player>");
    }
}
