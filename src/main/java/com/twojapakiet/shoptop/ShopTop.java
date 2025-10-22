package com.twojapakiet.shoptop;

// Import dla nowej komendy
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

    @Override
    public void onEnable() {
        instance = this; 
        this.dataManager = new DataManager(this);
        dataManager.loadData();

        getServer().getPluginManager().registerEvents(new TransactionListener(dataManager), this);

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new ShopStatsExpansion(this).register();
            getLogger().info("PlaceholderAPI znalezione. Rejestrowanie ekspansji 'sklep_staty'...");
        } else {
            getLogger().warning("PlaceholderAPI nie znalezione! Placeholdery nie będą działać.");
        }
        
        // NOWA LINIA: Rejestracja komendy
        getCommand("shoptop").setExecutor(new ReloadCommand(this));

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataManager.saveData();
            getLogger().info("Dane statystyk zostały zapisane.");
        }, SAVE_INTERVAL, SAVE_INTERVAL);

        getLogger().info("Plugin ShopTop został włączony!");
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveData();
            getLogger().info("Wszystkie statystyki graczy zostały zapisane przed wyłączeniem pluginu.");
        }
        getLogger().info("Plugin ShopTop został wyłączony!");
    }

    // NOWA METODA DO PRZEŁADOWANIA
    public void reloadPlugin() {
        getLogger().info("Rozpoczynanie przeładowania pluginu ShopTop...");

        // 1. Zapisz bieżące dane (na wszelki wypadek)
        dataManager.saveData();

        // 2. Wyczyść stare dane z pamięci
        dataManager.clearData();

        // 3. Załaduj dane na nowo z pliku
        dataManager.loadData();
        
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
