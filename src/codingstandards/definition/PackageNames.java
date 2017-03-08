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
	
	public PackageNames() {
		
	}
	
	public List<ViolationData> scan(List<String> doc, IResource r) {
		
		List<ViolationData> d = new LinkedList<ViolationData>();
		List<String> params = GetSettings.getValue("Package Names", r);
		if(params == null) return d;
		boolean forceLowerCase = Boolean.parseBoolean((params.get(0)));
		boolean denySpaces = Boolean.parseBoolean(params.get(1));
		
		for(int i = 0; i < doc.size(); i++) {
			String line = doc.get(i);
			int lC = i + 1;
			if(line.startsWith("package")) {
				char[] lineC = line.toCharArray();
				
				boolean capitalisation = false;
				boolean spaces = false;
				for(char w : lineC) {
					if(forceLowerCase && !Character.isLowerCase(w) && Character.isLetter(w)) {
						capitalisation = true;
					}
					if(denySpaces) {
						//w.contains(s)
					}
				}
				if(capitalisation || spaces) {
					String result = "";
					if(capitalisation && spaces) result = "Capitalisation and spaces";
					else if (capitalisation) result = "Capitalisation";
					else if (spaces) result = "Spaces";
					String s = String.format(violation, result);
					d.add(new ViolationData(name, s, new int[] {lC, 8, lC, line.length() - 1}));
				}
			}
			
		}
		
		return d;
	}
	
}
