package habier.pawnshop.commands.pawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import habier.pawnshop.helpers.Parser;

public class CommandPawn implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		int number;

		if (command.getName().compareToIgnoreCase("pawn") != 0)
			return false;
		if (!(sender instanceof Player))
			return false;
		if (args.length == 0)
			return Limit.exec((Player) sender);

		switch (args[0].toLowerCase()) {
		case "buy":
			if (args.length != 2)
				return false;
			number = Parser.getNumber(args[1]);
			if (number < 1) {
				return false;
			}
			return Buy.exec((Player) sender, number);

		case "sell":
			if (args.length != 2)
				return false;
			number = Parser.getNumber(args[1]);
			if (number < 1) {
				return false;
			}
			return Sell.exec((Player) sender, number);

		default:
			return false;
		}
	}

}
