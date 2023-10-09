package me.frxq.gangsx.command.commands.subcommands.gang;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.gui.menus.gangtop.Value;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class topCommand extends SubCommand {
    private final GangsX plugin;

    public topCommand(GangsX plugin) {
        super("top", "gangsx.command.top", "/gang top", Arrays.asList("topgangs"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            new Value(plugin, p).open(p);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /gang top");
    }
}
