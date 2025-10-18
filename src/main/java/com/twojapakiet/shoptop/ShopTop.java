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
        // Opóźniamy rejestrację PAPI.
        // Opóźnienie 1 tick (1L) jest prawie zawsze niewystarczające i powoduje "race condition".
        // PlaceholderAPI jest "włączone", ale nie jest gotowe na przyjmowanie nowych dodatków.
        // Zwiększamy opóźnienie do 2 sekund (40 ticków), aby dać serwerowi i PAPI czas na pełne załadowanie.
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {

                // WAŻNE: Sprawdzamy, czy rejestracja się faktycznie powiodła.
                // Metoda .register() zwraca boolean informujący o sukcesie.
                boolean success = new ShopStatsExpansion(dataManager).register();

                if (success) {
                    getLogger().info("Pomyślnie zintegrowano i ZAREJESTROWANO dodatek 'sklep_staty' w PlaceholderAPI.");
                } else {
                    // Ten błąd jest kluczowy. Jeśli go zobaczysz, PAPI odrzuciło dodatek.
                    getLogger().severe("Próbowano zintegrować się z PAPI, ale rejestracja dodatku 'sklep_staty' NIE powiodła się (register() zwróciło false)!");
                }
                
            } else {
                getLogger().warning("Nie znaleziono PlaceholderAPI! Placeholdery nie będą działać.");
            }
        }, 40L); // <-- ZMIANA: Z 1L na 40L (opóźnienie o 2 sekundy)
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
