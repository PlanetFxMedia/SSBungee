package de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.SSBungee;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.MinecraftServer.MinecraftServer;

public class MySQL {

	public static Connection c = null;
	public static Statement database;
	
	public static void Connect() {
		try {
			c = DriverManager.getConnection("jdbc:mysql://" + SSBungee.getInstance().database_host + ":" + 
					SSBungee.getInstance().database_port + "/" + SSBungee.getInstance().database_db + 
					"?user=" + SSBungee.getInstance().database_user + "&password=" + SSBungee.getInstance().database_password);
			database = c.createStatement();
			SSBungee.getInstance().getLogger().info("Die Verbindung zur Datenbank wurde hergestellt!");
		} catch (Exception e) {
			System.exit(0);
		}
	}

	public static void LadeTabellen() {
		try {
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SHOW TABLES LIKE 'MinecraftServer'");
			if (rss.next()) {
				SSBungee.getInstance().getLogger().info("Die Tabelle MinecraftServer wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE MinecraftServer (id INTEGER PRIMARY KEY AUTO_INCREMENT, BungeeCordServername TEXT, Port INTEGER, Map TEXT, Modi TEXT)");
				SSBungee.getInstance().getLogger().info("Die Tabelle MinecraftServer wurde erstellt! (" + rs + ")");
			}
		} catch (SQLException e) {
			System.exit(0);
		}
	}
	
	public static void addMinecraftServer(MinecraftServer mcs) {
		try {
			Statement stmt = c.createStatement();
			stmt.execute("INSERT INTO MinecraftServer (BungeeCordServername, Port, Map, Modi) VALUES ('" + mcs.getBungeeCordServername() + "', '" + mcs.getPort() +  "', '" + mcs.getMap() + "', '" + mcs.getModi() + "')");
		} catch (SQLException e) {}
	}
	
	public static void deleteMinecraftServer(String BungeeCordServername) {
		try {
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM MinecraftServer WHERE BungeeCordServername='" + BungeeCordServername + "'");
		} catch (SQLException e) {}
	}
}