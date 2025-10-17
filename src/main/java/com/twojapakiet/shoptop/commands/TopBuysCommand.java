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

public class TopBuysCommand implements CommandExecutor {

    private final DataManager dataManager;

    public TopBuysCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "--- Top 10 Kupujących ---");
        Map<UUID, Double> topBuys = dataManager.getTopBuys(10);

        if (topBuys.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Brak danych do wyświetlenia.");
            return true;
        }

        int i = 1;
        for (Map.Entry<UUID, Double> entry : topBuys.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            String name = player.getName() != null ? player.getName() : "Nieznany";
            sender.sendMessage(ChatColor.YELLOW + "" + i + ". " + name + " - " + ChatColor.WHITE + String.format("%,.2f", entry.getValue()));
            i++;
        }
        return true;
    }
}
