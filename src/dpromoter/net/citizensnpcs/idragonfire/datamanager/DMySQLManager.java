package net.citizensnpcs.idragonfire.datamanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.citizensnpcs.idragonfire.keys.DKey;
import net.citizensnpcs.idragonfire.values.DValue;
import net.citizensnpcs.properties.properties.UtilityProperties;
import net.citizensnpcs.utils.Messaging;

/**
 * Load and Save all data to a mysql server
 * 
 * @author IDragonfire
 * 
 */
public class DMySQLManager extends DDataManager {

	public static final DMySQLManager INSTANCE = new DMySQLManager();
	public static final String MYSQL = "global.mysql.";
	private Connection con;

	private DMySQLManager() {
		String adress = UtilityProperties.getConfig().getString(MYSQL + "ip",
				"127.0.0.1");
		int port = UtilityProperties.getConfig().getInt(MYSQL + "port", 3306);
		String db = UtilityProperties.getConfig().getString(MYSQL + "database",
				"ddm");
		String user = UtilityProperties.getConfig().getString(
				MYSQL + "username", "root");
		String pw = UtilityProperties.getConfig().getString(MYSQL + "password",
				"password");
		System.out.println("jdbc:mysql://" + adress + ":" + port + "/" + db
				+ "?user=" + user + "&password=" + pw);
		try {
			this.con = DriverManager.getConnection("jdbc:mysql://" + adress
					+ ":" + port + "/" + db + "?user=" + user + "&password="
					+ pw);
			Messaging.log("DB init completed");
		} catch (Exception e) {
			Messaging.log("# DB init failed: " + e.getMessage());
		}
	}

	private ResultSet fastStatment(String sql) {
		try {
			PreparedStatement stmt = this.con.prepareStatement(sql);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs;
		} catch (Exception e) {
			checkDB(e);
		}
		return null;
	}

	private void checkDB(Exception e) {
		if (this.con == null) {
			Messaging.log("Datenbankverbindung nicht vorhanden");
		} else {
			e.printStackTrace();
		}
	}

	@Override
	public DValue get(DKey key) {
		try {
			return key.fetch(fastStatment(key.getSQL_SELECT()));
		} catch (Exception e) {
			Messaging.log("Empty Result for: " + key);
		}
		return null;
	}

	public void updateState(String sql, int[] values, String player, int jobid) {
		try {
			PreparedStatement stmt = this.con.prepareStatement(sql);
			stmt.setInt(1, values[0]);
			stmt.setInt(2, values[1]);
			stmt.setString(3, player);
			stmt.setInt(4, jobid);
			System.out.println(stmt);
			stmt.executeUpdate();
		} catch (Exception e) {
			checkDB(e);
		}
	}

	public void insertState(String sql, int[] values, String player, int jobid) {
		try {
			PreparedStatement stmt = this.con.prepareStatement(sql);
			stmt.setString(1, player);
			stmt.setInt(2, jobid);
			stmt.setInt(3, values[0]);
			stmt.setInt(4, values[1]);
			System.out.println(stmt);
			stmt.executeUpdate();
		} catch (Exception e) {
			checkDB(e);
		}
	}

	@Override
	public boolean set(DKey key, DValue value) {
		// TODO Auto-generated method stub
		return false;
	}
}
