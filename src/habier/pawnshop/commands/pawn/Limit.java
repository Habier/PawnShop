package habier.pawnshop.commands.pawn;

import org.bukkit.entity.Player;

import habier.pawnshop.main.Lang;

public final class Limit {
	public static boolean exec(Player p) {
		int limit = PawnHelper.getPlayerData(p).sold;
		p.sendMessage(String.format(Lang.Pawn_checkLimit, limit));
		return true;
	}
}
