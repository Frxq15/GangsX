package net.withery.gangsx.gui.GUIs;

import net.withery.gangsx.gui.GUITemplate;
import net.withery.gangsx.GangsX;
import net.withery.gangsx.objects.Gang;
import org.bukkit.entity.Player;

public class Shop extends GUITemplate {
    private Player p;
    private Gang gang;
    public Shop(GangsX plugin) {
        super(plugin, plugin.getFileManager().getShopFile().getInt("ROWS"), plugin.getFileManager().getShopFile().getString("TITLE"));
        initialize();
    }
    void initialize() {
    }
    void initializeItem() {

    }
}
