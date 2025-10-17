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
        if (player == null) {
            return "";
        }

        return switch (params.toLowerCase(Locale.ROOT)) {
            // %sklep_staty_ilekupilem%
            case "ilekupilem" -> {
                double amount = dataManager.getBuyValue(player.getUniqueId());
                yield String.format(Locale.US, "%,.2f", amount);
            }
            // %sklep_staty_ilesprzedalem%
            case "ilesprzedalem" -> {
                double amount = dataManager.getSellValue(player.getUniqueId());
                yield String.format(Locale.US, "%,.2f", amount);
            }
            default -> null;
        };
    }
}
