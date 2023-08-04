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

public class alertCommand extends SubCommand {
    private final GangsX plugin;

    public alertCommand(GangsX plugin) {
        super("alert", "gangsx.command.alert", "/gang alert", Arrays.asList("helpme", "sendlocation"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if(!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return;
        }
        if(args.length == 0) {
            if(gPlayer.isOnAlertCooldown()) {
                plugin.getLocaleManager().sendMessage(p, "GANG_COMMAND_ON_COOLDOWN");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
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
