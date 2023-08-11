package net.withery.gangsx.utils;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.enums.Permission;
import net.withery.gangsx.enums.Role;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GangUtils {
    private final GangsX plugin;
    public GangUtils(GangsX plugin) {
        this.plugin = plugin;
    }
    public void prepareDisband(Gang gang) {
        gang.disband();
        plugin.getGangDataFactory().unloadGangData(gang.getID());
        plugin.getGangDataFactory().deleteGangData(gang.getID());
    }
    public boolean matchesRegex(String input) {
        String regex = plugin.getConfig().getString("gang.name_regex");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(!matcher.matches()) {
            return false;
        }
        return true;
    }
    public Role getDefaultRolePermission(Permission permission) {
        String perm = permission.getName();
        Role role = Role.valueOf(plugin.getConfig().getString("gang.default_permissions."+perm.toUpperCase()));
        return role;
    }
    public String getDefaultRolePermissionString(Permission permission) {
        String perm = permission.getName();
        Role role = Role.valueOf(plugin.getConfig().getString("gang.default_permissions."+perm));
        return role.getName();
    }
    public HashMap<Permission, Role> getDefaultPermissions() {
        HashMap<Permission, Role> permissions = new HashMap<>();
        permissions.put(Permission.BANK_DEPOSIT, getDefaultRolePermission(Permission.BANK_DEPOSIT));
        permissions.put(Permission.BANK_WITHDRAW, getDefaultRolePermission(Permission.BANK_WITHDRAW));
        permissions.put(Permission.CHANGE_DESCRIPTION, getDefaultRolePermission(Permission.CHANGE_DESCRIPTION));
        permissions.put(Permission.MANAGE_RELATIONS, getDefaultRolePermission(Permission.MANAGE_RELATIONS));
        permissions.put(Permission.PURCHASE_VALUE, getDefaultRolePermission(Permission.PURCHASE_VALUE));
        permissions.put(Permission.PURCHASE_UPGRADES, getDefaultRolePermission(Permission.PURCHASE_UPGRADES));
        permissions.put(Permission.PROMOTE, getDefaultRolePermission(Permission.PROMOTE));
        permissions.put(Permission.DEMOTE, getDefaultRolePermission(Permission.DEMOTE));
        permissions.put(Permission.MANAGE_FRIENDLY_FIRE, getDefaultRolePermission(Permission.MANAGE_FRIENDLY_FIRE));
        permissions.put(Permission.KICK, getDefaultRolePermission(Permission.KICK));
        permissions.put(Permission.RENAME_GANG, getDefaultRolePermission(Permission.RENAME_GANG));
        permissions.put(Permission.SHOP, getDefaultRolePermission(Permission.SHOP));
        permissions.put(Permission.INVSEE, getDefaultRolePermission(Permission.INVSEE));
        permissions.put(Permission.INVITE, getDefaultRolePermission(Permission.INVITE));

        return permissions;
    }
}
