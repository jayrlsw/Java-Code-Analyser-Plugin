package codingstandards.preferences;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	
	private String name;
	private boolean enabled;
	private List<Parameter> params = new ArrayList<Parameter>();
	
	Rule(final String name, final boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}
	
	void addParameter(Parameter p) {
		params.add(p);
	}
	
	void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public List<Parameter> getParams() {
		return params;
	}
}
