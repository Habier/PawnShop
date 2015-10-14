package habier.pawnshop.helpers;

import java.util.logging.Level;

import habier.pawnshop.main.PawnPlugin;

public final class Log {
	public static void write(Level level, String s, Exception e) {
		PawnPlugin.logs.log(level, s, e);
	}

	public static void write(Level level, String s) {
		PawnPlugin.logs.log(level, s);
	}

}
