package habier.pawnshop.commands.pawn;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import habier.pawnshop.database.PlayerData;
import habier.pawnshop.helpers.Log;
import habier.pawnshop.main.Configurer;
import habier.pawnshop.main.Lang;
import habier.pawnshop.main.PawnPlugin;

public final class PawnHelper {

	public static PlayerData getPlayerData(Player p) {
		PlayerData userData = PawnPlugin.PawnDB.getPlayerData(p.getUniqueId());

		if (userData == null) {
			p.sendMessage(Lang.DB_timeOut);
			Log.write(Level.SEVERE, p.getName() + " Cannot be found nor created in the DB");
			return null;
		}

		return userData;
	}

	public static boolean buyLimit(int number, int data) {
		if ((number + data) <= (Configurer.maxSell + Configurer.maxBuy))
			return true;

		return false;
	}

	public static boolean sellLimit(int number, int data) {
		if ((data - number) > -1)
			return true;

		return false;
	}

	public static void removeItems(Player p, int qty, Material material) {
		ItemStack[] inventory = p.getInventory().getContents();
		for (int i = 0; i < inventory.length && qty > 0; i++) {
			if (inventory[i] != null && inventory[i].getType() == material) {
				if (inventory[i].getAmount() <= qty) {
					qty -= inventory[i].getAmount();
					inventory[i] = null;
				} else {
					inventory[i].setAmount(inventory[i].getAmount() - qty);
					qty = 0;
				}
			}
		}
		p.getInventory().setContents(inventory);
		p.updateInventory();
	}

	public static int countInventoryFreeSpace(Inventory inv) {
		int freeSpace = 0;
		ItemStack[] inventory = inv.getContents();

		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null) {
				freeSpace++;
			}
		}
		return freeSpace;
	}

	public static boolean checkEnoughInventory(Inventory inv, int qty, Material material) {
		int freeSlots = countInventoryFreeSpace(inv);

		if ((qty + material.getMaxStackSize() - 1) / material.getMaxStackSize() > freeSlots)
			return false;

		return true;
	}

	public static void giveItems(Inventory inv, int number, Material targetItem) {
		while (number > targetItem.getMaxStackSize() - 1) {
			inv.addItem(new ItemStack(targetItem, targetItem.getMaxStackSize()));
			number -= targetItem.getMaxStackSize();
		}
		if (number > 0)
			inv.addItem(new ItemStack(targetItem, number));

	}
}
