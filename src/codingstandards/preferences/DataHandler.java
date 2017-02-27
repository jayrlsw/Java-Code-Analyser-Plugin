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
	
	static String getPreferences(String name) {
		IEclipsePreferences store = InstanceScope.INSTANCE.getNode("codingstandards.preferences");
		if(store != null) {
			return store.get(name, "");
		}
		return null;
	}
	
	static void setPreferences(String id, String data) {
		IEclipsePreferences store = InstanceScope.INSTANCE.getNode("codingstandards.preferences");
		store.put(id, data);
		
		try {
			store.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	static void addConfig(String name) {
		List<String> s = getConfigs();
		s.add(name);
		buildConfigs(s);
	}
	
	static void removeConfig(String name) {
		if(name != null){
			List<String> s = getConfigs();
			if(!s.contains(name)) return;
			s.remove(name);
			buildConfigs(s);
		}
	}
	
	static void buildConfigs(List<String> s) {
		StringBuilder sB = new StringBuilder(); 
		for(String c : s) {
			sB.append(c);
			sB.append(";");
		}
		if(sB.length() > 0) {
			sB.setLength(sB.length() - 1);
		}
		setPreferences(listN, sB.toString());
	}
	
	
	boolean checkExistence(String name) {
		for(ConfigList c : configL) {
			if(c.name.equals(name)) return true;
		}
		return false;
	}
	
	static List<String> getConfigs() {
		String p = getPreferences(listN);
		List<String> configList = new LinkedList<String>(Arrays.asList(p.split(";")));
		return configList;
	}
	
	void setTableMaker() {
		List<String> configs = getConfigs();
		configL.clear();
		if(configs.size() < 1) return;
		for(String s : configs) {
			ConfigList cL = new ConfigList(s);
			configL.add(cL);
		}
	}
	
	public List<ConfigList> tableFiller() {
		return configL;
	}
	
	void printConfigs() {
		for(ConfigList c : configL) {
			System.out.println(c.getName());
		}
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
