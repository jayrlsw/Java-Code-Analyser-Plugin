package codingstandards.preferences;

public class Parameter {
	private String name;
	private String value;
	
	Parameter(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	void printParameter() {
		System.out.println(name + " " + value);
	}
}
