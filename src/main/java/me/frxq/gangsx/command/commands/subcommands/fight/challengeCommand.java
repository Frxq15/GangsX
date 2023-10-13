package me.frxq.gangsx.command.commands.subcommands.fight;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class challengeCommand extends SubCommand {
    private final GangsX plugin;
    public challengeCommand(GangsX plugin) {
        super("challenge", "gangsx.command.challenge", "/fight challenge", Arrays.asList("duel"));
        this.plugin = plugin;
    }

    @Override
    public @NotNull void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

    }
}
