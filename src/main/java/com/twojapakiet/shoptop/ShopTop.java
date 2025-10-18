package com.twojapakiet.shoptop;

import com.twojapakiet.shoptop.data.DataManager;
import com.twojapakiet.shoptop.listeners.TransactionListener;
import com.twojapakiet.shoptop.placeholders.ShopStatsExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopTop extends JavaPlugin {

    private DataManager dataManager;
    private static final long SAVE_INTERVAL = 20L * 60 * 5; // 5 minut

    @Override
    public void onEnable() {
        this.dataManager = new DataManager(this);
        dataManager.loadData();

        getServer().getPluginManager().registerEvents(new TransactionListener(dataManager), this);

        // ### POCZĄTEK POPRAWKI ###
        // Opóźniamy rejestrację PAPI o 1 tick serwera.
        // Daje to pewność, że PlaceholderAPI jest już w pełni załadowane i gotowe
        // na przyjmowanie nowych rozszerzeń (Expansion).
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                new ShopStatsExpansion(dataManager).register();
                getLogger().info("Pomyślnie zintegrowano z PlaceholderAPI.");
            } else {
                getLogger().warning("Nie znaleziono PlaceholderAPI! Placeholdery nie będą działać.");
            }
        }, 1L); // 1L = opóźnienie o 1 tick
        // ### KONIEC POPRAWKI ###

        // Uruchomienie asynchronicznego, cyklicznego zapisu danych
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            dataManager.saveData();
            getLogger().info("Dane statystyk zostały zapisane.");
        }, SAVE_INTERVAL, SAVE_INTERVAL);


        getLogger().info("Plugin ShopTop został włączony!");
    }

    @Override
    public void onDisable() {
        // Ostateczny zapis danych przy wyłączaniu lub restarcie serwera.
        // Ta metoda gwarantuje, że wszystkie statystyki zostaną zapisane.
        dataManager.saveData();
        getLogger().info("Wszystkie statystyki graczy zostały zapisane przed wyłączeniem pluginu.");
        getLogger().info("Plugin ShopTop został wyłączony!");
    }
}
