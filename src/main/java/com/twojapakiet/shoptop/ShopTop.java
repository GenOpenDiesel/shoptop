package com.twojapakiet.shoptop;

import com.twojapakiet.shoptop.data.DataManager;
import com.twojapakiet.shoptop.listeners.TransactionListener;
import com.twojapakiet.shoptop.placeholders.ShopStatsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopTop extends JavaPlugin {

    private static ShopTop instance; // Tak jak w przykładzie
    private DataManager dataManager;
    private static final long SAVE_INTERVAL = 20L * 60 * 5; // 5 minut

    @Override
    public void onEnable() {
        instance = this; // Ustawienie instancji, tak jak w przykładzie
        this.dataManager = new DataManager(this);
        dataManager.loadData();

        getServer().getPluginManager().registerEvents(new TransactionListener(dataManager), this);

        // Rejestracja PlaceholderAPI - DOKŁADNIE tak jak w SpawnChestPlugin
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new ShopStatsExpansion(this).register();
            getLogger().info("PlaceholderAPI znalezione. Rejestrowanie ekspansji 'sklep_staty'...");
        } else {
            getLogger().warning("PlaceholderAPI nie znalezione! Placeholdery nie będą działać.");
        }

        // Uruchomienie asynchronicznego, cyklicznego zapisu danych
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataManager.saveData();
            getLogger().info("Dane statystyk zostały zapisane.");
        }, SAVE_INTERVAL, SAVE_INTERVAL);

        getLogger().info("Plugin ShopTop został włączony!");
    }

    @Override
    public void onDisable() {
        // W przykładzie nie było wyrejestrowywania, PAPI robi to samo
        
        // Ostateczny zapis danych
        if (dataManager != null) {
            dataManager.saveData();
            getLogger().info("Wszystkie statystyki graczy zostały zapisane przed wyłączeniem pluginu.");
        }
        getLogger().info("Plugin ShopTop został wyłączony!");
    }

    // Gettery, tak jak w przykładzie, aby ekspansja miała dostęp
    public static ShopTop getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
