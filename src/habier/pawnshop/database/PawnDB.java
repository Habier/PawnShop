package habier.pawnshop.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import habier.pawnshop.helpers.Log;
import habier.pawnshop.main.Configurer;
import habier.pawnshop.main.Lang;
import habier.sqlite.SQLite;

public class PawnDB extends SQLite {
	protected final String DBTable = "CREATE TABLE IF NOT EXISTS Users (" + "`uuid` BINARY(16)  NOT NULL,"
			+ "`sold` int(11) NOT NULL," + "PRIMARY KEY (`UUID`)" + ");";

	public PawnDB(String filename) {
		super(filename);
	}

	public PlayerData getPlayerData(UUID uuid) {
		ResultSet result = excuteSQL("Select * FROM Users where uuid = '" + uuid + "';");
		PlayerData aux = null;
		try {
			if (result.next()) {
				aux = new PlayerData(UUID.nameUUIDFromBytes(result.getBytes("uuid")), result.getInt("sold"));
			} else {
				if (executeUpdate("INSERT INTO Users VALUES ('" + uuid + "','" + Configurer.maxSell + "');"))
					aux = getPlayerData(uuid);
				else
					Log.write(Level.SEVERE, Lang.DB_cannotCreate);

			}
		} catch (SQLException e) {
			Log.write(Level.SEVERE, Lang.DB_timeOut);
		}

		close(result);
		return aux;
	}

	public boolean updateLimit(UUID uuid, int qty) {
		return executeUpdate("UPDATE Users SET sold='" + qty + "' WHERE uuid='" + uuid + "';");
	}

	@Override
	protected void CreateTables() {
		executeUpdate(DBTable);
	}
}
