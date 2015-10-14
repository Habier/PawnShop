package habier.pawnshop.helpers;

public final class Parser {

	public static int getNumber(String s) {
		int number;
		
		try {
			number = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return -1;
		} catch (NullPointerException e) {
			return -2;
		}
		
		return number;
	}
}
