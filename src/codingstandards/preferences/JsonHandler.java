package codingstandards.preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHandler {
		
	public static void setJson(final Configuration config, final String name) {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final String json = gson.toJson(config);
		DataHandler.setPreferences(name, json);
	}
	
	public static Configuration getConfig(final String name) {
		final String configString = DataHandler.getPreferences(name);
		final Gson gson = new Gson();
		return gson.fromJson(configString, Configuration.class);
	}
}
