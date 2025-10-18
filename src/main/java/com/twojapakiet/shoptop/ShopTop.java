package com.twojapakiet.shoptop;

import com.twojapakiet.shoptop.data.DataManager;
import com.twojapakiet.shoptop.listeners.TransactionListener;
import com.twojapakiet.shoptop.placeholders.ShopStatsExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopTop extends JavaPlugin {

    private DataManager dataManager;
    private static final long SAVE_INTERVAL = 20L * 60 * 5; // 5 minut
    private ShopStatsExpansion expansion;

    @Override
    public void onEnable() {
        this.dataManager = new DataManager(this);
        dataManager.loadData();

        getServer().getPluginManager().registerEvents(new TransactionListener(dataManager), this);

        // Rejestracja PlaceholderAPI
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI znalezione. Rejestrowanie ekspansji 'sklep_staty'...");
            
            expansion = new ShopStatsExpansion(dataManager);
            boolean success = expansion.register();
            
            // Sprawdzamy, czy rejestracja się powiodła I czy PAPI potwierdza, że ekspansja jest zarejestrowana
            if (success && expansion.isRegistered()) {
                getLogger().info("✓ Ekspansja 'sklep_staty' została pomyślnie ZAREJESTROWANA!");
                getLogger().info("Dostępne placeholdery:");
                getLogger().info("  - %sklep_staty_test%");
                getLogger().info("  - %sklep_staty_ilekupilem%");
                getLogger().info("  - %sklep_staty_ilesprzedalem%");
                
                // Test bezpośredniego parsowania (uwaga: player jest null, więc użyje onRequest(null, ...))
                // Powinno to wywołać logikę "TEST_OFFLINE" z ShopStatsExpansion
                String testResult = PlaceholderAPI.setPlaceholders(null, "%sklep_staty_test%");
                getLogger().info("Test parsowania (onRequest(null)): '%sklep_staty_test%' -> '" + testResult + "' (oczekiwano 'TEST_OFFLINE')");

            } else {
                getLogger().severe("✗ NIEPOWODZENIE! Ekspansja 'sklep_staty' NIE MOGŁA zostać zarejestrowana!");
                getLogger().severe("Możliwe przyczyny: Inny plugin już zarejestrował 'sklep_staty', lub błąd w PAPI.");
            }
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
        // Wyrejestrowujemy ekspansję
        if (expansion != null && expansion.isRegistered()) {
            expansion.unregister();
            getLogger().info("Ekspansja PlaceholderAPI została wyrejestrowana.");
        }
        
        // Ostateczny zapis danych
        dataManager.saveData();
        getLogger().info("Wszystkie statystyki graczy zostały zapisane przed wyłączeniem pluginu.");
        getLogger().info("Plugin ShopTop został wyłączony!");
    }
}
