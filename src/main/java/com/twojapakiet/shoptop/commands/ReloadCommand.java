package com.twojapakiet.shoptop.commands;

import com.twojapakiet.shoptop.ShopTop;
import org.bukkit.ChatColor;
// Zmieniamy importy - teraz używamy org.bukkit.command.Command
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.List;

// Zmieniamy 'implements CommandExecutor' na 'extends Command'
public class ReloadCommand extends Command {

    private final ShopTop plugin;

    public ReloadCommand(ShopTop plugin) {
        // To jest nazwa komendy, która będzie wpisywana
        super("shoptop");
        this.plugin = plugin;

        // Ustawienia komendy, które były w plugin.yml, przenosimy tutaj
        setDescription("Główna komenda pluginu ShopTop");
        setPermission("shoptop.admin");
        setAliases(List.of("st")); // aliasy
        setUsage("/<command> reload");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // Sprawdzenie uprawnień
        if (!testPermission(sender)) {
            // Wiadomość o braku uprawnień jest wysyłana automatycznie przez testPermission
            // (na podstawie tego, co jest w bukkit.yml), ale możemy też dodać własną:
            sender.sendMessage(ChatColor.RED + "Nie masz uprawnień do wykonania tej komendy.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.GREEN + "Plugin ShopTop został pomyślnie przeładowany.");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Użycie: /" + commandLabel + " reload");
        return false;
    }

    // Dodajemy też prosty tab-completer
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // Podpowiadamy "reload" jako jedyną opcję
            return List.of("reload");
        }
        // Domyślnie nie podpowiadamy nic więcej
        return List.of();
    }
}
