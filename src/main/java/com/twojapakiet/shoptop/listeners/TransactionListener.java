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
        if (event.getTransactionResult() != Transaction.Result.SUCCESS) {
            return;
        }

        Player player = event.getPlayer();
        double price = event.getPrice();
        Transaction.Type type = event.getTransactionType();

        if (type == Transaction.Type.BUY_SCREEN) {
            dataManager.addBuyValue(player.getUniqueId(), price);
        } else if (type == Transaction.Type.SELL_SCREEN || type == Transaction.Type.SELL_ALL_COMMAND || type == Transaction.Type.SELL_GUI_SCREEN) {
            dataManager.addSellValue(player.getUniqueId(), price);
        }
    }
}
