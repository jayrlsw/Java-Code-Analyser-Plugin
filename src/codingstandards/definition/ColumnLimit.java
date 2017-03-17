package codingstandards.definition;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;

import codingstandards.process.BeginScan;
import codingstandards.process.GetSettings;
import codingstandards.process.SyntaxHighlighter;
import codingstandards.process.ViolationData;

public class ColumnLimit {
		
	final String name = "Max characters per line";
	final String description = "A high number of characters in a line of code may decrease readability.";
	final String violation = "%d characters found on line. Maximum should be %d.";
	
	public ColumnLimit() {}
	
	public List<ViolationData> scan(List<String> str, IResource r) {
		
		final List<ViolationData> dataList = new LinkedList<ViolationData>();
		
		final List<String> params = GetSettings.getValue("Column Limit", r); 
		
		if(params == null) {
			return dataList;
		}
		
		for(int i = 0; i < str.size(); i++) {
			final int colnum = Integer.parseInt(params.get(0));
			final String line = BeginScan.replaceIndentation(str.get(i));
			if(line.length() > colnum) {
				final int lineCount = i + 1;
				Object[] format = new Object[] { line.length(), colnum };
				final String violationInfo = String.format(violation, format);
				dataList.add(new ViolationData(name, violationInfo, new int[] {lineCount, SyntaxHighlighter.getOffset(str, i, 0), lineCount, str.get(i).length()}));
			}
		}
		
		return dataList;
	}
}
