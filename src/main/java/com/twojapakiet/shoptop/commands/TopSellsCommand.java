package com.twojapakiet.shoptop.commands;

import com.twojapakiet.shoptop.data.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.Map;
import java.util.UUID;

public class TopSellsCommand implements CommandExecutor {

    private final DataManager dataManager;

    public TopSellsCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "--- Top 10 Sprzedających ---");
        Map<UUID, Double> topSells = dataManager.getTopSells(10);

        if (topSells.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Brak danych do wyświetlenia.");
            return true;
        }

        int i = 1;
        for (Map.Entry<UUID, Double> entry : topSells.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            String name = player.getName() != null ? player.getName() : "Nieznany";
            sender.sendMessage(ChatColor.YELLOW + "" + i + ". " + name + " - " + ChatColor.WHITE + String.format("%,.2f", entry.getValue()));
            i++;
        }
        return true;
    }
}
