package com.twojapakiet.shoptop.listeners;

import com.twojapakiet.shoptop.data.DataManager;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.api.events.TransactionType;
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
        // Nowsze API nie używa już `Transaction.Result`, sprawdzamy `isSuccessful()`
        if (!event.isSuccessful()) {
            return;
        }

        Player player = event.getPlayer();
        double price = event.getPrice();
        TransactionType type = event.getTransactionType();

        if (type == TransactionType.BUY_ITEM || type == TransactionType.BUY_STACK) {
            dataManager.addBuyValue(player.getUniqueId(), price);
        } else if (type == TransactionType.SELL_ITEM || type == TransactionType.SELL_STACK || type == TransactionType.SELL_ALL_ITEMS) {
            dataManager.addSellValue(player.getUniqueId(), price);
        }
    }
}
