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

    // Zmieniamy DataManager na główną klasę pluginu, tak jak w przykładzie
    private final ShopTop plugin;

    // Konstruktor przyjmuje główną klasę pluginu
    public ShopStatsExpansion(ShopTop plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sklep_staty";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TwojaNazwa"; // Możesz zmienić na "Fragmer2" jeśli chcesz
    }

    @Override
    public @NotNull String getVersion() {
        return "2.1"; // Zmieniona wersja po poprawce
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
        return Arrays.asList(
            "test",
            "ilekupilem", 
            "ilesprzedalem"
        );
    }

    // ZOSTAWIMY TYLKO TĘ METODĘ - tak jak w przykładzie
    // PAPI automatycznie użyje jej dla graczy online i offline
    @Override
    public String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        
        if (params.equalsIgnoreCase("test")) {
            return "TEST_Z_ONREQUEST"; // Zwracamy z tej metody
        }
        
        if (player == null) {
            // Jeśli PAPI z jakiegoś powodu wyśle null gracza
            return "0.00";
        }
        
        // Pobieramy DataManager z instancji pluginu
        if (params.equalsIgnoreCase("ilekupilem")) {
            double amount = plugin.getDataManager().getBuyValue(player.getUniqueId());
            return String.format(Locale.US, "%,.2f", amount);
        }
        
        if (params.equalsIgnoreCase("ilesprzedalem")) {
            double amount = plugin.getDataManager().getSellValue(player.getUniqueId());  
            return String.format(Locale.US, "%,.2f", amount);
        }
        
        // Tak jak w przykładzie, zwracamy null dla nieznanego parametru
        return null; 
    }
}
