package com.spleefleague.splegg;

import com.spleefleague.core.Core;
import com.spleefleague.core.chat.Chat;
import com.spleefleague.core.game.arena.Arenas;
import com.spleefleague.core.menu.InventoryMenuAPI;
import com.spleefleague.core.menu.InventoryMenuContainerChest;
import com.spleefleague.core.menu.InventoryMenuItem;
import com.spleefleague.core.menu.hotbars.main.GamemodeMenu;
import com.spleefleague.core.menu.hotbars.main.LeaderboardMenu;
import com.spleefleague.core.player.CoreDBPlayer;
import com.spleefleague.core.player.PlayerManager;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.coreapi.chat.ChatColor;
import com.spleefleague.splegg.commands.SpleggCommand;
import com.spleefleague.splegg.commands.SpleggGunCommand;
import com.spleefleague.splegg.game.SpleggGun;
import com.spleefleague.splegg.game.SpleggMode;
import com.spleefleague.splegg.player.SpleggPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

/**
 * @author NickM13
 * @since 4/16/2020
 */
public class Splegg extends CorePlugin {

    private static Splegg instance;

    private InventoryMenuItem spleggMenuItem;

    private PlayerManager<SpleggPlayer, CoreDBPlayer> playerManager;

    @Override
    protected void init() {
        instance = this;
        setPluginDB("Splegg");

        SpleggGun.init();

        playerManager = new PlayerManager<>(this, SpleggPlayer.class, CoreDBPlayer.class, getPluginDB().getCollection("Players"));

        SpleggMode.init();
        addBattleManager(SpleggMode.VERSUS.getBattleMode());
        addBattleManager(SpleggMode.MULTI.getBattleMode());
        initMenu();
        initCommands();
    }

    @Override
    protected void close() {

    }
    
    /**
     * @return Chat Prefix
     */
    public Component getChatPrefix() {
        return Component.text(Chat.TAG_BRACE + "[" + Chat.TAG + "Splegg" + Chat.TAG_BRACE + "] ");
    }
    
    public static Splegg getInstance() {
        return instance;
    }

    public PlayerManager<SpleggPlayer, CoreDBPlayer> getPlayers() {
        return playerManager;
    }

    public InventoryMenuItem getSpleggMenu() {
        return spleggMenuItem;
    }

    public void initMenu() {
        spleggMenuItem = InventoryMenuAPI.createItemDynamic()
                .setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Splegg")
                .setDescription("Dispatch your foes from up close or from afar with an arsenal of ranged weapons in this fast paced spin on Spleef!")
                .setDisplayItem(Material.EGG, 1)
                .createLinkedContainer("Splegg");

        InventoryMenuContainerChest container = spleggMenuItem.getLinkedChest();

        InventoryMenuItem classicMenu = Arenas.createMenu(getInstance(), SpleggMode.VERSUS.getBattleMode());
        classicMenu.getLinkedChest().addStaticItem(SpleggGun.createMenu("s1", "s2"), 6, 2);
        classicMenu.getLinkedChest().addStaticItem(SpleggGun.createMenu("s2", "s1"), 6, 3);

        InventoryMenuItem multiMenu = Arenas.createMenu(getInstance(), SpleggMode.MULTI.getBattleMode());
        multiMenu.getLinkedChest().addStaticItem(SpleggGun.createMenu("m1", "m2"), 6, 2);
        multiMenu.getLinkedChest().addStaticItem(SpleggGun.createMenu("m2", "m1"), 6, 3);

        container.addStaticItem(classicMenu, 6, 1);
        container.addStaticItem(multiMenu, 5, 1);

        SpleggGun.createMenu();

        GamemodeMenu.getItem().getLinkedChest().addStaticItem(spleggMenuItem, 4, 1);

        LeaderboardMenu.addLeaderboardMenu(SpleggMode.VERSUS.getBattleMode());
        LeaderboardMenu.addLeaderboardMenu(SpleggMode.MULTI.getBattleMode());
    }

    protected void initCommands() {
        Core.getInstance().addCommand(new SpleggCommand());
        Core.getInstance().addCommand(new SpleggGunCommand());
    }

}
