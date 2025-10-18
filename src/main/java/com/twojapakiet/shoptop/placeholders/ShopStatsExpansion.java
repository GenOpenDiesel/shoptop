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

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        // Używamy prostego if/else zamiast switcha dla pewności
        String lowerParams = params.toLowerCase(Locale.ROOT);

        if (lowerParams.equals("ilekupilem")) {
            // Jeśli player jest null (np. na hologramie), zwróć "0.00" zgodnie z prośbą
            if (player == null) {
                return "0.00";
            }
            // getBuyValue dzięki getOrDefault (w DataManager) zwróci 0.0 dla nowego gracza
            double amount = dataManager.getBuyValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }

        if (lowerParams.equals("ilesprzedalem")) {
            // Jeśli player jest null, zwróć "0.00"
            if (player == null) {
                return "0.00";
            }
            // getSellValue dzięki getOrDefault (w DataManager) zwróci 0.0 dla nowego gracza
            double amount = dataManager.getSellValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }

        // Jeśli parametr jest nieznany (np. %sklep_staty_cos%),
        // zwracamy null, aby PAPI pokazało literal string (to jest poprawne zachowanie).
        return null;
    }
}
