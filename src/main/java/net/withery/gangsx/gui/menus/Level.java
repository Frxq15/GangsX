package net.withery.gangsx.gui.menus;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.gui.GUITemplate;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Level extends GUITemplate {
    private Player player;
    private Gang gang;
    private GangsX plugin;
    private FileConfiguration level;

    public Level(GangsX plugin, Player player) {
        super(plugin, plugin.getFileManager().getLevelFile().getInt("ROWS"), plugin.getColorFormatter().format(plugin.getFileManager().getLevelFile().getString("TITLE")));
        this.plugin = plugin;
        this.player = player;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        this.gang = plugin.getGangDataFactory().getGangData(gPlayer.getGangId());
        this.level = plugin.getFileManager().getLevelFile();
        initialize();
    }
    public void initialize() {

    }
}
