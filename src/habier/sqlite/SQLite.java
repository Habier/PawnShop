package habier.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import habier.pawnshop.helpers.Log;

public abstract class SQLite {

	protected File dbname = null;
	protected Connection conn;

	public SQLite(String path) {
		dbname = new File(path);
	}

	protected SQLite() {

	}

	/**
	 * Returns the object internal conection
	 */
	public Connection getConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return newConnection();
	}

	static {
		try {
			Class.forName("org.sqlite.JDBC");

		} catch (ClassNotFoundException e) {
			Log.write(Level.SEVERE, "JBDC library not found.");
		}
	}

	/**
	 * Creates and Returns a conection that is not saved in the object. <br>
	 * It's your responsability to close it.
	 */
	public Connection newConnection() {
		checkFile();
		try {
			return DriverManager.getConnection("jdbc:sqlite:" + dbname.toString());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	protected void checkFile() {
		if (!dbname.exists()) {
			try {
				dbname.createNewFile();
				CreateTables();
			} catch (IOException e) {
				Log.write(Level.SEVERE, "File write error: " + dbname + ".db");
			}
		}
	}

	protected void CreateTables() {
	}

	public ResultSet excuteSQL(String sql) {
		conn = getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			return result;
		} catch (SQLException e) {
			Log.write(Level.SEVERE, "Unable to retreive connection", e);
		}

		return null;
	}

	protected boolean executeUpdate(String sql) {
		boolean OK = true;
		Statement s = null;
		conn = getConnection();
		try {
			s = conn.createStatement();
			s.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			OK = false;
		}
		close(s);
		return OK;
	}

	public void close(Statement s) {
		try {
			if (s != null)
				s.close();
		} catch (SQLException e) {
			Log.write(Level.WARNING, "Error closing an Statement");
		}
	}

	public void close(ResultSet result) {
		try {
			close(result.getStatement());
		} catch (SQLException e) {
			Log.write(Level.WARNING, "Error closing an Statement");
		}
	}

	public boolean commit() {
		try {
			conn.commit();
			return true;
		} catch (SQLException e) {
			rollback();
		}
		return false;
	}

	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
