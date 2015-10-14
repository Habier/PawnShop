package habier.pawnshop.main;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import habier.pawnshop.commands.pawn.CommandPawn;
import habier.pawnshop.database.PawnDB;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class PawnPlugin extends JavaPlugin {
	public static PawnDB PawnDB;
	FileConfiguration config = getConfig();

	// public static Permission permission = null;
	public static Economy econ = null;
	public static Chat chat = null;
	public static Logger logs;

	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		logs = getLogger();
		setupEconomy();
		saveResource("english.json", false);
		Configurer.loadConfig(config);// set default configuration
		saveConfig();

		this.getCommand("pawn").setExecutor(new CommandPawn());
		PawnDB = new PawnDB(getDataFolder() + config.getName() + "/DB.db");
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {

	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			econ = economyProvider.getProvider();
		}

		return (econ != null);
	}

}
