package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.enums.Permission;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class levelupCommand extends SubCommand {
    private final GangsX plugin;

    public levelupCommand(GangsX plugin) {
        super("levelup", "gangsx.command.levelup", "/gang levelup", null);
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
        if(!plugin.getGangUtils().playerHasGangPermission(gPlayer,gang, Permission.PURCHASE_UPGRADES)) { //to be changed to levelup
            plugin.getLocaleManager().sendMessage(p, "PLAYER_GANG_NO_PERMISSION");
            return;
        }
        if(args.length == 0) {
            if(!plugin.getGangUtils().canLevelup(gang)) {
                String levelName = String.valueOf((gang.getLevel()+1));
                plugin.getConfig().getStringList("levels."+levelName+".requirements_message").forEach(line -> {
                    p.sendMessage(plugin.getColorFormatter().format(line));
                });
                return;
            }
            gang.removeBankMoney(plugin.getGangUtils().getRequiredLevelupMoney(gang));
            gang.applyLevelup();
            plugin.getConfig().getStringList("levels."+gang.getLevel()+".levelup_message").forEach(line -> {
                gang.sendMessage(plugin.getColorFormatter().format(line));
            });
            plugin.getConfig().getStringList("levels."+gang.getLevel()+".levelup_commands").forEach(cmd -> {
                cmd = cmd.replace("%gang%", gang.getName()).replace("%level%", gang.getLevel()+"");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            });
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang levelup");
    }
}
