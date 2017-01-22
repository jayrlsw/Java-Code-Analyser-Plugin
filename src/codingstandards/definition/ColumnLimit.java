package codingstandards.definition;

import java.util.ArrayList;
import java.util.List;

public class ColumnLimit {
	
	List<String> doc;
	int colnum;
	
	String name = "Max characters per line";
	String description = "A high number of characters in a line of code may decrease readability.";
	static String violation = "%d characters found on line. Maximum should be %d.";
	
	/*ColumnLimit(List<String> doc, int colnum) {
		this.doc = doc;
		this.colnum = colnum;
	}*/
	
	public static List<String> scan(String line, int colnum) {
		List<String> results = new ArrayList<String>();
		
		if(line.length() > colnum) {
			Object[] format = new Object[] { line.length(), colnum };
			String r = String.format(violation, format);
			results.add(r);
		}
		
		return results;
	}
	

	
}
