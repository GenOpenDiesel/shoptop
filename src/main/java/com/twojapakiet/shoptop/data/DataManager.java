package com.twojapakiet.shoptop.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private final JavaPlugin plugin;
    private File dataFile;
    private FileConfiguration dataConfig;

    private final Map<UUID, Double> buyStats = new ConcurrentHashMap<>();
    private final Map<UUID, Double> sellStats = new ConcurrentHashMap<>();

    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createDataFile();
    }

    public void addBuyValue(UUID uuid, double value) {
        buyStats.merge(uuid, value, Double::sum);
    }

    public void addSellValue(UUID uuid, double value) {
        sellStats.merge(uuid, value, Double::sum);
    }

    public double getBuyValue(UUID uuid) {
        return buyStats.getOrDefault(uuid, 0.0);
    }

    public double getSellValue(UUID uuid) {
        return sellStats.getOrDefault(uuid, 0.0);
    }

    public void loadData() {
        if (dataConfig.contains("stats.buy")) {
            dataConfig.getConfigurationSection("stats.buy").getKeys(false).forEach(uuidString -> {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    double amount = dataConfig.getDouble("stats.buy." + uuidString);
                    buyStats.put(uuid, amount);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Błędny format UUID w data.yml: " + uuidString);
                }
            });
        }
        if (dataConfig.contains("stats.sell")) {
            dataConfig.getConfigurationSection("stats.sell").getKeys(false).forEach(uuidString -> {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    double amount = dataConfig.getDouble("stats.sell." + uuidString);
                    sellStats.put(uuid, amount);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Błędny format UUID w data.yml: " + uuidString);
                }
            });
        }
    }

    public synchronized void saveData() {
        // Tworzymy kopię, aby uniknąć problemów z współbieżnością podczas zapisu
        Map<UUID, Double> buyStatsCopy = new ConcurrentHashMap<>(buyStats);
        Map<UUID, Double> sellStatsCopy = new ConcurrentHashMap<>(sellStats);

        // Czyszczenie starej sekcji
        dataConfig.set("stats", null);

        buyStatsCopy.forEach((uuid, value) -> dataConfig.set("stats.buy." + uuid.toString(), value));
        sellStatsCopy.forEach((uuid, value) -> dataConfig.set("stats.sell." + uuid.toString(), value));

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
