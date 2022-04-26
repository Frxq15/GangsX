package net.withery.gangsx.objects;

import net.withery.gangsx.enums.Upgrades;
import net.withery.gangsx.GangsX;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class Gang {

    private final GangsX plugin;
    private final UUID id;

    private String name;
    private long created;
    private UUID leader;
    private int level;
    private int coins;
    private double bankBalance;
    private int kills;
    private int deaths;
    private int blocksbroken;
    private boolean friendlyFire;
    private final List<Gang> allies;
    private final List<GPlayer> members;
    private final List<GPlayer> invites;
    private final HashMap<Upgrades, Integer> upgrades;

    private final List<GPlayer> onlinemembers = new ArrayList<>();

    public Gang(GangsX plugin, final UUID id, final String name, final long created, final UUID leader, final int level, final int coins, final double bankBalance, final int kills, final int deaths, final int blocksbroken, final boolean friendlyFire, final List<Gang> allies, final List<GPlayer> members, final List<GPlayer> invites, final HashMap<Upgrades, Integer> upgrades) {
        this.plugin = plugin;
        this.id = id;
        this.name = name;
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
        this.invites = invites;
        this.upgrades = upgrades;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(leader);
        this.addOnlineMember(gPlayer);
    }

    public Gang(GangsX plugin, final UUID id, final String name, final UUID leader) {
        this.plugin = plugin;
        this.id = id;
        this.name = name;
        this.created = System.currentTimeMillis();
        this.leader = leader;
        this.level = 0;
        this.coins = 0;
        this.bankBalance = 0;
        this.kills = 0;
        this.deaths = 0;
        this.blocksbroken = 0;
        this.friendlyFire = false;
        this.allies = null;
        this.members = null;
        this.invites = null;
        this.upgrades = null;
        GPlayer gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(leader);
        this.addOnlineMember(gPlayer);
    }

    public void sendMessage(String message) {
        this.onlinemembers.forEach(member -> {
            Player p = Bukkit.getPlayer(member.getID());
            p.sendMessage(plugin.getColorFormatter().format(message));
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

    public double getBankBalance() {
        return bankBalance;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBlocksBroken() { return blocksbroken; }

    public boolean hasFriendlyFire() {
        return friendlyFire;
    }

    public List<Gang> getAllies() {
        return allies;
    }

    public int getAlliesCount() {
        return allies.size();
    }

    public List<GPlayer> getMembers() {
        return members;
    }

    public int getMembersCount() {
        return members.size();
    }

    public List<GPlayer> getInvites() {
        return invites;
    }

    public Integer getUpgrade(Upgrades upgrade) {
        return upgrades.get(upgrade);
    }

    public Date getCreationDate() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Date date = new Date(getCreated());
        String created = f.format(date);
        return date;
    }

    public int getUpgradesCount() {
        return upgrades.size();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public void setLevel(int level) {
        this.level = level;
    }

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
        this.coins = (this.coins - coins);
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

    public boolean checkUnload() {
        if (getOnlineMembers().isEmpty())
            return true;

        return false;
    }

}