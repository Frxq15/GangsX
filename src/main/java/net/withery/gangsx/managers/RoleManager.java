package net.withery.gangsx.managers;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.enums.Role;
import net.withery.gangsx.objects.GPlayer;

public class RoleManager {
    private GangsX plugin;
    private GPlayer gPlayer;
    public RoleManager(GangsX plugin, GPlayer gPlayer) {
        this.plugin = plugin;
        this.gPlayer = gPlayer;
    }
    Role role = gPlayer.getRole();
    public boolean canManage(Role promotion) {
        if(promotion.getPriority() >= role.getPriority()) { return false; }
        return true;
    }
    public Role getNextRole() {
        Role[] roles = Role.values();
        return roles[Math.min(roles.length -1, role.ordinal() + 1)];
    }
    public Role getPreviousRole() {
        Role[] roles = Role.values();
        if(role.getPriority() == 0) {
            return Role.RECRUIT;
        }
        return roles[Math.max(0, role.ordinal() - 1)];
    }
}
