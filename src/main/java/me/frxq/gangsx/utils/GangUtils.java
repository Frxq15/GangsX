package me.frxq.gangsx.utils;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.enums.Role;

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
        permissions.put(Permission.GANG_CHAT, getDefaultRolePermission(Permission.GANG_CHAT));

        return permissions;
    }
    public boolean playerHasGangPermission(GPlayer gPlayer, Gang gang, Permission permission) {
        Role minimum = gang.getPermissions().get(permission);
        if(minimum.getPriority() <= gPlayer.getRole().getPriority()) {
            return true;
        }
        return false;
    }
    public boolean canLevelup(Gang gang) {
        if(isMaxGangLevel(gang)) {
            return false;
        }
        int level = gang.getLevel();
        String levelName = String.valueOf((level+1));
        int rBlocks = plugin.getConfig().getInt("levels."+levelName+".requirements.blocksmined");
        int rKills = plugin.getConfig().getInt("levels."+levelName+".requirements.playerkills");
        int rBankBalance = plugin.getConfig().getInt("levels."+levelName+".requirements.bankbalance");

        if(compareStatistic(gang, "blocksmined", rBlocks) &&
                compareStatistic(gang, "playerkills", rKills) &&
                        compareStatistic(gang, "bankbalance", rBankBalance)) {
            return true;
        }
        return false;
    }
    public int getRequiredLevelupMoney(Gang gang) {
        int level = gang.getLevel();
        String levelName = String.valueOf((level+1));
        return plugin.getConfig().getInt("levels."+levelName+".requirements.bankbalance");
    }
    public boolean isMaxGangLevel(Gang gang) {
        if(gang.getLevel() >= plugin.getConfig().getInt("gang.maximum_level")) {
            return true;
        }
        return false;
    }
    public boolean compareStatistic(Gang gang, String statistic, int value) {
        switch (statistic.toLowerCase()) {
            case "blocksmined":
                int blocksmined = gang.getBlocksBroken();
                if(blocksmined < value) {
                    return false;
                }
                return true;
            case "playerkills":
                int playerkills = gang.getKills();
                if(playerkills < value) {
                    return false;
                }
                return true;
            case "bankbalance":
                int bankbalance = (int)gang.getBankBalance();
                if(bankbalance < value) {
                    return false;
                }
                return true;

            default:
                return false;

        }
    }
}
