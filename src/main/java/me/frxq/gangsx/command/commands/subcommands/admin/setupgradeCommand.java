package me.frxq.gangsx.command.commands.subcommands.admin;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.enums.Upgrade;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class setupgradeCommand extends SubCommand {
    private final GangsX plugin;

    public setupgradeCommand(GangsX plugin) {
        super("setupgrade", "gangsx.command.admin.setupgrade", "/gadmin setupgrade <gang> <upgrade> <value>",
                Arrays.asList("supgrade", "upgrade"));
        this.plugin = plugin;
    }
    @Override
    public @NotNull void onCommand(@NotNull CommandSender s, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 3) {
            String g = args[0];
            String u = args[1];

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if(!plugin.getGangDataFactory().doesGangNameExist(g)) {
                    plugin.getLocaleManager().sendMessage(s, "GANG_NOT_FOUND");
                    return;
                }
            });

            if(Upgrade.findByValue(u.toLowerCase()) == null) {
                plugin.getLocaleManager().sendUsageMessage(s, "&cInvalid upgrade specified, " +
                        "Available Types: member_limit, bank_limit, coin_multiplier, shop_discount, coloured_description, max_allies");
                return;
            }
                try {
                    Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    plugin.getLocaleManager().sendMessage(s, "INVALID_INPUT");
                    return;
                }
                int value = Integer.parseInt(args[2]);

                //inputs validated

            UUID uuid = plugin.getGangDataFactory().getGangUniqueId(g);
            Gang gang = plugin.getGangDataFactory().getGangData(uuid);
            gang.updateUpgrade(Upgrade.valueOf(u), value);
            s.sendMessage(plugin.getLocaleManager().getMessage("GANG_ADMIN_UPGRADE_APPLIED")
                    .replace("%gang%", gang.getName()).replace("%upgrade%", u.toUpperCase()).replace("%value%", value+""));

            return;
        }
        plugin.getLocaleManager().sendUsageMessage(s, "&cUsage: /gadmin setupgrade <gang> <upgrade> <value>");
    }
}
