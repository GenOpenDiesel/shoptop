package com.twojapakiet.shoptop.commands;

import com.twojapakiet.shoptop.ShopTop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final ShopTop plugin;

    public ReloadCommand(ShopTop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("shoptop.admin")) {
                sender.sendMessage(ChatColor.RED + "Nie masz uprawnień do wykonania tej komendy.");
                return true;
            }

            // Wywołanie metody przeładowania z głównej klasy
            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.GREEN + "Plugin ShopTop został pomyślnie przeładowany.");
            return true;
        }
        
        sender.sendMessage(ChatColor.YELLOW + "Użycie: /" + label + " reload");
        return false;
    }
}
