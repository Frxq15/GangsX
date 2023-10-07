package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.enums.Permission;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class renameCommand extends SubCommand {
    private final GangsX plugin;

    public renameCommand(GangsX plugin) {
        super("rename", "gangsx.command.rename", "/gang rename <name>", Arrays.asList("setname", "changename"));
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
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.RENAME_GANG)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if(args.length == 1) {
            String name = args[0];

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
            if(gang.hasRenameCooldown()) {
                plugin.getLocaleManager().sendMessage(p, "GANG_COMMAND_ON_COOLDOWN");
                return;
            }
            int cooldown = plugin.getConfig().getInt("gang.command_cooldowns.rename");

            if(plugin.getConfig().getBoolean("gang.player_actions.rename_gang")) {
                Bukkit.broadcastMessage(plugin.getLocaleManager().getMessage("BROADCAST_GANG_RENAME")
                        .replace("%gang%", gang.getName()).replace("%new%", name));
            }
            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_RENAMED")
                    .replace("%player%", p.getName())
                    .replace("%new%", name));
            gang.rename(name);
            gang.setRenameCooldown(true);
            Bukkit.getScheduler().runTaskLater(plugin, () -> gang.setRenameCooldown(false), 20L * cooldown);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang rename <name>");
    }
}
