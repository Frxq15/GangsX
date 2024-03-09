package me.frxq.gangsx.objects;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.enums.Permission;
import me.frxq.gangsx.formatting.number.NumberFormatter;
import me.frxq.gangsx.enums.Role;
import me.frxq.gangsx.enums.Upgrades;
import me.frxq.gangsx.gui.GUITemplate;
import me.frxq.gangsx.utils.GangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Gang {

    private final GangsX plugin;
    private final UUID id;

    private String name;

    private String description;
    private final long created;
    private UUID leader;
    private int level;
    private int coins;
    private double bankBalance;
    private int kills;
    private int deaths;
    private int blocksbroken;
    private boolean friendlyFire;
    private List<Gang> allies;

    private int points;
    private ArrayList<GPlayer> members = new ArrayList<>();;
    private ArrayList<GPlayer> invites = new ArrayList<>();
    private HashMap<Upgrades, Integer> upgrades;
    private ArrayList<GPlayer> onlinemembers = new ArrayList<>();

    private HashMap<Permission, Role> permissions = new HashMap<>();

    private boolean renameCooldown = false;

    private final GangUtils gangUtils;

    private List<GPlayer> roster;

    private List<Gang> fight_requests;

    private List<Gang> sent_fight_requests;

    private UUID active_fight;

    private boolean isInFight;

    public Gang(GangsX plugin, final UUID id, String name, String description, final long created, UUID leader, int level, int coins, double bankBalance, int kills, int deaths, int blocksbroken, boolean friendlyFire, ArrayList<Gang> allies, ArrayList<GPlayer> members, ArrayList<GPlayer> invites, HashMap<Upgrades, Integer> upgrades, int points, HashMap<Permission, Role> permissions) {
        this.plugin = plugin;
        this.gangUtils = plugin.getGangUtils();
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.leader = leader;
        this.level = level;
        this.coins = coins;
        this.bankBalance = bankBalance;
        this.kills = kills;
        this.deaths = deaths;
        this.blocksbroken = blocksbroken;
        this.friendlyFire = friendlyFire;
        this.allies = allies;
        this.members = members;
        this.invites = new ArrayList<>();
        this.upgrades = upgrades;
        this.points = points;
        this.renameCooldown = false;
        this.permissions = permissions;
        this.roster = new ArrayList<>();
        this.fight_requests = new ArrayList<>();
        this.sent_fight_requests = new ArrayList<>();
        this.active_fight = null;
        this.isInFight = false;
    }

    public Gang(GangsX plugin, final UUID id, String name, UUID leader) {
        this.plugin = plugin;
        this.gangUtils = plugin.getGangUtils();
        this.id = id;
        this.name = name;
        this.description = plugin.getConfig().getString("gang.default_description");
        this.created = System.currentTimeMillis();
        this.leader = leader;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(leader);
        this.level = 1;
        this.coins = 0;
        this.bankBalance = 0;
        this.kills = 0;
        this.deaths = 0;
        this.blocksbroken = 0;
        this.friendlyFire = false;
        this.allies = null;
        this.members = new ArrayList<GPlayer>();
        members.add(gPlayer);
        addOnlineMember(gPlayer);
        this.invites = new ArrayList<>();
        this.upgrades = null;
        this.points = 0;
        this.renameCooldown = false;
        this.permissions = gangUtils.getDefaultPermissions();
        this.roster = new ArrayList<>();
        this.fight_requests = new ArrayList<>();
        this.sent_fight_requests = new ArrayList<>();
        this.active_fight = null;
        this.isInFight = false;
    }

    public void sendMessage(String message) {
        this.onlinemembers.forEach(member -> {
            Player p = Bukkit.getPlayer(member.getID());
            p.sendMessage(plugin.getColorFormatter().format(message).replace("%gang%", this.getName()));
        });
    }
    public void sendAlliesMessage(String message) {
        getAllies().forEach(gang -> {
            gang.getOnlineMembers().forEach(online -> {
                Player player = Bukkit.getPlayer(online.getID());
                player.sendMessage(plugin.getColorFormatter().format(message).replace("%gang%", this.getName()));
            });
        });
    }

    public UUID getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCreated() {
        return created;
    }

    public UUID getLeader() {
        return leader;
    }

    public int getLevel() {
        return level;
    }

    public int getCoins() {
        return coins;
    }

    public HashMap<Permission, Role> getPermissions() {
        return permissions;
    }

    public Role getRequiredRole(Permission permission) {
        return getPermissions().get(permission);
    }
    public void updatePermission(Permission permission, Role role) {
        getPermissions().replace(permission, role);
    }
    public double getBankBalance() {
        return bankBalance;
    }

    public int getPoints() { return points;}

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public List<GPlayer> getFightRoster() { return roster; }

    public void addToFightRoster(GPlayer player) { this.roster.add(player); }

    public void removeFromFightRoster(GPlayer player) { this.roster.remove(player); }

    public String getPointsFormatted() {
        return plugin.getCommandUtils().formatNumber(getPoints());
    }
    public String getBlocksMinedFormatted() {
        return plugin.getCommandUtils().formatNumber(getBlocksBroken());
    }
    public String getBalanceFormatted() {
        return plugin.getCommandUtils().formatNumber(getBankBalance());
    }

    public int getBlocksBroken() { return blocksbroken; }

    public boolean hasFriendlyFire() {
        return friendlyFire;
    }

    public String getDescription() { return description; }

    public List<Gang> getAllies() {
        return allies;
    }

    public int getAlliesCount() {
        return allies.size();
    }

    public ArrayList<GPlayer> getMembers() {
        return members;
    }

    public boolean hasRenameCooldown() { return renameCooldown; }

    public boolean roleHasPermission(Role role, Permission permission) {
        Role req = getPermissions().get(permission);
        if(role.getPriority() < req.getPriority()) {
            return false;
        }
        return true;
    }
    public String getPermissionOutput(Role role, Permission permission) {
        boolean output = roleHasPermission(role, permission);
        if(output) {
            return plugin.getFileManager().getPermissionsFile().getString("PERMISSION_OUTPUTS.true");
        }
        return plugin.getFileManager().getPermissionsFile().getString("PERMISSION_OUTPUTS.false");
    }

    public int getMembersCount() {
        return members.size();
    }

    public List<GPlayer> getInvites() {
        return invites;
    }

    public Integer getUpgradeLevel(Upgrades upgrade) {
        return upgrades.get(upgrade);
    }

    public String getCreationDate() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Date date = new Date(getCreated());
        String created = f.format(date);
        return created;
    }

    public int getUpgradesCount() {
        return upgrades.size();
    }

    public void applyLevelup() { this.level += 1; }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPoints(int points) { this.points = points; }

    public void addPoints(int points) { this.points += points; }

    public void removePoints(int points) { this.points -= points; }

    public void setRenameCooldown(boolean result) { this.renameCooldown = result; }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setBankBalance(int bankBalance) {
        this.bankBalance = bankBalance;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void importMembers(ArrayList<GPlayer> imported) {
        imported.forEach(member -> {
            addMember(member);
        });
    }

    public void rename(String name) {
        setName(name);
        plugin.getGangDataFactory().updateGangNameAsync(getID(), name);
    }

    public void setDescription(String description) { this.description = description; }

    public void setBlocksBroken(int blocks) { this.blocksbroken = blocks; }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public void addAlly(Gang gang) {
        this.allies.add(gang);
    }

    public void addMember(GPlayer player) {
        this.members.add(player);
    }

    public void addOnlineMember(GPlayer player) {
        this.onlinemembers.add(player);
    }

    public void inviteMember(GPlayer player) {
        this.invites.add(player);
    }

    public void addUpgrade(Upgrades upgrade, Integer level) {
        this.upgrades.put(upgrade, level);
    }

    public void increaseLevel(int levels) {
        this.level = (this.level + levels);
    }

    public void addCoins(int coins) {
        this.coins = (this.coins + coins);
    }

    public void addBankMoney(int bankMoney) {
        this.bankBalance = (this.bankBalance + bankMoney);
    }

    public void addKills(int kills) {
        this.kills = (this.kills + kills);
    }

    public void addDeaths(int deaths) {
        this.deaths = (this.deaths + deaths);
    }

    public void increaseBlocks(int blocks) {
        this.blocksbroken = (this.blocksbroken + blocks);
    }

    public void removeCoins(int coins) {
        this.coins -= coins;
    }

    public void removeBankMoney(int bankMoney) {
        this.bankBalance = (this.bankBalance - bankMoney);
    }

    public void removeAlly(Gang gang) {
        this.allies.remove(gang);
    }

    public void removeMember(GPlayer player) {
        this.members.remove(player);
    }

    public void removeOnlineMember(GPlayer player) {
        this.onlinemembers.remove(player);
    }

    public void removeUpgrade(Upgrades upgrade) {
        this.upgrades.remove(upgrade);
    }

    public void resetCoins() {
        this.coins = 0;
    }

    public void resetBankBalance() {
        this.bankBalance = 0;
    }


    public void resetKills() {
        this.kills = 0;
    }

    public void resetDeaths() {
        this.deaths = 0;
    }

    public List<GPlayer> getOnlineMembers() {
        return onlinemembers;
    }

    public String convertMembersForInfo() {
        ArrayList<String> members = new ArrayList<String>();
        getMembers().forEach(gPlayer -> {
            String icon = gPlayer.getRole().getRoleIcon();
            String display = icon+gPlayer.getName();
            members.add(display);
        });
        String m = members.toString().replace("[", "").replace("]", "");
        return m;
    }
    public String convertOnlineMembersForInfo() {
        ArrayList<String> members = new ArrayList<String>();
        getOnlineMembers().forEach(gPlayer -> {
            String icon = gPlayer.getRole().getRoleIcon();
            String display = icon+gPlayer.getName();
            members.add(display);
        });
        String m = members.toString().replace("[", "").replace("]", "");
        return m;
    }

    public String getBankBalanceFormatted() { return NumberFormatter.format(getBankBalance()); }

    public boolean checkUnload() {
        if (getOnlineMembers().isEmpty())
            return true;

        return false;
    }
    public void disband() {
        List<GPlayer> gOnline = new ArrayList<>(getOnlineMembers());
        for(GPlayer gOnl : gOnline) {
            Player gP = Bukkit.getPlayer(gOnl.getID());
            if(GUITemplate.openInventories.containsKey(gP.getUniqueId())) {
                gP.getOpenInventory().close();
            }
        }
        List<GPlayer> gPlayers = new ArrayList<>(getMembers());
        for (GPlayer gPlayer : gPlayers) {
            gPlayer.kickFromGang();
        }
    }
    public Integer getKDR() {
        if(getKills() == 0) {
            return 0;
        }
        if(getDeaths() == 0) {
            return getKills();
        }
        return (getKills()/getDeaths());
    }
    public String getKDRString() {
        if(getKills() == 0) {
            return "0";
        }
        if(getDeaths() == 0) {
            return getKills()+"";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(getKills()/getDeaths());
    }
}