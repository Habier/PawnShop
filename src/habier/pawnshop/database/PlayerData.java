package habier.pawnshop.database;

import java.util.UUID;

public class PlayerData {
	public UUID uuid;
	public int sold;

	public PlayerData(UUID string, int sold) {
		this.uuid = string;
		this.sold = sold;
	}
}
