package com.twojapakiet.shoptop;

import com.twojapakiet.shoptop.commands.TopBuysCommand;
import com.twojapakiet.shoptop.commands.TopSellsCommand;
import com.twojapakiet.shoptop.data.DataManager;
import com.twojapakiet.shoptop.listeners.TransactionListener;
import com.twojapakiet.shoptop.placeholders.ShopStatsExpansion;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopTop extends JavaPlugin {

    private DataManager dataManager;

    @Override
    public void onEnable() {
        this.dataManager = new DataManager(this);
        dataManager.loadData();

        getServer().getPluginManager().registerEvents(new TransactionListener(dataManager), this);

        getCommand("topkupno").setExecutor(new TopBuysCommand(dataManager));
        getCommand("topsprzedaz").setExecutor(new TopSellsCommand(dataManager));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ShopStatsExpansion(dataManager).register();
            getLogger().info("Pomyślnie zintegrowano z PlaceholderAPI.");
        }

        getLogger().info("Plugin ShopTop został włączony!");
    }

    @Override
    public void onDisable() {
        dataManager.saveData();
        getLogger().info("Plugin ShopTop został wyłączony!");
    }
}
