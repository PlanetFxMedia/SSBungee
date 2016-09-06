package de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank.LoadServers;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.MinecraftServer.MinecraftServer;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SSBungee extends Plugin {

	public static SSBungee instance;
	public String database_host;
	public int database_port;
	public String database_db;
	public String database_user;
	public String database_password;
	
	public static SSBungee getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		LoadConfig();
		MySQL.Connect();
		MySQL.LadeTabellen();
		ProxyServer.getInstance().getScheduler().runAsync(instance, new LoadServers());
	}
	
	@Override
	public void onDisable() {
		try {
			MySQL.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void LoadConfig() {
		try {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			File fconfig = new File(getDataFolder().getPath(), "config.yml");
			if (!fconfig.exists()) {
				fconfig.createNewFile();
				Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fconfig);
				config.set("database.host", "localhost");
				config.set("database.port", 3306);
				config.set("database.db", "akonia_cloudsystem");
				config.set("database.user", "root");
				config.set("database.password", "U2ViYXN0aWFuMTIy");
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fconfig);
			}
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fconfig);
			database_host = config.getString("database.host");
			database_port = config.getInt("database.port");
			database_db = config.getString("database.db");
			database_user = config.getString("database.user");
			database_password = config.getString("database.password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addServerToList(MinecraftServer mcs) {
		ServerInfo server = ProxyServer.getInstance().constructServerInfo(mcs.getBungeeCordServername(), InetSocketAddress.createUnresolved("95.156.227.75", mcs.getPort()), "CloudSystem", true);
		ProxyServer.getInstance().getServers().put(mcs.getBungeeCordServername(), server);
	}
}