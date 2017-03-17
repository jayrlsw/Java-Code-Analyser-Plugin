package codingstandards.definition;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;

import codingstandards.process.BeginScan;
import codingstandards.process.GetSettings;
import codingstandards.process.SyntaxHighlighter;
import codingstandards.process.ViolationData;

public class CommentAlignment {

	final String name = "Comment alignment";
	final String description = "The vertical alignment of commenting can make things messy when lines are edited, as this can stop them from being horizontal and can be difficult to maintain.";
	final String violation = "Comment alignment is in use.";
	
	public CommentAlignment() {}
	
	public List<ViolationData> scan(List<String> doc, IResource r) {
		final List<ViolationData> d = new LinkedList<ViolationData>();
		final List<String> params = GetSettings.getValue("Comment Alignment", r);
		
		if(params == null) {
			return d;
		}
		if(!Boolean.parseBoolean(params.get(0))) {
			return d;
		}
		final List<Integer> check = new LinkedList<Integer>();
		for(int i = 0; i < doc.size(); i++) {
			final String line = BeginScan.replaceIndentation(doc.get(i));
			
			final char[] cArr = line.toCharArray();
			int count = 0;
			char lastChar = 'a';
			for(final char c : cArr) {
				if(lastChar == '/' && c == '/') {
					count--;
					check.add(count);
					d.add(new ViolationData(name, violation, new int[] {i + 1, SyntaxHighlighter.getOffset(doc, i, count), i + 1, doc.get(i).length() - count}));
				}
				lastChar = c;
				count++;
			}
			
		}
		
		if(check.size() < 1) {
			d.clear();
			return d;
		}
		
		final Integer startNum = check.get(0);
		for(int i = 1; i < check.size(); i++) {
			if(check.get(i) != startNum) {
				d.clear();
				break;
			}
		}
		
		return d;
	}
}
