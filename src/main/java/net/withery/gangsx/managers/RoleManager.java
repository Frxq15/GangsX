package net.withery.gangsx.managers;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.enums.Role;
import net.withery.gangsx.objects.GPlayer;

public class RoleManager {
    private GangsX plugin;

    public RoleManager(GangsX plugin) {
        this.plugin = plugin;
    }
    public boolean canPromote(Role promotion, GPlayer gPlayer, GPlayer tPlayer) {
        if(tPlayer.getRole().equals(Role.LEADER)) { return false; }
        if(promotion.getPriority() >= gPlayer.getRole().getPriority()) { return false; }
        return true;
    }
    public boolean canDemote(GPlayer gPlayer, GPlayer tPlayer) {
        if(tPlayer.getRole().equals(Role.LEADER)) { return false; }
        if(tPlayer.getRole().getPriority() >= gPlayer.getRole().getPriority()) { return false; }
        return true;
    }
    public boolean canManage(GPlayer gPlayer, GPlayer tPlayer) {
        if(tPlayer.getRole().equals(Role.LEADER)) { return false; }
        if(tPlayer.getRole().getPriority() >= gPlayer.getRole().getPriority()) { return false; }
        return true;
    }
    public Role getNextRole(GPlayer gPlayer) {
        Role[] roles = Role.values();
        return roles[Math.min(roles.length -1, gPlayer.getRole().ordinal() + 1)];
    }
    public Role getPreviousRole(GPlayer gPlayer) {
        Role[] roles = Role.values();
        if(gPlayer.getRole().getPriority() == 0) {
            return Role.RECRUIT;
        }
        return roles[Math.max(0, gPlayer.getRole().ordinal() - 1)];
    }
    public void promotePlayer(GPlayer gPlayer) {
        gPlayer.setRole(getNextRole(gPlayer));
    }
    public void demotePlayer(GPlayer gPlayer) {
        gPlayer.setRole(getPreviousRole(gPlayer));
    }
}
