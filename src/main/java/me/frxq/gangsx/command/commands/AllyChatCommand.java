package me.frxq.gangsx.command.commands;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AllyChatCommand implements CommandExecutor {
    private GangsX plugin = GangsX.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return true;
        }
        Player p = (Player) sender;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if(!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return true;
        }
        if(args.length == 0) {
            if(gPlayer.hasAllyChatEnabled()) {
                gPlayer.setAllyChatEnabled(false);
                plugin.getLocaleManager().sendMessage(p, "PLAYER_ALLY_CHAT_DISABLED");
                return true;
            }
            gPlayer.setChatEnabled(false);
            gPlayer.setAllyChatEnabled(true);
            plugin.getLocaleManager().sendMessage(p, "PLAYER_ALLY_CHAT_ENABLED");
            return true;
        }
        if(args.length > 0) {
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            String message = plugin.getCommandUtils().getFinalArg(args, 0);

            gang.sendAlliesMessage(plugin.getConfig().getString("gang.ally_chat_format")
                    .replace("%role%", gPlayer.getRole().getRolePrefix())
                    .replace("%player%", p.getName())
                    .replace("%message%", message));
            gang.sendMessage(plugin.getConfig().getString("gang.ally_chat_format")
                    .replace("%role%", gPlayer.getRole().getRolePrefix())
                    .replace("%player%", p.getName())
                    .replace("%message%", message));
        }
        return true;
    }
}
