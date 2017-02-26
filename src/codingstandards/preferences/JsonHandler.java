package codingstandards.preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHandler {
		
	public static void setJson(Configuration c, String name) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(c);
		DataHandler.setPreferences(name, json);
	}
	
	public static Configuration getConfig(String name) {
		String c = DataHandler.getPreferences(name);
		Gson gson = new Gson();
		Configuration config = gson.fromJson(c, Configuration.class);
		return config;
	}
}
