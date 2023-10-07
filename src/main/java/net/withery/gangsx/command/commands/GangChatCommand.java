package net.withery.gangsx.command.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.withery.gangsx.GangsX;
import net.withery.gangsx.enums.Permission;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GangChatCommand implements CommandExecutor {
    private GangsX plugin = GangsX.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return true;
        }
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if (!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return true;
        }
        Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.GANG_CHAT)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return true;
        }
        if (args.length == 0) {
            if (gPlayer.hasChatEnabled()) {
                gPlayer.setChatEnabled(false);
                plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_CHAT_DISABLED");
                return true;
            }
            gPlayer.setChatEnabled(true);
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_CHAT_ENABLED");
            return true;
        }
        if (args.length > 0) {
            String message = plugin.getCommandUtils().getFinalArg(args, 0);

            gang.sendMessage(plugin.getConfig().getString("gang.chat_format")
                    .replace("%role%", gPlayer.getRole().getRolePrefix())
                    .replace("%player%", p.getName())
                    .replace("%message%", message));
        }
        return true;
    }
}
