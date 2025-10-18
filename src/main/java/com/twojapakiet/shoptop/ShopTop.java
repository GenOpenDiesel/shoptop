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

        // Rejestracja PlaceholderAPI z debugowaniem
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI znalezione! Próba rejestracji...");
            
            // Tworzymy ekspansję
            expansion = new ShopStatsExpansion(dataManager);
            
            // Próbujemy zarejestrować natychmiast
            boolean immediateSuccess = expansion.register();
            getLogger().info("Natychmiastowa próba rejestracji: " + (immediateSuccess ? "SUKCES" : "NIEPOWODZENIE"));
            
            if (!immediateSuccess) {
                // Jeśli nie udało się natychmiast, próbujemy z opóźnieniem
                getLogger().info("Próba rejestracji z opóźnieniem 2 sekund...");
                
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    boolean delayedSuccess = expansion.register();
                    getLogger().info("Opóźniona próba rejestracji: " + (delayedSuccess ? "SUKCES" : "NIEPOWODZENIE"));
                    
                    // Sprawdzamy czy ekspansja jest zarejestrowana
                    if (PlaceholderAPI.getRegisteredExpansions().stream()
                            .anyMatch(exp -> exp.getIdentifier().equals("sklep_staty"))) {
                        getLogger().info("✓ Ekspansja 'sklep_staty' jest ZAREJESTROWANA w PlaceholderAPI!");
                        getLogger().info("Dostępne placeholdery:");
                        getLogger().info("  - %sklep_staty_test%");
                        getLogger().info("  - %sklep_staty_ilekupilem%");
                        getLogger().info("  - %sklep_staty_ilesprzedalem%");
                    } else {
                        getLogger().severe("✗ Ekspansja 'sklep_staty' NIE JEST zarejestrowana!");
                        getLogger().severe("Sprawdź czy PlaceholderAPI jest poprawnie zainstalowane.");
                    }
                }, 40L);
            } else {
                getLogger().info("✓ Ekspansja zarejestrowana natychmiast!");
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
