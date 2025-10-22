package com.twojapakiet.shoptop;

import com.twojapakiet.shoptop.commands.ReloadCommand;
import com.twojapakiet.shoptop.data.DataManager;
import com.twojapakiet.shoptop.listeners.TransactionListener;
import com.twojapakiet.shoptop.placeholders.ShopStatsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopTop extends JavaPlugin {

    private static ShopTop instance;
    private DataManager dataManager;
    private static final long SAVE_INTERVAL = 20L * 60 * 5; // 5 minut

    // NOWE POLE: Przechowujemy instancję ekspansji
    private ShopStatsExpansion shopStatsExpansion;

    @Override
    public void onEnable() {
        instance = this;
        this.dataManager = new DataManager(this);
        dataManager.loadData();

        getServer().getPluginManager().registerEvents(new TransactionListener(dataManager), this);

        // ZMIANA: Sprawdzamy PAPI i zapisujemy instancję ekspansji
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.shopStatsExpansion = new ShopStatsExpansion(this);
            this.shopStatsExpansion.register();
            getLogger().info("PlaceholderAPI znalezione. Rejestrowanie ekspansji 'sklep_staty'...");
        } else {
            getLogger().warning("PlaceholderAPI nie znalezione! Placeholdery nie będą działać.");
        }

        // Rejestracja komendy (bez zmian)
        getServer().getCommandMap().register("shoptop", new ReloadCommand(this));

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataManager.saveData();
            getLogger().info("Dane statystyk zostały zapisane.");
        }, SAVE_INTERVAL, SAVE_INTERVAL);

        getLogger().info("Plugin ShopTop został włączony!");
    }

    @Override
    public void onDisable() {
        // ZMIANA: Wyrejestrujemy ekspansję przy wyłączaniu
        if (this.shopStatsExpansion != null) {
            this.shopStatsExpansion.unregister();
            getLogger().info("Wyrejestrowano ekspansję PlaceholderAPI.");
        }

        if (dataManager != null) {
            dataManager.saveData();
            getLogger().info("Wszystkie statystyki graczy zostały zapisane przed wyłączeniem pluginu.");
        }
        getLogger().info("Plugin ShopTop został wyłączony!");
    }

    // ZMIANA: Kompletna przebudowa logiki przeładowania
    public void reloadPlugin() {
        getLogger().info("Rozpoczynanie przeładowania pluginu ShopTop...");

        // 1. Zapisz bieżące dane
        dataManager.saveData();

        // 2. Wyczyść stare dane z pamięci
        dataManager.clearData();

        // 3. Załaduj dane na nowo z pliku
        dataManager.loadData();

        // 4. Przeładuj PlaceholderAPI
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            // Wyrejestruj starą instancję, jeśli istnieje
            if (this.shopStatsExpansion != null) {
                this.shopStatsExpansion.unregister();
            }
            // Stwórz i zarejestruj nową instancję
            this.shopStatsExpansion = new ShopStatsExpansion(this);
            this.shopStatsExpansion.register();
            getLogger().info("Ekspansja PlaceholderAPI została pomyślnie przeładowana.");
        } else {
            getLogger().warning("PlaceholderAPI nie znalezione! Nie można przeładować ekspansji.");
        }

        getLogger().info("Plugin ShopTop został pomyślnie przeładowany!");
    }

    // Gettery (bez zmian)
    public static ShopTop getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
