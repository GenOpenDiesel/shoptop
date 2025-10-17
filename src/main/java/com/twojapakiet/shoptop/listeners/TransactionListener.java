package com.twojapakiet.shoptop.listeners;

import com.twojapakiet.shoptop.data.DataManager;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TransactionListener implements Listener {

    private final DataManager dataManager;

    public TransactionListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onTransaction(PostTransactionEvent event) {
        // Poprawny sposób sprawdzenia, czy transakcja się powiodła
        if (event.getTransactionResult() != Transaction.Result.SUCCESS) {
            return;
        }

        Player player = event.getPlayer();
        double price = event.getPrice();
        Transaction.Type type = event.getTransactionType();

        // Logika rozróżniająca kupno od sprzedaży na podstawie poprawnych enumów
        if (isBuyTransaction(type)) {
            dataManager.addBuyValue(player.getUniqueId(), price);
        } else if (isSellTransaction(type)) {
            dataManager.addSellValue(player.getUniqueId(), price);
        }
    }

    private boolean isBuyTransaction(Transaction.Type type) {
        return type == Transaction.Type.BUY_SCREEN ||
               type == Transaction.Type.BUY_STACKS_SCREEN ||
               type == Transaction.Type.QUICK_BUY ||
               type == Transaction.Type.SHOPSTAND_BUY_SCREEN;
    }

    private boolean isSellTransaction(Transaction.Type type) {
        return type == Transaction.Type.SELL_GUI_SCREEN ||
               type == Transaction.Type.SELL_ALL_COMMAND ||
               type == Transaction.Type.SELL_ALL_SCREEN ||
               type == Transaction.Type.SELL_SCREEN ||
               type == Transaction.Type.QUICK_SELL ||
               type == Transaction.Type.SHOPSTAND_SELL_SCREEN ||
               type == Transaction.Type.AUTO_SELL_CHEST;
    }
}
