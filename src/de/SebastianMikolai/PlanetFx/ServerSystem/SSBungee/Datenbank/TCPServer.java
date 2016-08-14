package de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Datenbank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.JsonObject;

import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.SSBungee;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.MinecraftServer.MinecraftServer;
import de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Utils.JsonUtil;

public class TCPServer extends Thread {
	
	private ServerSocket serversocket;
	
	public TCPServer(int port) {
		try {
			serversocket = new ServerSocket(port);
			SSBungee.getInstance().getLogger().info("Der TCP-Server läuft auf Port: " + port);
		} catch (IOException e) {}
	}
	
	public void run() {
		try {
			while(true) {
				Socket socket = serversocket.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String stringFromClient = inFromClient.readLine();
				if (JsonUtil.isJsonValid(stringFromClient)) {
					JsonObject jsonObject = JsonUtil.getAsJsonObject(stringFromClient);
					if (jsonObject.get("option").getAsString().equalsIgnoreCase("add")) {
						MinecraftServer mcs = new MinecraftServer(jsonObject.get("servername").getAsString(), Integer.parseInt(jsonObject.get("port").getAsString()), "", "1x1");
						SSBungee.getInstance().addServerToConfig(mcs);
					} else if (jsonObject.get("option").getAsString().equalsIgnoreCase("remove")) {
						MinecraftServer mcs = new MinecraftServer(jsonObject.get("servername").getAsString(), Integer.parseInt(jsonObject.get("port").getAsString()), "", "1x1");
						SSBungee.getInstance().removeServerFromConfig(mcs);
					}
				}
			}
		} catch (IOException e) {}
	}
	
	public void close() {
		try {
			serversocket.close();
		} catch (IOException e) {}
    }
}