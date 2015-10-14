package habier.pawnshop.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import habier.pawnshop.helpers.Log;

public class Lang {
	public static String DB_timeOut;
	public static String DB_cannotCreate;
	public static String DB_cannotConnect;
	public static String Error_MoneyIsGone;
	public static String Pawn_checkLimit;
	public static String Pawn_overLimit;
	public static String Pawn_player_sold;
	public static String Pawn_player_buyLimitReached;
	public static String Pawn_player_notEnoughMoney;
	public static String Pawn_player_notEnoughItems;
	public static String Pawn_player_notEnoughInventory;
	public static String Pawn_player_wrongLimit;

	public static void load(File langFile) {
		JsonObject translation = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(langFile);
		} catch (FileNotFoundException e1) {
			Log.write(Level.SEVERE, "JSON translation not found");
		}
		try {
			translation = new JsonParser().parse(scanner.useDelimiter("\\Z").next()).getAsJsonObject();
		} catch (Exception e) {
			Log.write(Level.SEVERE, "Error with the JSON translation");
		}
		scanner.close();

		DB_timeOut = translation.get("DB_timeOut").getAsString();
		DB_cannotCreate = translation.get("DB_cannotCreate").getAsString();
		Error_MoneyIsGone = translation.get("Error_MoneyIsGone").getAsString();
		Pawn_checkLimit = translation.get("Pawn_checkLimit").getAsString();
		Pawn_overLimit = translation.get("Pawn_overLimit").getAsString();
		Pawn_player_sold = translation.get("Pawn_player_sold").getAsString();
		Pawn_player_buyLimitReached = translation.get("Pawn_player_buyLimitReached").getAsString();
		Pawn_player_notEnoughMoney = translation.get("Pawn_player_notEnoughMoney").getAsString();
		Pawn_player_notEnoughItems = translation.get("Pawn_player_notEnoughItems").getAsString();
		Pawn_player_notEnoughInventory = translation.get("Pawn_player_notEnoughInventory").getAsString();
		Pawn_player_wrongLimit = translation.get("Pawn_player_wrongLimit").getAsString();
	}

}
