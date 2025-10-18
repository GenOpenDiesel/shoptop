package com.twojapakiet.shoptop.placeholders;

import com.twojapakiet.shoptop.data.DataManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ShopStatsExpansion extends PlaceholderExpansion {

    private final DataManager dataManager;

    public ShopStatsExpansion(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sklep_staty";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TwojaNazwa";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0";
    }

    @Override
    public boolean persist() {
        return true;
    }
    
    // DODAJEMY: Metoda canRegister() - niektóre wersje PAPI tego wymagają
    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        // Debugowanie - logujemy każde zapytanie
        System.out.println("[ShopTop Debug] Otrzymano zapytanie o placeholder: '" + params + "'");
        
        if (params == null) {
            return null;
        }
        
        // Czyścimy parametr z białych znaków i konwertujemy na małe litery
        final String cleanParams = params.trim().toLowerCase(Locale.ROOT);
        
        // Dodatkowe debugowanie
        System.out.println("[ShopTop Debug] Po wyczyszczeniu: '" + cleanParams + "'");

        // TEST DIAGNOSTYCZNY
        if (cleanParams.equals("test")) {
            return "TEST_OK";
        }

        if (cleanParams.equals("ilekupilem")) {
            // Jeśli player jest null (np. na hologramie), zwróć "0.00"
            if (player == null) {
                return "0.00";
            }
            double amount = dataManager.getBuyValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }

        if (cleanParams.equals("ilesprzedalem")) {
            // Jeśli player jest null, zwróć "0.00"
            if (player == null) {
                return "0.00";
            }
            double amount = dataManager.getSellValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }

        // Dla nieznanych parametrów logujemy i zwracamy null
        System.out.println("[ShopTop Debug] Nieznany parametr: '" + cleanParams + "'");
        return null;
    }
}
