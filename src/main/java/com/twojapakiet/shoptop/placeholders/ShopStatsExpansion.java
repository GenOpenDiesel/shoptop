package com.twojapakiet.shoptop.placeholders;

import com.twojapakiet.shoptop.data.DataManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ShopStatsExpansion extends PlaceholderExpansion {

    private final DataManager dataManager;

    public ShopStatsExpansion(DataManager dataManager) {
        this.dataManager = dataManager;
        Bukkit.getLogger().info("[ShopTop] Tworzenie instancji ShopStatsExpansion");
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
    public boolean canRegister() {
        return true;
    }
    
    // Dodajemy listę placeholderów
    @Override
    public @NotNull List<String> getPlaceholders() {
        return Arrays.asList(
            "test",
            "ilekupilem", 
            "ilesprzedalem"
        );
    }

    // Główna metoda - z Player
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        Bukkit.getLogger().info("[ShopTop] onPlaceholderRequest z Player: '" + params + "' dla gracza: " + (player != null ? player.getName() : "null"));
        
        if (params.equalsIgnoreCase("test")) {
            return "TEST_Z_PLAYER";
        }
        
        if (player == null) {
            return "BRAK_GRACZA";
        }
        
        if (params.equalsIgnoreCase("ilekupilem")) {
            double amount = dataManager.getBuyValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }
        
        if (params.equalsIgnoreCase("ilesprzedalem")) {
            double amount = dataManager.getSellValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }
        
        return "NIEZNANY_PARAMETR";
    }

    // Metoda z OfflinePlayer - MUSI być zaimplementowana
    @Override
    public String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        Bukkit.getLogger().info("[ShopTop] onRequest z OfflinePlayer: '" + params + "' dla gracza: " + (player != null ? player.getName() : "null"));
        
        if (params.equalsIgnoreCase("test")) {
            return "TEST_OFFLINE";
        }
        
        if (player == null) {
            return "0.00";
        }
        
        if (params.equalsIgnoreCase("ilekupilem")) {
            double amount = dataManager.getBuyValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }
        
        if (params.equalsIgnoreCase("ilesprzedalem")) {
            double amount = dataManager.getSellValue(player.getUniqueId());  
            return String.format(Locale.US, "%,.2f", amount);
        }
        
        Bukkit.getLogger().warning("[ShopTop] Nieznany placeholder: " + params);
        return null;
    }
}
