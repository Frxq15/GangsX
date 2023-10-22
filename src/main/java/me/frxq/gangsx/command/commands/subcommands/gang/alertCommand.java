package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class alertCommand extends SubCommand {
    private final GangsX plugin;

    public alertCommand(GangsX plugin) {
        super("alert", "gangsx.command.alert", "/gang alert", Arrays.asList("helpme", "sendlocation"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player)sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if(!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return;
        }
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.ALERT)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if(args.length == 0) {
            if(gPlayer.isOnAlertCooldown()) {
                plugin.getLocaleManager().sendMessage(p, "PLAYER_COMMAND_ON_COOLDOWN");
                return;
            }

            gPlayer.applyAlertCooldown();
            int cooldown = plugin.getConfig().getInt("gang.command_cooldowns.alert");

            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_ALERT")
                    .replace("%player%", p.getName()).replace("%world%", p.getWorld().getName())
                    .replace("%x%", format(p.getLocation().getX()+"")).replace("%y%", format(p.getLocation().getY()+""))
                    .replace("%z%", format(p.getLocation().getZ()+"")));

            Bukkit.getScheduler().runTaskLater(plugin, gPlayer::removeAlertCooldown, 20L * cooldown);

            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang alert");
    }
    public String format(String type) {
        return type.substring(0, type.indexOf("."));
    }
}
