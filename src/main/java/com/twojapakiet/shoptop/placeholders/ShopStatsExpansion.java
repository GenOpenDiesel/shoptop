package com.twojapakiet.shoptop.placeholders;

import com.twojapakiet.shoptop.data.DataManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class ShopStatsExpansion extends PlaceholderExpansion {

    private final DataManager dataManager;

    public ShopStatsExpansion(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sklep_staty"; // Prefix placeholderów
    }

    @Override
    public @NotNull String getAuthor() {
        return "TwojaNazwa";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // %sklep_staty_ilekupilem%
        if (params.equalsIgnoreCase("ilekupilem")) {
            double amount = dataManager.getBuyValue(player.getUniqueId());
            return String.format("%,.2f", amount);
        }

        // %sklep_staty_ilesprzedalem%
        if (params.equalsIgnoreCase("ilesprzedalem")) {
            double amount = dataManager.getSellValue(player.getUniqueId());
            return String.format("%,.2f", amount);
        }

        return null; // Zwróć null, jeśli placeholder jest niepoprawny
    }
}
