package de.SebastianMikolai.PlanetFx.ServerSystem.SSBungee.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {

	private static final JsonParser jsonParser = new JsonParser();

	public static JsonObject getAsJsonObject(String strg) {
		if (isJsonValid(strg)) {
			return (JsonObject) jsonParser.parse(strg);
		}
		return new JsonObject();
	}

	public static boolean isJsonValid(String strg) {
		try {
			jsonParser.parse(strg);
			return true;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}
}