package com.twojapakiet.shoptop.placeholders;

import com.twojapakiet.shoptop.ShopTop; // Importujemy główną klasę
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ShopStatsExpansion extends PlaceholderExpansion {

    private final ShopTop plugin;

    public ShopStatsExpansion(ShopTop plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        // ZMIANA 1: Zmieniono identyfikator z "sklep_staty" na "shoptop"
        return "shoptop";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TwojaNazwa"; 
    }

    @Override
    public @NotNull String getVersion() {
        return "2.2"; // Podbita wersja po zmianach
    }

    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public boolean canRegister() {
        return true;
    }
    
    @Override
    public @NotNull List<String> getPlaceholders() {
        // ZMIANA 2: Aktualizacja listy dla PAPI (opcjonalne, ale dobra praktyka)
        return Arrays.asList(
            "test",
            "kupile", 
            "sellile"
        );
    }

    @Override
    public String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        
        if (params.equalsIgnoreCase("test")) {
            return "TEST_Z_ONREQUEST"; 
        }
        
        if (player == null) {
            return "0.00";
        }
        
        // ZMIANA 3: Zmieniono "ilekupilem" na "kupile"
        if (params.equalsIgnoreCase("kupile")) {
            double amount = plugin.getDataManager().getBuyValue(player.getUniqueId());
            // POPRAWKA: Usunięto przecinek (,) z formatowania, aby Topper mógł parsować liczbę
            return String.format(Locale.US, "%.2f", amount);
        }
        
        // ZMIANA 4: Zmieniono "ilesprzedalem" na "sellile"
        if (params.equalsIgnoreCase("sellile")) {
            double amount = plugin.getDataManager().getSellValue(player.getUniqueId());  
            // POPRAWKA: Usunięto przecinek (,) z formatowania, aby Topper mógł parsować liczbę
            return String.format(Locale.US, "%.2f", amount);
        }
        
        return null; 
    }
}
