package habier.pawnshop.main;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class Configurer {
	public static int maxSell;
	public static int maxBuy;
	public static double moneyPerSell;
	public static double moneyPerBuy;

	public static void loadConfig(FileConfiguration config) {
		setDefaults(config);
		readConfig(config);

	}

	public static void setDefaults(FileConfiguration config) {
		config.addDefault("cadena", "Database isn't responding.");
		config.addDefault("language", "english.json");
		config.addDefault("maxSell", 16);
		config.addDefault("maxBuy", 0);
		config.addDefault("moneyPerSell", 1000);
		config.addDefault("moneyPerBuy", 1000);
		config.options().copyDefaults(true);
	}

	public static void readConfig(FileConfiguration config) {
		maxSell = config.getInt("maxSell");
		maxBuy = config.getInt("maxBuy");
		moneyPerSell = config.getInt("moneyPerSell");
		moneyPerBuy = config.getInt("moneyPerBuy");
		Lang.load(new File("plugins/PawnPlugin/" + config.getString("language")));
	}
}
