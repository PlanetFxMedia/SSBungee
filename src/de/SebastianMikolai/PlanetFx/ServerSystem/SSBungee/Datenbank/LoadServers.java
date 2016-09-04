package de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank;

import java.util.Map;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.SSBungee;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.MinecraftServer.MinecraftServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class LoadServers implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				Map<String, ServerInfo> Servers = ProxyServer.getInstance().getServers();
				for (MinecraftServer mcs : MySQL.getMinecraftServers()) {
					if (!Servers.containsKey(mcs.getBungeeCordServername())) {
						SSBungee.getInstance().addServerToList(mcs);
					}
				}
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}