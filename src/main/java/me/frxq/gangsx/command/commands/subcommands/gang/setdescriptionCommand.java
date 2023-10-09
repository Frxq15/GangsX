package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class setdescriptionCommand extends SubCommand {
    private final GangsX plugin;

    public setdescriptionCommand(GangsX plugin) {
        super("setdescription", "gangsx.command.setdescription", "/gang setdescription <description>",
                Arrays.asList("setdesc", "description", "desc"));
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
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.CHANGE_DESCRIPTION)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if(args.length > 0) {
            String description = plugin.getCommandUtils().getFinalArg(args, 0);

            if(plugin.getCommandUtils().containsBlacklistedWords(description)) {
                plugin.getLocaleManager().sendMessage(p, "BLACKLISTED_WORDS");
                return;
            }
            if(!p.hasPermission("gangsx.command.setdescription.color")) { //change to upgrade
                if(description.contains("&")) {
                    plugin.getLocaleManager().sendMessage(p, "INVALID_INPUT");
                }
            }
            gang.setDescription(description);
            gang.sendMessage(plugin.getLocaleManager().getMessage("DESCRIPTION_SET")
                    .replace("%description%", description)
                    .replace("%player%", p.getName()));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang setdescription <description>");
    }
}
