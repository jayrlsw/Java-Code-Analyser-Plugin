package codingstandards.preferences;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public class DataHandler {
	
	final static String listN = "configList";
	static List<ConfigList> configL = new LinkedList<ConfigList>();
	
	public DataHandler() {
		setTableMaker();
	}
	
	static String getPreferences(final String name) {
		final IEclipsePreferences store = InstanceScope.INSTANCE.getNode("codingstandards.preferences");
		if(store != null) {
			return store.get(name, "");
		}
		return null;
	}
	
	static void setPreferences(final String id, final String data) {
		final IEclipsePreferences store = InstanceScope.INSTANCE.getNode("codingstandards.preferences");
		store.put(id, data);
		
		try {
			store.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	static void addConfig(final String name) {
		List<String> configs = getConfigs();
		configs.add(name);
		buildConfigs(configs);
	}
	
	static void removeConfig(final String name) {
		if(name != null){
			final List<String> config = getConfigs();
			if(!config.contains(name)) {
				return;
			}
			config.remove(name);
			buildConfigs(config);
		}
	}
	
	static void buildConfigs(final List<String> s) {
		final StringBuilder stringBuilder = new StringBuilder(); 
		for(final String c : s) {
			stringBuilder.append(c);
			stringBuilder.append(';');
		}
		if(stringBuilder.length() > 0) {
			stringBuilder.setLength(stringBuilder.length() - 1);
		}
		setPreferences(listN, stringBuilder.toString());
	}
	
	
	boolean checkExistence(final String name) {
		boolean result = false;
		for(final ConfigList c : configL) {
			if(c.name.equals(name)) {
				result =  true;
				break;
			}
		}
		return result;
	}
	
	static List<String> getConfigs() {
		final String preferences = getPreferences(listN);
		final List<String> configList = new LinkedList<String>(Arrays.asList(preferences.split(";")));
		return configList;
	}
	
	void setTableMaker() {
		final List<String> configs = getConfigs();
		configL.clear();
		if(configs.size() < 1) {
			return;
		}
		for(final String s : configs) {
			final ConfigList configList = new ConfigList(s);
			configL.add(configList);
		}
	}
	
	public List<ConfigList> tableFiller() {
		return configL;
	}
	
	public class ConfigList {
		String name;
		ConfigList(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
