package codingstandards.preferences;

import java.util.ArrayList;

public class Definition {
	
	static final String TEXTBOX = "0";
	static final String CHECKBOX = "1";
	
	private String name;
	private String description;
	private ArrayList<String[]> params = new ArrayList<String[]>(); //Name, Description, Type
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<String[]> getParams() {
		return params;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void setDescription(String description) {
		this.description = description;
	}
	
	void addParams(final String param1, final String param2, final String param3) {
		String[] parameters = new String[3];
		parameters[0] = param1;
		parameters[1] = param2;
		parameters[2] = param3;
		this.params.add(parameters);
	}
}