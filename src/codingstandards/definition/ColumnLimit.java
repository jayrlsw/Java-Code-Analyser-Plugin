package codingstandards.definition;

import codingstandards.process.ViolationData;

public class ColumnLimit {
	
	int colnum;
	
	String name = "Max characters per line";
	String description = "A high number of characters in a line of code may decrease readability.";
	String violation = "%d characters found on line. Maximum should be %d.";
	
	public ViolationData scan(String line, int colnum) {
		
		if(line.length() > colnum) {
			Object[] format = new Object[] { line.length(), colnum };
			String r = String.format(violation, format);
			return new ViolationData(name, r, new int[] {0, line.length()});
		}
		
		return null;
	}
}
