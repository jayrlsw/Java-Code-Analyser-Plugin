package codingstandards.preferences;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	
	private String name;
	private String enabled;
	private List<Parameter> params = new ArrayList<Parameter>();
	
	Rule(String name, String enabled) {
		this.name = name;
		this.enabled = enabled;
	}
	
	void addParameter(Parameter p) {
		params.add(p);
	}
	
	void setEnabled(boolean enabled) {
		if(enabled) this.enabled = "true";
		else this.enabled = "false";
	}
	
	Parameter getParameter(String name) {
		for(Parameter p : params) {
			if(p.getName().equals(name)) return p;
		}
		return null;
	}
	
	String getName() {
		return name;
	}
	
	String getEnabled() {
		return enabled;
	}
	
	List<Parameter> getParams() {
		return params;
	}
	
	void printRule() {
		System.out.println(name + " " + enabled);
	}
}