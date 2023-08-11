package net.withery.gangsx.gui.menus;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.gui.GUITemplate;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class GangInvsee extends GUITemplate {
    private final Player player;
    private final Player target;
    public GangInvsee(GangsX plugin, Player player, Player target) {
        super(plugin, 5, plugin.getColorFormatter().format("&8"+target.getName()+"'s Inventory"));
        this.player = player;
        this.target = target;
        initialize();
    }
    public void initialize() {
        int size = target.getInventory().getSize();
        AtomicInteger i = new AtomicInteger(0);
        while(i.get() < size) {
            setItem(i.get(), target.getInventory().getItem(i.get()));
            i.getAndIncrement();
        }
    }
}
