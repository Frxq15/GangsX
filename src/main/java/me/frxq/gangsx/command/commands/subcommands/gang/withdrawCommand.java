package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.formatting.number.NumberFormatter;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class withdrawCommand extends SubCommand {
    private final GangsX plugin;

    public withdrawCommand(GangsX plugin) {
        super("withdraw", "gangsx.command.withdraw", "/gang withdraw <amount>", Arrays.asList("bankwithdraw"));
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
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.BANK_WITHDRAW)) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if(args.length == 1) {
            int amount = 0;
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                plugin.getLocaleManager().sendMessage(p, "INVALID_INPUT");
                return;
            }
            if(amount == 0) {
                plugin.getLocaleManager().sendMessage(p, "VALUE_CANNOT_BE_ZERO");
                return;
            }
            if(amount > gang.getBankBalance()) {
                plugin.getLocaleManager().sendMessage(p, "WITHDRAW_FAIL_AMOUNT");
                return;
            }
            gang.removeBankMoney(amount);
            plugin.getAPIHooks().getEconomy().depositPlayer(p, amount);
            gang.sendMessage(plugin.getLocaleManager().getMessage("BANK_WITHDRAWN")
                    .replace("%amount%", NumberFormatter.format(amount))
                    .replace("%player%", p.getName()));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang withdraw <amount>");
        return;
    }
}
