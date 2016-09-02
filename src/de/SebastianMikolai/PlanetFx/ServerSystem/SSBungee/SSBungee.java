package de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee;

import java.io.File;
import java.io.IOException;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank.TCPServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.MinecraftServer.MinecraftServer;

import net.md_5.bungee.BungeeCord;
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
	public int tcp_port = 25606;
	public String bcpath;
	public TCPServer tcp;
	
	public static SSBungee getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		LoadConfig();
		MySQL.Connect();
		MySQL.LadeTabellen();
		MySQL.deleteMinecraftServer("bungee");
		MySQL.addMinecraftServer(new MinecraftServer("bungee", tcp_port, "", "4x1"));
		tcp = new TCPServer(25606);
		tcp.start();
	}
	
	@Override
	public void onDisable() {
		tcp.close();
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
				config.set("version", getDescription().getVersion());
				config.set("database.host", "localhost");
				config.set("database.port", 3306);
				config.set("database.db", "akonia_cloudsystem");
				config.set("database.user", "root");
				config.set("database.password", "U2ViYXN0aWFuMTIy");
				config.set("tcpport", 25566);
				config.set("bcpath", "/home/ServerSystem/BungeeCord/");
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fconfig);
			}
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fconfig);
			database_host = config.getString("database.host");
			database_port = config.getInt("database.port");
			database_db = config.getString("database.db");
			database_user = config.getString("database.user");
			database_password = config.getString("database.password");
			bcpath = config.getString("bcpath");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addServerToConfig(MinecraftServer mcs) {
		try {
			File fconfig = new File(bcpath, "config.yml");
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fconfig);
			config.set("servers." + mcs.getBungeeCordServername() + ".motd", "CloudSystem_" + mcs.getBungeeCordServername());
			config.set("servers." + mcs.getBungeeCordServername() + ".address", "localhost:" + mcs.getPort());
			config.set("servers." + mcs.getBungeeCordServername() + ".restricted", false);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BungeeCord.getInstance().config.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeServerFromConfig(MinecraftServer mcs) {
		try {
			File fconfig = new File(bcpath, "config.yml");
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fconfig);
			config.set("servers." + mcs.getBungeeCordServername(), null);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BungeeCord.getInstance().config.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}