package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class allychatCommand extends SubCommand {
    private final GangsX plugin;

    public allychatCommand(GangsX plugin) {
        super("chat", "gangsx.command.allychat", "/gang allychat", Arrays.asList("ac"));
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
        if(args.length == 0) {
            if(gPlayer.hasAllyChatEnabled()) {
                gPlayer.setAllyChatEnabled(false);
                plugin.getLocaleManager().sendMessage(p, "PLAYER_ALLY_CHAT_DISABLED");
                return;
            }
            gPlayer.setChatEnabled(false);
            gPlayer.setAllyChatEnabled(true);
            plugin.getLocaleManager().sendMessage(p, "PLAYER_ALLY_CHAT_ENABLED");
            return;
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
    }
}
