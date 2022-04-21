package net.withery.gangsx.command.commands.subcommands;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.command.SubCommand;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import net.withery.gangsx.settings.locale.LocaleReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class createCommand extends SubCommand {
    private final GangsX plugin;

    public createCommand(GangsX plugin) {
        super("create", "gangsx.command.help", "create <name>", new String[]{"new", "start"});
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        Player p = (Player) sender;
        if(args.length == 1) {
            String name = args[0];
            Gang gang = new Gang(plugin, UUID.randomUUID(), name, p.getUniqueId());
            GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
            gPlayer.setGangId(gPlayer.getGangId());
            plugin.getLocaleRegistry().sendMessageToAll(LocaleReference.COMMAND_GANG_CREATED, gang.getName(), p.getName());
            plugin.getLocaleRegistry().sendMessage(p, LocaleReference.COMMAND_PLAYER_GANG_CREATED);
            return;
        }
        plugin.getLocaleRegistry().sendMessage(p, LocaleReference.COMMAND_WRONG_USAGE, "create <name>");
        return;
    }
}
