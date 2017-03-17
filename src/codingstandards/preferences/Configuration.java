package codingstandards.preferences;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
	
	private String name;
	private List<Rule> rule = new ArrayList<Rule>();
	
	Configuration(String name) {
		this.name = name;
	}

	void addRule(Rule r) {
		rule.add(r);
	}
	
	public Rule getRule(String name) {
		for(Rule r : rule) {
			if(r.getName().equals(name)) return r;
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
}