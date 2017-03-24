package codingstandards.definition;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;

import codingstandards.process.GetSettings;
import codingstandards.process.ViolationData;

public class PackageNames {

	final String name = "Package name capitalisation";
	final String description = "All package names should be lowercase and spaceless to avoid confusing them with methods and classes.";
	final String violation = "%s found inappropriately in package name.";
	
	public PackageNames() {}
	
	public List<ViolationData> scan(List<String> doc, IResource r) {
		
		final List<ViolationData> dataList = new LinkedList<ViolationData>();
		final List<String> params = GetSettings.getValue("Package Names", r);
		if(params == null) {
			return dataList;
		}
		final boolean forceLowerCase = Boolean.parseBoolean((params.get(0)));
		final boolean denySpaces = Boolean.parseBoolean(params.get(1));
		
		for(int i = 0; i < doc.size(); i++) {
			final String line = doc.get(i);
			final int lineCount = i + 1;
			if(line.startsWith("package")) {
				final char[] lineC = line.toCharArray();
				
				boolean capitalisation = false;
				boolean spaces = false;
				for(final char w : lineC) {
					if(forceLowerCase && !Character.isLowerCase(w) && Character.isLetter(w)) {
						capitalisation = true;
					}
					if(denySpaces & w == '_') {
						spaces = true;
					}
				}
				if(capitalisation || spaces) {
					String result = "";
					if(capitalisation && spaces) {
						result = "Capitalisation and spaces";
					}
					else if (capitalisation) {
						result = "Capitalisation";
					}
					else if (spaces) {
						result = "Spaces";
					}
					final String violationInfo = String.format(violation, result);
					dataList.add(new ViolationData(name, violationInfo, new int[] {lineCount, 8, lineCount, line.length() - 1}));
				}
			}
			
		}
		
		return dataList;
	}
	
}
