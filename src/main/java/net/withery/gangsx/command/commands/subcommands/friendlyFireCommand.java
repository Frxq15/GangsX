package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.enums.Permission;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class friendlyFireCommand extends SubCommand {
    private final GangsX plugin;

    public friendlyFireCommand(GangsX plugin) {
        super("friendlyfire", "gangsx.command.friendlyfire", "/gang friendlyfire", Arrays.asList("ff", "pvp"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if (!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return;
        }
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.MANAGE_FRIENDLY_FIRE)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if (args.length == 0) {
            if (gang.hasFriendlyFire()) {
                gang.setFriendlyFire(false);
                gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_FRIENDLYFIRE_OFF").replace("%player%", p.getName()));
                return;
            }
            gang.setFriendlyFire(true);
            gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_FRIENDLYFIRE_ON").replace("%player%", p.getName()));
            return;
        }
        if (args.length == 1) {
            String option = args[0];

            switch (option.toLowerCase()) {
                case "on":
                case "enable":
                    gang.setFriendlyFire(true);
                    gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_FRIENDLYFIRE_ON").replace("%player%", p.getName()));
                    return;
                case "off":
                case "disable":
                    gang.setFriendlyFire(false);
                    gang.sendMessage(plugin.getLocaleManager().getMessage("GANG_FRIENDLYFIRE_OFF").replace("%player%", p.getName()));
                    return;
                default:
                    plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang friendlyfire <option>");
                    return;
            }
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang friendlyfire <option>");
    }
}
