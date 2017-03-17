package codingstandards.preferences;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	
	private String name;
	private String enabled;
	private List<Parameter> params = new ArrayList<Parameter>();
	
	Rule(final String name, final String enabled) {
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
	
	Parameter getParameter(final String name) {
		for(Parameter parameter : params) {
			if(parameter.getName().equals(name)) return parameter;
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEnabled() {
		return enabled;
	}
	
	public List<Parameter> getParams() {
		return params;
	}
}
