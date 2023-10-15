package me.frxq.gangsx.command.commands.subcommands.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class challengeCommand extends SubCommand {
    private final GangsX plugin;
    public challengeCommand(GangsX plugin) {
        super("challenge", "gangsx.command.challenge", "/fight challenge", Arrays.asList("duel"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) {
            plugin.log("This command cannot be executed from console.");
            return;
        }
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(p.getUniqueId());
        if(!gPlayer.hasGang()) {
            plugin.getLocaleManager().sendMessage(p, "PLAYER_NOT_IN_A_GANG");
            return;
        }
        if(args.length == 1) {
            String target = args[0];

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if(!plugin.getGangDataFactory().doesGangNameExist(target)) {
                    plugin.getLocaleManager().sendMessage(p, "GANG_NOT_FOUND");
                    return;
                    }
                });
            UUID uuid = plugin.getGangDataFactory().getGangUniqueId(target);
            Gang tGang = plugin.getGangDataFactory().getGangData(uuid);
            Gang gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());

            TextComponent textComponent = new TextComponent("Click Here");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "fight accept "+gang.getName()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept this request").create()));
            textComponent.setColor(ChatColor.GREEN);

            tGang.getFightRoster().forEach(r -> {
                Player t = Bukkit.getPlayer(r.getID());
                t.spigot().sendMessage(textComponent);
            });

            gang.sendFightRequest(tGang);
            return;
        }
        plugin.getLocaleManager().sendUsageMessage(p, "&cUsage: /fight duel <gang>");
        return;
    }
}
