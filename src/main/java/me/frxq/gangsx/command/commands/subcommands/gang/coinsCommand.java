package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class coinsCommand extends SubCommand {
    private final GangsX plugin;

    public coinsCommand(GangsX plugin) {
        super("coins", "gangsx.command.coins", "/gang bank", null);
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
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            p.sendMessage(plugin.getLocaleManager().getMessage("GANG_COINS_BALANCE")
                    .replace("%gang%", gang.getName()).replace("%coins%", gang.getCoins()+""));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang coins");
        return;
    }
}
