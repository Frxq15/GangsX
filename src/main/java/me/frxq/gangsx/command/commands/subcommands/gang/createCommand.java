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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class createCommand extends SubCommand {
    private final GangsX plugin;

    public createCommand(GangsX plugin) {
        super("create", "gangsx.command.help", "/gang create <name>", Arrays.asList("new", "start"));
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
            String name = args[0];

            if(name.length() < plugin.getConfig().getInt("gang.name_min_length")
                    || name.length() > plugin.getConfig().getInt("gang.name_max_length")) {
                plugin.getLocaleManager().sendMessage(p, "INVALID_NAME_LENGTH");
                return;
            }

            if(!plugin.getGangUtils().matchesRegex(name)) {
                plugin.getLocaleManager().sendMessage(p, "INVALID_INPUT");
                return;
            }
            if(plugin.getCommandUtils().isBlacklistedName(name)) {
                plugin.getLocaleManager().sendMessage(p, "BLACKLISTED_WORDS");
                return;
            }
            if(plugin.getGangDataFactory().doesGangNameExist(name)) {
                plugin.getLocaleManager().sendMessage(p, "GANG_ALREADY_EXISTS");
                return;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    UUID gangId = UUID.randomUUID();
                    while (plugin.getGangDataFactory().doesGangDataExist(gangId)) {
                        gangId = UUID.randomUUID();
                    }
                    Gang gang = new Gang(plugin, gangId, name, p.getUniqueId());
                    plugin.getGangDataFactory().initializeGangData(gang);
                    gPlayer.setGangId(gang.getID());
                    gPlayer.setHasGang(true);
                    gPlayer.setRole(Role.LEADER);
                    Bukkit.broadcastMessage(plugin.getLocaleManager().getMessage("GANG_CREATED")
                            .replace("%gang%", gang.getName())
                            .replace("%player%", p.getName()));
                    p.sendMessage(plugin.getLocaleManager().getMessage("PLAYER_GANG_CREATED")
                            .replace("%gang%", gang.getName()));
                    return;
                }

            }.runTaskAsynchronously(plugin);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang create <name>");
        return;
    }
}
