package de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.SSBungee;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.MinecraftServer.MinecraftServer;

public class MySQL {

	public static Connection con;
	
	public static Connection Connect() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://" + SSBungee.getInstance().database_host + ":" + 
					SSBungee.getInstance().database_port + "/" + SSBungee.getInstance().database_db + "?autoReconnect=true", SSBungee.getInstance().database_user, SSBungee.getInstance().database_password);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public static Connection getConnection() {
		try {
			if (con == null) {
				con = Connect();
			} else if (con.isClosed()) {
				con = Connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void LadeTabellen() {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SHOW TABLES LIKE 'MinecraftServer'");
			if (rss.next()) {
				SSBungee.getInstance().getLogger().info("Die Tabelle MinecraftServer wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE MinecraftServer (id INTEGER PRIMARY KEY AUTO_INCREMENT, BungeeCordServername TEXT, Port INTEGER, Map TEXT, Modi TEXT)");
				SSBungee.getInstance().getLogger().info("Die Tabelle MinecraftServer wurde erstellt! (" + rs + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<MinecraftServer> getMinecraftServers() {
		List<MinecraftServer> MinecraftServers = new ArrayList<MinecraftServer>();
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SELECT * FROM MinecraftServer");
			while (rss.next()) {
				MinecraftServers.add(new MinecraftServer(rss.getString("BungeeCordServername"), rss.getInt("Port"), rss.getString("Map"), rss.getString("Modi")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return MinecraftServers;
	}
}