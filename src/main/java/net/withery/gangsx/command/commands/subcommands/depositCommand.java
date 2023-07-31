package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.formatting.number.NumberFormatter;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class depositCommand extends SubCommand {
    private final GangsX plugin;

    public depositCommand(GangsX plugin) {
        super("deposit", "gangsx.command.deposit", "/gang deposit <amount>", Arrays.asList("bankdeposit"));
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
        if(args.length == 1) {
            double balance = plugin.getAPIHooks().getEconomy().getBalance(p);
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
            if(amount > balance) {
                plugin.getLocaleManager().sendMessage(p, "DEPOSIT_FAIL_AMOUNT");
                return;
            }
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
            if(plugin.getConfig().getBoolean("gang.bank.limit_enabled")) {
                if(amount+(int)gang.getBankBalance() > plugin.getConfig().getInt("gang.bank.bank_limit")) {
                    plugin.getLocaleManager().sendMessage(p, "BANK_BALANCE_EXCEED");
                    return;
                }
            }
            gang.addBankMoney(amount);
            gang.sendMessage(plugin.getLocaleManager().getMessage("BANK_DEPOSITED")
                    .replace("%amount%", NumberFormatter.format(amount))
                    .replace("%player%", p.getName()));
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang deposit <amount>");
        return;
    }
}
