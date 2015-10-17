package habier.pawnshop.commands.pawn;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import habier.pawnshop.database.PawnDB;
import habier.pawnshop.database.PlayerData;
import habier.pawnshop.helpers.Log;
import habier.pawnshop.main.Configurer;
import habier.pawnshop.main.Lang;
import habier.pawnshop.main.PawnPlugin;
import net.milkbowl.vault.economy.EconomyResponse;

public class Buy {
	static boolean exec(Player p, int number) {
		Inventory inv = p.getInventory();

		if (!PawnHelper.checkEnoughInventory(inv, number, Configurer.targetItem)) {
			p.sendMessage(Lang.Pawn_player_notEnoughInventory);
			return true;
		}

		PlayerData userData = PawnHelper.getPlayerData(p);
		if (userData == null)
			return true;

		if (!PawnHelper.buyLimit(number, userData.sold)) {
			p.sendMessage(Lang.Pawn_player_buyLimitReached);
			return true;
		}

		double money = number * Configurer.moneyPerBuy;
		if (PawnPlugin.econ.getBalance(p) < money) {
			p.sendMessage(Lang.Pawn_player_notEnoughMoney);
			return true;
		}

		PawnDB db = new PawnDB(); // Comienza la transaccion.
		try {
			db.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			Log.write(Level.SEVERE, p.getName() + " Cannot disable autoCommit");
			p.sendMessage(Lang.DB_timeOut);
			e.printStackTrace();
			db.closeConnection();
			return true;
		}

		if (!db.updateLimit(p.getUniqueId(), number + userData.sold)) {
			p.sendMessage(Lang.DB_timeOut);
			Log.write(Level.SEVERE, p.getName() + " Cannot be updated in the DB");
			db.closeConnection();
			return true;
		}

		if (!db.updateLimit(userData.uuid, number + userData.sold)) {
			p.sendMessage(Lang.DB_cannotCreate);
			db.closeConnection();
			return true;
		}
		EconomyResponse r = PawnPlugin.econ.withdrawPlayer(p, money); // pay!

		if (r.transactionSuccess())
			if (db.commit()) {
				// all OK. take the items and give the money
				PawnHelper.giveItems(inv, number, Configurer.targetItem);
				p.sendMessage(String.format(Lang.Pawn_player_bought, money, PawnPlugin.econ.format(r.balance)));
				p.sendMessage(Lang.DB_timeOut);
			} else {
				db.rollback();

				if (!PawnPlugin.econ.depositPlayer(p, money).transactionSuccess()) {
					// gives moneyback
					p.sendMessage(Lang.Error_MoneyIsGone);
					Log.write(Level.SEVERE, "Player: " + p.getName() + " with UUID: " + p.getUniqueId().toString()
							+ " has lost " + money + " money");
				}
			}
		else {
			db.rollback();
			p.sendMessage(String.format("An error occured: %s", r.errorMessage));
		}
		db.closeConnection();
		return true;
	}
}
