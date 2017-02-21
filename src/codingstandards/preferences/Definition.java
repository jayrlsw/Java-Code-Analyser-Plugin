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
	
	void addParams(String param1, String param2, String param3) {
		String[] p = new String[3];
		p[0] = param1;
		p[1] = param2;
		p[2] = param3;
		this.params.add(p);
	}
}