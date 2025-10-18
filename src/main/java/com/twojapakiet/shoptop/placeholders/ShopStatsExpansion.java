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
        // ZMIANA: Dodajemy .trim() aby usunąć przypadkowe białe znaki
        final String cleanParams = params.trim().toLowerCase(Locale.ROOT);

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

        // Jeśli parametr jest nieznany, zwracamy null.
        // PAPI pokaże wtedy literalny tekst placeholdera (np. %sklep_staty_cos%)
        return null;
    }
}
