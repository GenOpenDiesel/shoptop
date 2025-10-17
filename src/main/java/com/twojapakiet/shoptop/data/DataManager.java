package com.twojapakiet.shoptop.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {

    private final JavaPlugin plugin;
    private FileConfiguration dataConfig;
    private File dataFile;

    private final Map<UUID, Double> buyStats = new HashMap<>();
    private final Map<UUID, Double> sellStats = new HashMap<>();

    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createDataFile();
    }

    public void addBuyValue(UUID uuid, double value) {
        buyStats.put(uuid, buyStats.getOrDefault(uuid, 0.0) + value);
    }

    public void addSellValue(UUID uuid, double value) {
        sellStats.put(uuid, sellStats.getOrDefault(uuid, 0.0) + value);
    }
    
    // Metody dla Placeholderów
    public double getBuyValue(UUID uuid) {
        return buyStats.getOrDefault(uuid, 0.0);
    }
    
    public double getSellValue(UUID uuid) {
        return sellStats.getOrDefault(uuid, 0.0);
    }

    public LinkedHashMap<UUID, Double> getTopBuys(int limit) {
        return buyStats.entrySet().stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public LinkedHashMap<UUID, Double> getTopSells(int limit) {
        return sellStats.entrySet().stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void loadData() {
        if (dataConfig.contains("stats.buy")) {
            dataConfig.getConfigurationSection("stats.buy").getKeys(false).forEach(uuidString -> {
                UUID uuid = UUID.fromString(uuidString);
                double value = dataConfig.getDouble("stats.buy." + uuidString);
                buyStats.put(uuid, value);
            });
        }
        if (dataConfig.contains("stats.sell")) {
            dataConfig.getConfigurationSection("stats.sell").getKeys(false).forEach(uuidString -> {
                UUID uuid = UUID.fromString(uuidString);
                double value = dataConfig.getDouble("stats.sell." + uuidString);
                sellStats.put(uuid, value);
            });
        }
    }

    public void saveData() {
        dataConfig.set("stats", null);
        buyStats.forEach((uuid, value) -> dataConfig.set("stats.buy." + uuid.toString(), value));
        sellStats.forEach((uuid, value) -> dataConfig.set("stats.sell." + uuid.toString(), value));

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie można zapisać danych do pliku data.yml!");
            e.printStackTrace();
        }
    }

    private void createDataFile() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }
}
