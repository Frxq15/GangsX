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
import java.util.UUID;

public class kickCommand extends SubCommand {
    private final GangsX plugin;

    public kickCommand(GangsX plugin) {
        super("kick", "gangsx.command.kick", "/gang kick <player>", Arrays.asList("remove"));
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

            if(p.getName().equalsIgnoreCase(args[0])) {
                plugin.getLocaleManager().sendMessage(p, "CANNOT_PERFORM_ACTION_ON_SELF");
                return;
            }

            GPlayer tPlayer = plugin.getGPlayerDataFactory().getGPlayerData(uuid);

            if(tPlayer.getGangId() == null || !tPlayer.getGangId().equals(gPlayer.getGangId())) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_YOUR_GANG");
                return;
            }
            tPlayer.kickFromGang();
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());

            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_PLAYER_KICKED")
                    .replace("%player%", p.getName())
                    .replace("%target%", tPlayer.getName()));

            if(Bukkit.getPlayer(tPlayer.getName()) != null) {
                Player target = Bukkit.getPlayer(tPlayer.getName());
                target.sendMessage(plugin.getLocaleManager().getMessage("PLAYER_GANG_KICKED")
                        .replace("%player%", p.getName()));
            }
            if(plugin.getConfig().getBoolean("gang.player_actions.kicked_from_gang")) {
                 Bukkit.broadcastMessage(plugin.getLocaleManager().getMessage("BROADCAST_GANG_KICKED")
                         .replace("%player%", p.getName())
                         .replace("%target%", tPlayer.getName())
                         .replace("%gang%", gang.getName()));
            }
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang kick <player>");
        return;
    }
}
