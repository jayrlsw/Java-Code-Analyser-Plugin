package codingstandards.preferences;

public class Parameter {
	private String name;
	private String value;
	
	Parameter(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	String getName() {
		return name;
	}
	
	String getValue() {
		return value;
	}
	
	void printParameter() {
		System.out.println(name + " " + value);
	}
}
