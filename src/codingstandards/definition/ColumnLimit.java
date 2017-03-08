package codingstandards.definition;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;

import codingstandards.process.BeginScan;
import codingstandards.process.GetSettings;
import codingstandards.process.SyntaxHighlighter;
import codingstandards.process.ViolationData;

public class ColumnLimit {
	
	int colnum;
	
	final String name = "Max characters per line";
	final String description = "A high number of characters in a line of code may decrease readability.";
	final String violation = "%d characters found on line. Maximum should be %d.";
	
	public ColumnLimit() {}
	
	public List<ViolationData> scan(List<String> str, IResource r) {
		
		List<ViolationData> d = new LinkedList<ViolationData>();
		
		List<String> params = GetSettings.getValue("Column Limit", r);
		if(params == null) return d;
		colnum = Integer.parseInt(params.get(0));
		
		for(int i = 0; i < str.size(); i++) {
			String line = BeginScan.replaceIndentation(str.get(i));
			int lC = i + 1;
			if(line.length() > colnum) {
				Object[] format = new Object[] { line.length(), colnum };
				String s = String.format(violation, format);
				d.add(new ViolationData(name, s, new int[] {lC, SyntaxHighlighter.getOffset(str, i, 0), lC, str.get(i).length()}));
			}
		}
		
		return d;
	}
}
