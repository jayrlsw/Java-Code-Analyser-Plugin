package codingstandards.preferences;

public class Parameter {
	private String name;
	private String value;
	
	Parameter(final String name, final String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
}
