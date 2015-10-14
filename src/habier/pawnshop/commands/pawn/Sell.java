package habier.pawnshop.commands.pawn;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import habier.pawnshop.database.PlayerData;
import habier.pawnshop.helpers.Log;
import habier.pawnshop.main.Configurer;
import habier.pawnshop.main.Lang;
import habier.pawnshop.main.PawnPlugin;
import net.milkbowl.vault.economy.EconomyResponse;

public final class Sell {
	public static boolean exec(Player p, int number) {

		Inventory inv = p.getInventory();
		Material targetItem = Material.DIAMOND;

		if (!(inv.contains(targetItem, number))) { // Check item quatity.
			p.sendMessage(Lang.Pawn_player_notEnoughItems);
			return true;
		}

		PlayerData userData = PawnHelper.getPlayerData(p); // Get data from
															// database
		if (userData == null)
			return true;

		if (!PawnHelper.sellLimit(number, userData.sold)) {
			p.sendMessage(Lang.Pawn_overLimit);
			return true;
		}

		if (!PawnPlugin.PawnDB.updateLimit(p.getUniqueId(), userData.sold - number)) {
			p.sendMessage(Lang.DB_timeOut);
			return true;
		}

		double money = number * Configurer.moneyPerSell;
		EconomyResponse r = PawnPlugin.econ.depositPlayer(p, money); // giveMoney

		if (r.transactionSuccess()) {
			// All OK. take the items and give the money
			PawnHelper.removeItems(p, number, targetItem);
			p.sendMessage(String.format(Lang.Pawn_player_sold, money, PawnPlugin.econ.format(r.balance)));
		} else {// Creepy error
			p.sendMessage(String.format("An error occured: %s", r.errorMessage));
			if (!PawnPlugin.PawnDB.updateLimit(p.getUniqueId(), userData.sold + number)) {
				p.sendMessage(Lang.Pawn_player_wrongLimit);
				Log.write(Level.SEVERE, p.getName() + " lost a pawn-limit of " + number + " due to a BUG");
				return true;
			}

		}

		return true;
	}
}
